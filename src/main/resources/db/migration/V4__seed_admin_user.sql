-- Create an admin user if it does not already exist

INSERT OR IGNORE INTO users (username, email, password, role)
VALUES (
  'admin@local.test',
  'admin@local.test',
  '$2b$12$ga5zP1HRhBLbtlNzkpt56euheIDTQ0nrMCoJ/VWe4ItZLFNf5Dhdi', -- BCrypt-hashed password
  'ROLE_ADMIN'
);