-- Add role column for authorization
ALTER TABLE users
ADD COLUMN role TEXT NOT NULL DEFAULT 'ROLE_USER';
