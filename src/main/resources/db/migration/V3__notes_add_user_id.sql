-- Rebuild notes table to properly link notes to users

-- Step 1: Create a new notes table that includes user ownership
CREATE TABLE IF NOT EXISTS notes_new (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  user_id INTEGER NOT NULL,
  CONSTRAINT fk_notes_user
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Step 2: Remove the old notes table (the one without user_id)
DROP TABLE notes;

-- Step 3: Rename the new table so the app keeps using "notes"
ALTER TABLE notes_new RENAME TO notes;

-- Step 4: Add an index on user_id
CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);