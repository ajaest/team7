/*
DELETE FROM searchindex_comments   ;
DELETE FROM searchindex_question_titles;
DELETE FROM searchindex_questions      ;
DELETE FROM searchindex_tags       ;
*/
CREATE TABLE favourite_posts
(
  post_id INTEGER,
  user_id INTEGER,
  CONSTRAINT favourite_posts_pk         PRIMARY KEY (post_id, user_id),
  CONSTRAINT favourite_posts_post_id_fk FOREIGN KEY (post_id) REFERENCES posts (id),
  CONSTRAINT favourite_posts_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS searchindex_comments ;
CREATE TABLE searchindex_comments 
(
  id   INTEGER                                                                , 
  word TEXT                                                               ,
  CONSTRAINT searchindex_comments_pk PRIMARY KEY (id, word)               ,
  CONSTRAINT searchindex_comments_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
	
);

DROP TABLE IF EXISTS searchindex_questions;
CREATE TABLE searchindex_questions
(
  id   INTEGER                                                           ,
  word TEXT                                                             ,
  CONSTRAINT searchindex_questions_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_questions_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS searchindex_responses;
CREATE TABLE searchindex_responses
(
  id   INTEGER                                                              ,
  word TEXT                                                             ,
  CONSTRAINT searchindex_responses_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_responses_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
  /*CONSTRAINT searchindex_responses_isresponse CHECK (SELECT DISTINCT count(post_type_id) FROM posts NATURAL JOIN searchindex_responses*/
);

DROP TABLE IF EXISTS searchindex_question_titles;
CREATE TABLE searchindex_question_titles
(
  id   INTEGER                                                                    ,
  word TEXT                                                                   ,
  CONSTRAINT searchindex_question_titles_pk PRIMARY KEY (id, word)                ,
  CONSTRAINT searchindex_question_titles_fk FOREIGN KEY (id) REFERENCES posts (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS searchindex_tags;
CREATE TABLE searchindex_tags
(
  id   INTEGER                                                              ,
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
   weight INTEGER                                                ,
   CONSTRAINT tag_graph_pk PRIMARY KEY (tag1, tag2)          
);

/* FK indexes */
/* CREATE INDEX IF EXISTS posts_fk ON posts (id) /* Not necessary since sqlite create autmatically indexes for PKs*/

/* search indexes, those indexes should improve search speed */
CREATE INDEX IF NOT EXISTS searchindex_comments_index        ON searchindex_comments        (word);
CREATE INDEX IF NOT EXISTS searchindex_question_titles_index ON searchindex_question_titles (word);
CREATE INDEX IF NOT EXISTS searchindex_questions_index       ON searchindex_questions       (word);
CREATE INDEX IF NOT EXISTS searchindex_questions_responses   ON searchindex_responses       (word);
CREATE INDEX IF NOT EXISTS searchindex_tags_index            ON searchindex_tags            (tag );
CREATE INDEX tag_graph_tag1_index ON tag_graph (tag1);
CREATE INDEX tag_graph_tag2_index ON tag_graph (tag2);

/* insert indexes, those indexes can be removed after first indexing */
/*
CREATE INDEX IF NOT EXISTS searchindex_comments_insertindex    ON searchindex_comments    (id, word);
CREATE INDEX IF NOT EXISTS searchindex_question_titles_insertindex ON searchindex_question_titles (id, word);
CREATE INDEX IF NOT EXISTS searchindex_question_insertindex        ON searchindex_questions       (id, word);
CREATE INDEX IF NOT EXISTS searchindex_tags_insertindex        ON searchindex_tags        (id, tag) ;

*/
