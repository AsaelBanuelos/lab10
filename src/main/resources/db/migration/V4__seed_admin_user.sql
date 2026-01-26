-- Create an admin user if it does not already exist
-- This is useful for testing admin-only pages and roles

INSERT OR IGNORE INTO users (username, email, password, role)
VALUES (
  'admin@local.test',   -- admin username (same as email)
  'admin@local.test',   -- admin email
  '$2b$12$ga5zP1HRhBLbtlNzkpt56euheIDTQ0nrMCoJ/VWe4ItZLFNf5Dhdi', -- BCrypt-hashed password
  'ROLE_ADMIN'          -- admin role for authorization
);