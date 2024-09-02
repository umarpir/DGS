package com.dgs.dgs_backend.Exceptions.ClientOrganisation;

    public class ClientOrganisationNameExistsException extends RuntimeException {
        public ClientOrganisationNameExistsException(String name) {
            super("ClientOrganisation with name " + name + " already exists.");
        }
    }

