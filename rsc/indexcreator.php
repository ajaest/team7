#!/usr/bin/php
<?php

global $pdo;

/* starts processing */
$pdo = new PDO("sqlite:so.sqlite");

if(!$pdo){
  echo "Error when trying to connect to database, exiting\n";
  exit(-1);
}

$pdo->query('BEGIN TRANSACTION');

echo "==Extracting words from post titles and bodies==\n";
$progress=1;
foreach($pdo->query("SELECT id, title, body, tags FROM posts ") as $row){
  
  echo "$progress Post " . $row['id'] . "\n";
  $progress++;
  
  foreach(extractwords($row['title']) as $word){
    $query = "INSERT OR IGNORE INTO searchindex_post_titles (id, word) VALUES (" . 
      $pdo->quote($row['id']) . "," . 
      $pdo->quote($word     ) . ")\n"
    ;

    $pdo->query($query);
  }

  foreach(extractwords($row['body']) as $word){
    $query = "INSERT OR IGNORE INTO searchindex_posts (id, word) VALUES (" . 
      $pdo->quote($row['id']) . "," . 
      $pdo->quote($word     ) . ")\n"
    ;

    $pdo->query($query);
  }

  foreach(extracttags($row['tags']) as $tag){
    $query = "INSERT OR IGNORE INTO searchindex_tags (id, tag) VALUES (" . 
      $pdo->quote($row['id']) . "," . 
      $pdo->quote($tag     ) . ")\n"
    ;

    $pdo->query($query);
  }
}

$pdo->query('END TRANSACTION');

$pdo->query('BEGIN TRANSACTION');

echo "== Indexing post comments ==\n";
$progress=1;
foreach($pdo->query("SELECT post_id AS id,text FROM comments ") as $row){

  echo "$progress Comment " . $row['id'] . "\n";
  $progress++;

  foreach(extractwords($row['text']) as $word){
    $query = "INSERT OR IGNORE INTO searchindex_comments (id, word) VALUES (" . 
      $pdo->quote($row['id']) . "," . 
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
