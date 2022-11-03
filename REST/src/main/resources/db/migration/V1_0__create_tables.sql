CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS operation;
DROP TABLE IF EXISTS operation_type;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4 (),
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    creation TIMESTAMP,

    CONSTRAINT proper_email CHECK (email ~* '[^@\s]+@[^@\s]+\.[^@\s]+')
);

CREATE TABLE account (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4 (),
    holder uuid REFERENCES "user"(id) DEFERRABLE,
    title VARCHAR(255) NOT NULL,
    balance NUMERIC default 0,
    creation TIMESTAMP
);

CREATE TABLE operation_type (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4 (),
    creator uuid REFERENCES "user"(id) DEFERRABLE,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE operation (
   id uuid PRIMARY KEY DEFAULT uuid_generate_v4 (),
   account uuid REFERENCES account (id) DEFERRABLE,
   sum NUMERIC NOT NULL,
   type uuid REFERENCES operation_type (id) DEFERRABLE,
   creation TIMESTAMP
);
