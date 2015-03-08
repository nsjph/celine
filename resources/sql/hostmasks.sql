-- name: create-hostmask<!
-- creates a new hostmask record
INSERT INTO hostmasks
(channelhost,admin,op,kick,ban,ignore,created_at)
VALUES (:channelhost,:admin,:op,:kick,:ban,:ignore,strftime('%s','now'))

--name: update-hostmask!
-- update an existing hostmask record
UPDATE hostmasks
SET channelhost = :channelhost, admin = :admin, op = :op, kick = :kick, ban = :ban, ignore = :ignore, updated_at = strftime('%s','now')
WHERE id = :id

-- name: find-hostmask-by-id
-- retrieve a hostmask by id
SELECT * FROM hostmasks
WHERE id = ?

-- name: find-hostmasks
-- retrieve all hostmasks
SELECT * FROM hostmasks

