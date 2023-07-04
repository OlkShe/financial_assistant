-- V1__Create_Categories_Table.sql
CREATE TABLE categories
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255),
    user_id    BIGINT REFERENCES users (id),
    created_at TIMESTAMP
);