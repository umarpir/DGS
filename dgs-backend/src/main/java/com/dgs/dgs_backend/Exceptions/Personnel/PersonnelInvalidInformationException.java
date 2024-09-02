package com.dgs.dgs_backend.Exceptions.Personnel;

    public class PersonnelUsernameExistsException extends RuntimeException {
        public PersonnelUsernameExistsException(String name) {
            super("Personnel with username " + name + " already exists.");
        }
    }

