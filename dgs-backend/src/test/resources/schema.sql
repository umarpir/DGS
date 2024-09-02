-- Drop existing tables to avoid conflicts
DROP TABLE IF EXISTS personnel;
DROP TABLE IF EXISTS client_organisation;
CREATE TABLE client_organisation (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     registration_date DATE NOT NULL,
                                     expiry_date DATE NOT NULL,
                                     enabled BOOLEAN NOT NULL
);

-- Create table for personnel
CREATE TABLE personnel (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           username VARCHAR(255) NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           telephone_number VARCHAR(255) NOT NULL,
                           client_organisation_id BIGINT,
                           FOREIGN KEY (client_organisation_id) REFERENCES client_organisation(id)
);