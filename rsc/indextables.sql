/*
DELETE FROM searchindex_comments   ;
DELETE FROM searchindex_post_titles;
DELETE FROM searchindex_posts      ;
DELETE FROM searchindex_tags       ;
*/

DROP TABLE IF EXISTS searchindex_comments ;
CREATE TABLE 
searchindex_comments 
(
  id   INT                                                                   , 
  word TEXT                                                                  ,
  CONSTRAINT searchindex_comments_pk PRIMARY KEY (id, word)                  ,
  CONSTRAINT searchindex_comments_fk FOREIGN KEY (id) REFERENCES comments (id)
);

DROP TABLE IF EXISTS searchindex_posts;
CREATE TABLE searchindex_posts
(
  id   INT                                                              ,
  word TEXT                                                             ,
  CONSTRAINT searchindex_posts_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_posts_fk FOREIGN KEY (id) REFERENCES posts (id)
);

DROP TABLE IF EXISTS searchindex_post_titles;
CREATE TABLE searchindex_post_titles
(
  id   INT                                                                    ,
  word TEXT                                                                   ,
  CONSTRAINT searchindex_post_titles_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_post_titles_fk FOREIGN KEY (id) REFERENCES posts (id)
);

DROP TABLE IF EXISTS searchindex_tags;
CREATE TABLE searchindex_tags
(
  id   INT                                                              ,
  tag  TEXT                                                             ,
  CONSTRAINT searchindex_tags_pk PRIMARY KEY (id, tag)                  ,
  CONSTRAINT searchindex_tags_fk FOREIGN KEY (id) REFERENCES posts (id)
);

/* search indexes, those indexes should improve search speed */
CREATE INDEX IF NOT EXISTS searchindex_comments_index    ON searchindex_comments    (word);
CREATE INDEX IF NOT EXISTS searchindex_post_titles_index ON searchindex_post_titles (word);
CREATE INDEX IF NOT EXISTS searchindex_post_index        ON searchindex_posts       (word);
CREATE INDEX IF NOT EXISTS searchindex_tags_index        ON searchindex_tags        (tag );

/* insert indexes, those indexes can be removed after first indexing */
CREATE INDEX IF NOT EXISTS searchindex_comments_insertindex    ON searchindex_comments    (id, word);
CREATE INDEX IF NOT EXISTS searchindex_post_titles_insertindex ON searchindex_post_titles (id, word);
CREATE INDEX IF NOT EXISTS searchindex_post_insertindex        ON searchindex_posts       (id, word);
CREATE INDEX IF NOT EXISTS searchindex_tags_insertindex        ON searchindex_tags        (id, tag) ;
