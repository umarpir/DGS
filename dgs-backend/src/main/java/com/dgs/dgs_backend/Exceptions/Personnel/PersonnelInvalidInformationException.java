package com.dgs.dgs_backend.Exceptions.Personnel;

    public class PersonnelInvalidInformationException extends RuntimeException {
        public PersonnelInvalidInformationException(String field) {
            super("Invalid field: " + field);
        }
    }

