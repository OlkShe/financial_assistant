-- V1__Create_Users_Table.sql
CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    login      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL
);
