package com.dgs.dgs_backend.service;

import com.dgs.dgs_backend.Exceptions.ClientOrganisationNotFoundException;
import com.dgs.dgs_backend.domain.ClientOrganisation;
import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientOrganisationService {
    private ClientOrganisationRepository clientOrganisationRepository;

    public List<ClientOrganisation> findAllOrganisations() {
        return clientOrganisationRepository.findAll();
    }
    public ClientOrganisation findOrganisationById(Long id) {
        return clientOrganisationRepository.findById(id)
                .orElseThrow(() -> new ClientOrganisationNotFoundException(id));
    }
    @Transactional
    public void deleteOrganisationById(Long id) {
        if (clientOrganisationRepository.existsById(id)) {
            clientOrganisationRepository.deleteById(id);
        } else {
            throw new ClientOrganisationNotFoundException(id);
        }
    }

    @Transactional
    public ClientOrganisation updateOrganisation(Long id, ClientOrganisation updatedOrganisation) {
        Optional<ClientOrganisation> existingOrganisation = clientOrganisationRepository.findById(id);
        if (existingOrganisation.isPresent()) {
            if (clientOrganisationRepository.existsByNameAndIdNot(updatedOrganisation.getName(), id)) {
                throw new IllegalArgumentException("Organisation name must be unique. The name " + updatedOrganisation.getName() + " is already taken.");
            }
            ClientOrganisation organisation = existingOrganisation.get();
            organisation.setName(updatedOrganisation.getName());
            organisation.setExpiryDate(updatedOrganisation.getExpiryDate());
            organisation.setEnabled(updatedOrganisation.isEnabled());


            return clientOrganisationRepository.save(organisation);
        } else {
            throw new ClientOrganisationNotFoundException(id);
        }
    }

    @Transactional
    public ClientOrganisation saveOrganisation(ClientOrganisation clientOrganisation) {
        validateClientOrganisation(clientOrganisation);

        if (clientOrganisationRepository.existsByName(clientOrganisation.getName())) {
            throw new IllegalArgumentException("Organisation name must be unique. The name " + clientOrganisation.getName() + " is already taken.");
        }

        return clientOrganisationRepository.save(clientOrganisation);
    }

    private void validateClientOrganisation(ClientOrganisation clientOrganisation) {
        if (!StringUtils.hasText(clientOrganisation.getName())) {
            throw new IllegalArgumentException("Missing parameter: name");
        }
        if (clientOrganisation.getRegistrationDate() == null) {
            throw new IllegalArgumentException("Missing parameter: registrationDate");
        }
        if (clientOrganisation.getExpiryDate() == null) {
            throw new IllegalArgumentException("Missing parameter: expiryDate");
        }
        if (clientOrganisation.isEnabled() == false) {
            throw new IllegalArgumentException("Missing parameter: enabled");
        }
    }
}
