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

echo "==Extracting tag graph from post titles and bodies==\n";
$progress=1;
foreach($pdo->query("SELECT id, tags FROM posts ") as $row){
  
  $tagmap = extracttags($row['tags']);

  foreach($tagmap as $tag1){
    foreach($tagmap as $tag2){
      $query = "SELECT count(*) FROM tag_graph WHERE tag1=" 
        . $pdo->quote($tag1) . " AND tag2="
        . $pdo->quote($tag2)
      ;
      echo "$query\n";

      $result = $pdo->query($query);
      $result = $result->fetch();
      if($result['count(*)']){
        $query = "UPDATE tag_graph SET weight=weight+1 WHERE (" 
          . "tag1=" . $pdo->quote($tag1) . " AND " 
          . "tag2=" . $pdo->quote($tag2) .
          ") OR ("  
          . "tag2=" . $pdo->quote($tag1) . " AND " 
          . "tag1=" . $pdo->quote($tag2) .
          ")"
        ;
        echo "$query\n";

        $pdo->query($query);
      }else{
        $query = "INSERT INTO tag_graph (tag1, tag2, weight) VALUES ("  
            . $pdo->quote($tag1) . " , " 
            . $pdo->quote($tag2) . " , "
            . "1" . 
          ")"
        ;
        echo "\n$query";
        $pdo->query($query);

        $query = "INSERT INTO tag_graph (tag1, tag2, weight) VALUES ("  
            . $pdo->quote($tag2) . " , " 
            . $pdo->quote($tag1) . " , "
            . "1" . 
          ")"
        ;
        echo "$query\n";
        $pdo->query($query);
      }

    }
  }
}

$pdo->query('END TRANSACTION');

function extracttags($string){

  return explode('><',substr($string,1,-1));
}
