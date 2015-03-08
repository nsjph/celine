-- name: create-factoid<!
-- creates a new user record
INSERT INTO factoids
(key,fact,author,visible,created_at)
VALUES (:key,:fact,:author,1,strftime('%s','now'))

--name: replace-factoid<!
-- update an existing factoid record
UPDATE factoids
SET fact = :fact, author = :author, updated_at = strftime('%s','now')
WHERE key = ?

-- name: find-factoid
-- retrieve a fact by id
SELECT * FROM factoids
WHERE id = :id

-- name: find-factoid-by-key
-- retrieve a fact given the key.
SELECT * FROM factoids
WHERE key = ?

-- name: find-factoids 
-- retrieve all factoids
SELECT * FROM factoids



-- key VARCHAR(32),
-- 	fact VARCHAR(500),
-- 	author VARCHAR(350), 
-- 	visible BOOLEAN,
-- 	ts INTEGER);