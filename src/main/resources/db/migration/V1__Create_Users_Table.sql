-- V1__Create_Person_Table.sql
CREATE TABLE Person (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(100) NOT NULL,
                        year_of_birth INT,
                        password VARCHAR(255),
                        role VARCHAR(255)
);
