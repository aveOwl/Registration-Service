DROP TABLE IF EXISTS users;

-- User Data Table
CREATE TABLE users (
  id                BIGSERIAL NOT NULL,
  email             VARCHAR(60) NOT NULL,
  password          VARCHAR(60) NOT NULL,
  is_confirmed      VARCHAR(60) NOT NULL,
  CONSTRAINT user_id_pk PRIMARY KEY (id),
  CONSTRAINT unique_email UNIQUE (email)
);