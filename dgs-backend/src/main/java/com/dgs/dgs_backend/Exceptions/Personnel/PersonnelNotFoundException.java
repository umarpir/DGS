package com.dgs.dgs_backend.Exceptions.ClientOrganisation;

    public class ClientOrganisationNotFoundException extends RuntimeException {
        public ClientOrganisationNotFoundException(Long id) {
            super("ClientOrganisation with ID " + id + " not found.");
        }
    }

