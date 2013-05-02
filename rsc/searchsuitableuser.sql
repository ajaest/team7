SELECT count(*), users.owner_user_id FROM (SELECT DISTINCT owner_user_id FROM posts WHERE post_type_id=1
INTERSECT
SELECT DISTINCT owner_user_id FROM posts WHERE post_type_id=2) AS users JOIN posts ON posts.owner_user_id=users.owner_user_id GROUP BY users.owner_user_id ORDER BY count(*) DESC;

SELECT * FROM users WHERE id=438992;
UPDATE users SET email_hash="4b9411a9b28f9063ea75e5fee24bb2a8"  WHERE id=438992;

SELECT * FROM users WHERE email_hash="4b9411a9b28f9063ea75e5fee24bb2a8";
