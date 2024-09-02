package com.dgs.dgs_backend.Exceptions.Personnel;

    public class InvalidDateFormatException extends RuntimeException {
        public InvalidDateFormatException(String date) {
            super("Date provided: " + date + " is in the wrong format");
        }
    }

