-- Rebuild notes table to properly link notes to users
-- SQLite is a bit limited with ALTER TABLE, so the safest way
-- is to recreate the table with the new column and constraints.

-- Step 1: Create a new notes table that includes user ownership
CREATE TABLE IF NOT EXISTS notes_new (
  id INTEGER PRIMARY KEY AUTOINCREMENT, -- unique note ID
  title TEXT NOT NULL,                  -- note title
  content TEXT NOT NULL,                -- note content
  user_id INTEGER NOT NULL,             -- which user owns this note
  CONSTRAINT fk_notes_user
    FOREIGN KEY (user_id) REFERENCES users(id) -- enforce ownership at DB level
);

-- Step 2: Remove the old notes table (the one without user_id)
DROP TABLE notes;

-- Step 3: Rename the new table so the app keeps using "notes"
ALTER TABLE notes_new RENAME TO notes;

-- Step 4: Add an index on user_id
-- This makes "find my notes" queries faster and is used all the time
CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);