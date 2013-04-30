#!/usr/bin/php
<?php

global $pdo;

/* starts processing */
$pdo = new PDO("sqlite:so.sqlite.android");

if(!$pdo){
  echo "Error when trying to connect to database, exiting\n";
  exit(-1);
}

$pdo->query('BEGIN TRANSACTION');

echo "==Extracting words from post titles and bodies==\n";
$progress=1;
foreach($pdo->query("SELECT id, title, body, tags, post_type_id, parent_id FROM posts ") as $row){

  $post_type = $row['post_type_id'];
  $index_tables = $post_type=='1' ? 'searchindex_questions' : 'searchindex_responses';
  $post_id      = $post_type=='1' ? 'id' : 'parent_id';
  $post_id = $row[$post_id];
  
  
  echo "$progress " . ($post_type=='1' ? "Question" : "Answer") . " " .   $row['id'] . "\n";
  $progress++;
  
  if($post_type=='1'){
    foreach(extractwords($row['title']) as $word){
      $query = "INSERT OR IGNORE INTO searchindex_question_titles (id, word) VALUES (" . 
        $pdo->quote($post_id  ) . "," . 
        $pdo->quote($word     ) . ")\n"
      ;

      $pdo->query($query);
    }
  }

  foreach(extractwords($row['body']) as $word){
    $query = "INSERT OR IGNORE INTO " . $index_tables .  " (id, word) VALUES (" . 
      $pdo->quote($post_id  ) . "," . 
      $pdo->quote($word     ) . ")\n"
    ;

    $pdo->query($query);
  }

  foreach(extracttags($row['tags']) as $tag){
    $query = "INSERT OR IGNORE INTO searchindex_tags (id, tag) VALUES (" . 
      $pdo->quote($post_id ) . "," . 
      $pdo->quote($tag     ) . ")\n"
    ;

    $pdo->query($query);
  }
}

$pdo->query('END TRANSACTION');

$pdo->query('BEGIN TRANSACTION');

echo "== Indexing post comments ==\n";
$progress=1;
foreach($pdo->query("SELECT posts.post_type_id AS post_type_id, comments.post_id AS id,comments.text AS text, posts.parent_id AS parent_id FROM comments JOIN posts ON comments.post_id=posts.id") as $row){

  $post_type = $row['post_type_id'];
  $post_id = $post_type=='1' ? 'id' : 'parent_id';
  $post_id = $row[$post_id];

  echo "$progress Comment " . $row['id'] . "\n";
  $progress++;

  foreach(extractwords($row['text']) as $word){
    $query = "INSERT OR IGNORE INTO searchindex_comments (id, word) VALUES (" . 
      $pdo->quote($post_id  ) . "," . 
      $pdo->quote($word     ) . ")\n"
    ;

    $pdo->query($query);
  }
}

$pdo->query('END TRANSACTION');


//Auxiliar methods
function extractwords($string){

  $words;

  preg_match_all('/[A-Za-z_-]+/', $string, $words);

  return $words[0]?$words[0]:array();
}

function extracttags($string){

  return explode('><',substr($string,1,-1));
}
