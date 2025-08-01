-- Drop existing tables if they exist
-- DROP TABLE IF EXISTS users;
-- CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users
(
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(90) NOT NULL,
  password VARCHAR(255) NOT NULL
);

