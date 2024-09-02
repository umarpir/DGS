package com.dgs.dgs_backend.Exceptions.ClientOrganisation;

    public class ClientOrganisationInvalidDateFormatException extends RuntimeException {
        public ClientOrganisationInvalidDateFormatException(String date) {
            super("Date provided: " + date + " is in the wrong format");
        }
    }

