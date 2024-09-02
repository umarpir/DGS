package com.dgs.dgs_backend.Exceptions.ClientOrganisation;

    public class ClientOrganisationMissingParametersException extends RuntimeException {
        public ClientOrganisationMissingParametersException(String name) {
            super("Request for client organisation is missing parameter: " + name + ".");
        }
    }

