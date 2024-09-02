-- TODO: ADD MORE DUMMY DATA !
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
                                                                                    ('Org Two', '2024-02-01', '2025-02-01', FALSE),
                                                                                    ('Org Three', '2024-03-01', '2025-03-01', TRUE),
                                                                                    ('Org Four', '2024-04-01', '2025-04-01', TRUE),
                                                                                    ('Org Five', '2024-05-01', '2025-05-01', FALSE);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('John', 'Doe', 'john_doe1', 'securepasswordhash1', 'john.doe1@example.com', '123-456-7890', 1),
                                                                                                                       ('Jane', 'Doe', 'jane_doe1', 'securepasswordhash2', 'jane.doe1@example.com', '123-456-7891', 1),
                                                                                                                       ('Jim', 'Beam', 'jim_beam1', 'securepasswordhash3', 'jim.beam1@example.com', '123-456-7892', 1);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('Alice', 'Smith', 'alice_smith2', 'securepasswordhash4', 'alice.smith2@example.com', '098-765-4320', 2),
                                                                                                                       ('Bob', 'Smith', 'bob_smith2', 'securepasswordhash5', 'bob.smith2@example.com', '098-765-4321', 2),
                                                                                                                       ('Charlie', 'Brown', 'charlie_brown2', 'securepasswordhash6', 'charlie.brown2@example.com', '098-765-4322', 2);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('David', 'Johnson', 'david_johnson3', 'securepasswordhash7', 'david.johnson3@example.com', '234-567-8901', 3),
                                                                                                                       ('Eve', 'Johnson', 'eve_johnson3', 'securepasswordhash8', 'eve.johnson3@example.com', '234-567-8902', 3),
                                                                                                                       ('Frank', 'Wright', 'frank_wright3', 'securepasswordhash9', 'frank.wright3@example.com', '234-567-8903', 3);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('Grace', 'Hopper', 'grace_hopper4', 'securepasswordhash10', 'grace.hopper4@example.com', '345-678-9012', 4),
                                                                                                                       ('Hank', 'Pym', 'hank_pym4', 'securepasswordhash11', 'hank.pym4@example.com', '345-678-9013', 4),
                                                                                                                       ('Ivy', 'Green', 'ivy_green4', 'securepasswordhash12', 'ivy.green4@example.com', '345-678-9014', 4);

INSERT INTO personnel (first_name, last_name, username, password, email, telephone_number, client_organisation_id) VALUES
                                                                                                                       ('Jack', 'Black', 'jack_black5', 'securepasswordhash13', 'jack.black5@example.com', '456-789-0123', 5),
                                                                                                                       ('Kara', 'White', 'kara_white5', 'securepasswordhash14', 'kara.white5@example.com', '456-789-0124', 5),
                                                                                                                       ('Leo', 'Gray', 'leo_gray5', 'securepasswordhash15', 'leo.gray5@example.com', '456-789-0125', 5);