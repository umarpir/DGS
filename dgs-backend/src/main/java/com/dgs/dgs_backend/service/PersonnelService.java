package com.dgs.dgs_backend.service;

import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNotFoundException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelInvalidInformationException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelNotFoundException;
import com.dgs.dgs_backend.Exceptions.Personnel.PersonnelUsernameExistsException;
import com.dgs.dgs_backend.domain.ClientOrganisation;
import com.dgs.dgs_backend.domain.Personnel;
import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import com.dgs.dgs_backend.repository.PersonnelRepository;
import com.dgs.dgs_backend.rest.dto.PersonnelDTO;
import com.dgs.dgs_backend.rest.requests.personnel.PersonnelRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PersonnelService {
    private PersonnelRepository personnelRepository;
    private ClientOrganisationRepository clientOrganisationRepository;
    private PasswordEncoder passwordEncoder;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );
    public List<PersonnelDTO> findAllPersonnel() {
//        return personnelRepository.findAll();
        return personnelRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }
    public PersonnelDTO findPersonnelById(Long id) {
        return convertToDTO(personnelRepository.findById(id)
                .orElseThrow(() -> new PersonnelNotFoundException(id)));
    }
    public List<PersonnelDTO> findPersonnelByOrganisationId(Long id) {
        if (!clientOrganisationRepository.existsById(id)) {
            throw new ClientOrganisationNotFoundException(id);
        }
        List<Personnel> personnelList = personnelRepository.findPersonnelByClientOrganisationId(id);
        return personnelList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public String deletePersonnelById(Long id) {
        if (personnelRepository.existsById(id)) {
            personnelRepository.deleteById(id);
            return "Personnel successfully deleted.";
        } else {
            throw new PersonnelNotFoundException(id);
        }
    }

    @Transactional
    public PersonnelDTO updatePersonnel(Long id, PersonnelRequest updatedPersonnel) {
        Optional<Personnel> existingPersonnel = personnelRepository.findById(id);
        if (existingPersonnel.isPresent()) {
            if (personnelRepository.existsByUsernameAndIdNot(updatedPersonnel.getUsername(), id)) {
                throw new PersonnelUsernameExistsException(updatedPersonnel.getUsername());
            }
            String newPassword = updatedPersonnel.getPassword();
            if(updatedPersonnel.getPassword() == null) {
                newPassword = existingPersonnel.get().getPassword();
            }
            String existingPassword = existingPersonnel.get().getPassword();
            String hashedPassword;

            if (passwordEncoder.matches(newPassword, existingPassword)) {

                hashedPassword = existingPassword;
            } else {
                hashedPassword = passwordEncoder.encode(newPassword);
            }
            Personnel personnel = existingPersonnel.get();
            personnel.setEmail(updatedPersonnel.getEmail());
            personnel.setPassword(hashedPassword);
            personnel.setUsername(updatedPersonnel.getUsername());
            personnel.setFirstName(updatedPersonnel.getFirstName());
            personnel.setLastName(updatedPersonnel.getLastName());
            personnel.setTelephoneNumber(updatedPersonnel.getTelephoneNumber());


            return convertToDTO(personnelRepository.save(personnel));
        } else {
            throw new PersonnelNotFoundException(id);
        }
    }
    @Transactional
    public PersonnelDTO savePersonnel(Long organisationId, PersonnelRequest personnelRequest) {
        Optional<ClientOrganisation> clientOrganisationOptional = clientOrganisationRepository.findById(organisationId);
        if (!clientOrganisationOptional.isPresent()) {
            throw new ClientOrganisationNotFoundException(organisationId);
        }
        if (personnelRepository.existsPersonnelByUsername(personnelRequest.getUsername())) {
            throw new PersonnelUsernameExistsException(personnelRequest.getUsername());
        }
        validatePersonnel(personnelRequest);
        String hashedPassword = passwordEncoder.encode(personnelRequest.getPassword());
        Personnel newPersonnel = new Personnel();
        newPersonnel.setFirstName(personnelRequest.getFirstName());
        newPersonnel.setLastName(personnelRequest.getLastName());
        newPersonnel.setUsername(personnelRequest.getUsername());
        newPersonnel.setPassword(hashedPassword);
        newPersonnel.setEmail(personnelRequest.getEmail());
        newPersonnel.setTelephoneNumber(personnelRequest.getTelephoneNumber());
        newPersonnel.setClientOrganisationId(clientOrganisationOptional.get().getId());
        return convertToDTO(personnelRepository.save(newPersonnel));

    }
    public PersonnelDTO convertToDTO(Personnel personnel) {
        PersonnelDTO dto = new PersonnelDTO();
        dto.setId(personnel.getId());
        dto.setFirstName(personnel.getFirstName());
        dto.setLastName(personnel.getLastName());
        dto.setUsername(personnel.getUsername());
        dto.setEmail(personnel.getEmail());
        dto.setTelephoneNumber(personnel.getTelephoneNumber());
        dto.setClientOrganisationId(personnel.getClientOrganisationId());
        return dto;
    }
    private void validatePersonnel(PersonnelRequest personnelRequest) {
        if (personnelRequest.getPassword() == null || personnelRequest.getPassword().length() <= 5) {
            throw new PersonnelInvalidInformationException("password");
        }

        if (!EMAIL_PATTERN.matcher(personnelRequest.getEmail()).matches()) {
            throw new PersonnelInvalidInformationException("email");
        }

        if (personnelRequest.getTelephoneNumber() == null || personnelRequest.getTelephoneNumber().length() != 11 || !personnelRequest.getTelephoneNumber().matches("\\d+")) {
            throw new PersonnelInvalidInformationException("telephone");
        }
    }
}
