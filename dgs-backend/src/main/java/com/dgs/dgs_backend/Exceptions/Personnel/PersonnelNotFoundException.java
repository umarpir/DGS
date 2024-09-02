package com.dgs.dgs_backend.Exceptions.Personnel;

    public class PersonnelNotFoundException extends RuntimeException {
        public PersonnelNotFoundException(Long id) {
            super("Personnel with ID " + id + " not found.");
        }
    }

