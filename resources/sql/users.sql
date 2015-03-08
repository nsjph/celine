-- name: create-user!
-- creates a new user record
INSERT INTO users
(user,pass,host)
VALUES (:user,:pass,:host)

--name: update-user!
-- update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- name: get-user
-- retrieve a used given the id.
SELECT * FROM users
WHERE id = :id
