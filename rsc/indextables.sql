/*
DELETE FROM searchindex_comments   ;
DELETE FROM searchindex_post_titles;
DELETE FROM searchindex_posts      ;
DELETE FROM searchindex_tags       ;
*/

DROP TABLE IF EXISTS searchindex_comments ;
CREATE TABLE searchindex_comments 
(
  id   INT                                                                , 
  word TEXT                                                               ,
  CONSTRAINT searchindex_comments_pk PRIMARY KEY (id, word)               ,
  CONSTRAINT searchindex_comments_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
	
);

DROP TABLE IF EXISTS searchindex_posts;
CREATE TABLE searchindex_posts
(
  id   INT                                                              ,
  word TEXT                                                             ,
  CONSTRAINT searchindex_posts_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_posts_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS searchindex_post_titles;
CREATE TABLE searchindex_post_titles
(
  id   INT                                                                    ,
  word TEXT                                                                   ,
  CONSTRAINT searchindex_post_titles_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_post_titles_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS searchindex_tags;
CREATE TABLE searchindex_tags
(
  id   INT                                                              ,
  tag  TEXT                                                             ,
  CONSTRAINT searchindex_tags_pk PRIMARY KEY (id, tag)                  ,
  CONSTRAINT searchindex_tags_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS tag_graph;
 CREATE TABLE tag_graph
 (
   tag1   TEXT                                               ,
   tag2   TEXT                                               ,
   weight INT                                                ,
   CONSTRAINT tag_graph_pk PRIMARY KEY (tag1, tag2)          
)

/* FK indexes */
/* CREATE INDEX IF EXISTS posts_fk ON posts (id) /* Not necessary since sqlite create autmatically indexes for PKs*/

/* search indexes, those indexes should improve search speed */
CREATE INDEX IF NOT EXISTS searchindex_comments_index    ON searchindex_comments    (word);
CREATE INDEX IF NOT EXISTS searchindex_post_titles_index ON searchindex_post_titles (word);
CREATE INDEX IF NOT EXISTS searchindex_posts_index       ON searchindex_posts       (word);
CREATE INDEX IF NOT EXISTS searchindex_tags_index        ON searchindex_tags        (tag );
CREATE INDEX tag_graph_tag1_index ON tag_graph (tag1);
CREATE INDEX tag_graph_tag2_index ON tag_graph (tag2);

/* insert indexes, those indexes can be removed after first indexing */
/*
CREATE INDEX IF NOT EXISTS searchindex_comments_insertindex    ON searchindex_comments    (id, word);
CREATE INDEX IF NOT EXISTS searchindex_post_titles_insertindex ON searchindex_post_titles (id, word);
CREATE INDEX IF NOT EXISTS searchindex_post_insertindex        ON searchindex_posts       (id, word);
CREATE INDEX IF NOT EXISTS searchindex_tags_insertindex        ON searchindex_tags        (id, tag) ;

*/
