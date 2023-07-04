-- V2__Create_Categories_Table.sql
CREATE TABLE categories
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255),
    user_id    BIGINT REFERENCES users (id),
    type       VARCHAR(10), --expenses or income
    created_at TIMESTAMP,
    CONSTRAINT unique_name_user_id UNIQUE (name, user_id)
);
