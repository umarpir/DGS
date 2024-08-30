--TODO: ADD MORE DUMMY DATA !
CREATE DATABASE IF NOT EXISTS mydatabase;

USE mydatabase;

CREATE TABLE IF NOT EXISTS client_organisation (
                                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   name VARCHAR(255) NOT NULL UNIQUE,
    registration_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    enabled BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS personnel (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telephone_number VARCHAR(20),
    client_organisation_id BIGINT,
    FOREIGN KEY (client_organisation_id) REFERENCES client_organisation(id)
    );

INSERT INTO client_organisation (name, registration_date, expiry_date, enabled) VALUES
                                                                                    ('Org One', '2024-01-01', '2025-01-01', TRUE),
                                                                                    ('Org Two', '2024-02-01', '2025-02-01', FALSE);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('John', 'Doe', 'john_doe', 'securepasswordhash1', 'john.doe@example.com', '123-456-7890', 1),
                                                                                                                       ('Jane', 'Smith', 'jane_smith', 'securepasswordhash2', 'jane.smith@example.com', '098-765-4321', 2);