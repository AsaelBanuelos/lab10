-- Add role column to users table
-- This is used by Spring Security for authorization (roles)
ALTER TABLE users
ADD COLUMN role TEXT NOT NULL DEFAULT 'ROLE_USER';