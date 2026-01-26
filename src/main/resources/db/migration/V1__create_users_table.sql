-- Create users table
-- This table stores authentication data for users
-- SQLite-friendly syntax (AUTOINCREMENT works with JPA IDENTITY)
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT, -- unique user ID
  username TEXT NOT NULL UNIQUE,        -- used by Spring Security (same as email)
  email TEXT NOT NULL UNIQUE,           -- login identifier, must be unique
  password TEXT NOT NULL                -- stores BCrypt-hashed password (never plain text)
);

-- Create notes table
-- This table stores notes created by users
CREATE TABLE IF NOT EXISTS notes (
  id INTEGER PRIMARY KEY AUTOINCREMENT, -- unique note ID
  title TEXT NOT NULL,                  -- note title
  content TEXT NOT NULL                 -- note content/body
);