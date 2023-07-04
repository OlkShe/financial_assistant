-- V3__Create_Transactions_Table.sql
CREATE TABLE transactions
(
    id            SERIAL PRIMARY KEY,
    category_id   BIGINT REFERENCES categories (id) ON DELETE CASCADE,
    description   VARCHAR(255) DEFAULT NULL,
    user_id       BIGINT REFERENCES users (id),
    name          VARCHAR(255),
    amount        INTEGER,
    is_accounting BOOLEAN,
    created_at    TIMESTAMP
);