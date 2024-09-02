package com.dgs.dgs_backend.service;

import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationMissingParametersException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNameExistsException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNotFoundException;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationInvalidDateFormatException;
import com.dgs.dgs_backend.domain.ClientOrganisation;
import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import com.dgs.dgs_backend.repository.PersonnelRepository;
import com.dgs.dgs_backend.rest.dto.ClientOrganisationDTO;
import com.dgs.dgs_backend.rest.requests.organisation.OrganisationRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClientOrganisationService {
    private ClientOrganisationRepository clientOrganisationRepository;
    private PersonnelRepository personnelRepository;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<ClientOrganisationDTO> findAllOrganisations() {
        return clientOrganisationRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public ClientOrganisationDTO findOrganisationById(Long id) {
        return convertToDTO(clientOrganisationRepository.findById(id)
                .orElseThrow(() -> new ClientOrganisationNotFoundException(id)));
    }
    @Transactional
    public String deleteOrganisationById(Long id) {
        if (clientOrganisationRepository.existsById(id)) {
            deleteAllPersonnelsInOrganisation(id);
            clientOrganisationRepository.deleteById(id);
            return "Organisation deleted successfully";
        } else {
            throw new ClientOrganisationNotFoundException(id);
        }
    }

    @SneakyThrows
    @Transactional
    public ClientOrganisationDTO updateOrganisation(Long id, OrganisationRequest updatedOrganisation) {
        validateClientOrganisation(updatedOrganisation);
        Optional<ClientOrganisation> existingOrganisation = clientOrganisationRepository.findById(id);
        if (existingOrganisation.isPresent()) {
            if (clientOrganisationRepository.existsByNameAndIdNot(updatedOrganisation.getName(), id)) {
                throw new ClientOrganisationNameExistsException(updatedOrganisation.getName());
            }
            ClientOrganisation organisation = existingOrganisation.get();
            organisation.setName(updatedOrganisation.getName());
            organisation.setExpiryDate(DATE_FORMAT.parse(updatedOrganisation.getExpiryDate()));
            organisation.setEnabled(updatedOrganisation.getEnabled());


            return convertToDTO(clientOrganisationRepository.save(organisation));
        } else {
            throw new ClientOrganisationNotFoundException(id);
        }
    }

    @SneakyThrows
    @Transactional
    public ClientOrganisationDTO saveOrganisation(OrganisationRequest clientOrganisation) {
        validateClientOrganisation(clientOrganisation);

        if (clientOrganisationRepository.existsByName(clientOrganisation.getName())) {
            throw new ClientOrganisationNameExistsException(clientOrganisation.getName());
        }
        ClientOrganisation newClientOrg = new ClientOrganisation();
        newClientOrg.setEnabled(clientOrganisation.getEnabled());
        newClientOrg.setExpiryDate(DATE_FORMAT.parse(clientOrganisation.getExpiryDate()));
        newClientOrg.setName(clientOrganisation.getName());
        newClientOrg.setRegistrationDate(Date.from(Instant.now()));
        return convertToDTO(clientOrganisationRepository.save(newClientOrg));
    }

    private void deleteAllPersonnelsInOrganisation(Long id) {
        personnelRepository.deleteAllByClientOrganisationId(id);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void disableExpiredOrganisations() {
        List<ClientOrganisation> expiredOrganisations = clientOrganisationRepository.findExpiredOrganisations(LocalDate.now());
        for (ClientOrganisation organisation : expiredOrganisations) {
            organisation.setEnabled(false);
            clientOrganisationRepository.save(organisation);
        }
    }

    private ClientOrganisationDTO convertToDTO(ClientOrganisation organisation) {
        ClientOrganisationDTO dto = new ClientOrganisationDTO();
        dto.setId(organisation.getId());
        dto.setName(organisation.getName());
        dto.setEnabled(organisation.isEnabled());
        dto.setExpiryDate(organisation.getExpiryDate());
        dto.setRegistrationDate(organisation.getRegistrationDate());
        return dto;
    }

    private void validateClientOrganisation(OrganisationRequest clientOrganisation) {
        if (!StringUtils.hasText(clientOrganisation.getName())) {
            throw new ClientOrganisationMissingParametersException("Missing parameter: name");
        }
        Date expiryDate;
        try {
            expiryDate = DATE_FORMAT.parse(clientOrganisation.getExpiryDate());
        } catch (ParseException e) {
            throw new ClientOrganisationInvalidDateFormatException("Invalid date format: " + clientOrganisation.getExpiryDate());
        }
        if (!expiryDate.after(Date.from(Instant.now()))) {
            throw new ClientOrganisationMissingParametersException("Missing parameter: expiryDate");
        }
        if (clientOrganisation.getEnabled() == null) { // Check if enabled is null
            throw new ClientOrganisationMissingParametersException("Missing parameter: enabled");
        }
    }
}
