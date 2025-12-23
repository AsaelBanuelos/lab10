-- Rebuild notes table to add user_id and FK (SQLite-safe approach without PRAGMA)

CREATE TABLE IF NOT EXISTS notes_new (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  user_id INTEGER NOT NULL,
  CONSTRAINT fk_notes_user
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- IMPORTANT:
-- If you already had notes, you must decide how to handle them.
-- For the lab (strict ownership), we do NOT migrate old rows by default.

DROP TABLE notes;
ALTER TABLE notes_new RENAME TO notes;

CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);
