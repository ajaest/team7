#!/usr/bin/php
<?php


$pdo = new PDO("sqlite:so.sqlite");

if(!$pdo){
  echo "Error when trying to connect to database, exiting\n";
  exit(-1);
}

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

function extractwords($string){

  $words;

  preg_match_all('/[A-Za-z_-]{2,}/', $string, $words);

  return $words[0]?$words[0]:array();
}

function extracttags($string){

  return explode('><',substr($string,1,-1));
}
