package com.dgs.dgs_backend.rest.service;

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
import com.dgs.dgs_backend.service.PersonnelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonnelServiceUTests {

    @Mock
    private PersonnelRepository personnelRepository;

    @Mock
    private ClientOrganisationRepository clientOrganisationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonnelService personnelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllPersonnel() {
        Personnel personnel1 = new Personnel();
        personnel1.setId(1L);
        personnel1.setFirstName("John");
        Personnel personnel2 = new Personnel();
        personnel2.setId(2L);
        personnel2.setFirstName("Jane");

        when(personnelRepository.findAll()).thenReturn(Arrays.asList(personnel1, personnel2));

        List<PersonnelDTO> personnelList = personnelService.findAllPersonnel();

        assertEquals(2, personnelList.size());
        verify(personnelRepository, times(1)).findAll();
    }

    @Test
    void findPersonnelById() {
        Personnel personnel = new Personnel();
        personnel.setId(1L);
        personnel.setFirstName("John");

        when(personnelRepository.findById(1L)).thenReturn(Optional.of(personnel));

        PersonnelDTO personnelDTO = personnelService.findPersonnelById(1L);

        assertNotNull(personnelDTO);
        assertEquals("John", personnelDTO.getFirstName());
        verify(personnelRepository, times(1)).findById(1L);
    }

    @Test
    void findPersonnelById_NotFound() {
        when(personnelRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PersonnelNotFoundException.class, () -> {
            personnelService.findPersonnelById(1L);
        });

        String expectedMessage = "Personnel with ID 1 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(personnelRepository, times(1)).findById(1L);
    }

    @Test
    void findPersonnelByOrganisationId() {
        Personnel personnel1 = new Personnel();
        personnel1.setId(1L);
        personnel1.setFirstName("John");
        Personnel personnel2 = new Personnel();
        personnel2.setId(2L);
        personnel2.setFirstName("Jane");

        when(clientOrganisationRepository.existsById(1L)).thenReturn(true);
        when(personnelRepository.findPersonnelByClientOrganisationId(1L)).thenReturn(Arrays.asList(personnel1, personnel2));

        List<PersonnelDTO> personnelList = personnelService.findPersonnelByOrganisationId(1L);

        assertEquals(2, personnelList.size());
        verify(clientOrganisationRepository, times(1)).existsById(1L);
        verify(personnelRepository, times(1)).findPersonnelByClientOrganisationId(1L);
    }

    @Test
    void findPersonnelByOrganisationId_OrgNotFound() {
        when(clientOrganisationRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ClientOrganisationNotFoundException.class, () -> {
            personnelService.findPersonnelByOrganisationId(1L);
        });

        String expectedMessage = "ClientOrganisation with ID 1 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(clientOrganisationRepository, times(1)).existsById(1L);
        verify(personnelRepository, never()).findPersonnelByClientOrganisationId(anyLong());
    }

    @Test
    void deletePersonnelById() {
        when(personnelRepository.existsById(1L)).thenReturn(true);

        String result = personnelService.deletePersonnelById(1L);

        assertEquals("Personnel successfully deleted.", result);
        verify(personnelRepository, times(1)).existsById(1L);
        verify(personnelRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePersonnelById_NotFound() {
        when(personnelRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(PersonnelNotFoundException.class, () -> {
            personnelService.deletePersonnelById(1L);
        });

        String expectedMessage = "Personnel with ID 1 not found.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage,actualMessage);
        verify(personnelRepository, times(1)).existsById(1L);
        verify(personnelRepository, never()).deleteById(anyLong());
    }

    @Test
    void updatePersonnel() {
        PersonnelRequest updatedPersonnel = new PersonnelRequest();
        updatedPersonnel.setFirstName("Updated John");
        updatedPersonnel.setPassword("newpassword");
        updatedPersonnel.setUsername("updated_username");

        Personnel existingPersonnel = new Personnel();
        existingPersonnel.setId(1L);
        existingPersonnel.setFirstName("John");
        existingPersonnel.setPassword("oldpassword");

        when(personnelRepository.findById(1L)).thenReturn(Optional.of(existingPersonnel));
        when(personnelRepository.existsByUsernameAndIdNot("updated_username", 1L)).thenReturn(false);
        when(passwordEncoder.matches("newpassword", "oldpassword")).thenReturn(false);
        when(passwordEncoder.encode("newpassword")).thenReturn("hashed_newpassword");
        when(personnelRepository.save(any(Personnel.class))).thenAnswer(i -> i.getArguments()[0]);

        PersonnelDTO result = personnelService.updatePersonnel(1L, updatedPersonnel);

        assertNotNull(result);
        assertEquals("Updated John", result.getFirstName());
        verify(personnelRepository, times(1)).findById(1L);
        verify(personnelRepository, times(1)).existsByUsernameAndIdNot("updated_username", 1L);
        verify(personnelRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    void savePersonnel() {
        PersonnelRequest newPersonnel = new PersonnelRequest();
        newPersonnel.setFirstName("New John");
        newPersonnel.setPassword("testpass");
        newPersonnel.setUsername("testusre");
        newPersonnel.setEmail("test@test.com");
        newPersonnel.setTelephoneNumber("07432007286");

        ClientOrganisation clientOrganisation = new ClientOrganisation();
        clientOrganisation.setId(1L);

        when(clientOrganisationRepository.findById(1L)).thenReturn(Optional.of(clientOrganisation));
        when(personnelRepository.existsPersonnelByUsername("testusre")).thenReturn(false);
        when(passwordEncoder.encode("testpass")).thenReturn("hashed_password");
        when(personnelRepository.save(any(Personnel.class))).thenAnswer(i -> i.getArguments()[0]);

        PersonnelDTO result = personnelService.savePersonnel(1L, newPersonnel);

        assertNotNull(result);
        assertEquals("New John", result.getFirstName());
        verify(clientOrganisationRepository, times(1)).findById(1L);
        verify(personnelRepository, times(1)).existsPersonnelByUsername("testusre");
        verify(personnelRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    void InvalidPersonnelEmailInformation() {
        when(clientOrganisationRepository.findById(anyLong())).thenReturn(Optional.of(new ClientOrganisation()));
        PersonnelRequest invalidEmailRequest = new PersonnelRequest();
        invalidEmailRequest.setPassword("validpassword");
        invalidEmailRequest.setEmail("invalid-email");
        invalidEmailRequest.setTelephoneNumber("12345678901");
        assertThrows(PersonnelInvalidInformationException.class, () -> personnelService.savePersonnel(1L, invalidEmailRequest));
    }
    @Test
    void InvalidPersonnelPhoneInformation() {
        when(clientOrganisationRepository.findById(anyLong())).thenReturn(Optional.of(new ClientOrganisation()));

        PersonnelRequest invalidPhoneRequest = new PersonnelRequest();
        invalidPhoneRequest.setPassword("validpassword");
        invalidPhoneRequest.setEmail("valid@example.com");
        invalidPhoneRequest.setTelephoneNumber("12345");
        assertThrows(PersonnelInvalidInformationException.class, () -> personnelService.savePersonnel(1L, invalidPhoneRequest));
    }
    @Test
    void InvalidPersonnelPasswordInformation() {
        when(clientOrganisationRepository.findById(anyLong())).thenReturn(Optional.of(new ClientOrganisation()));
        PersonnelRequest invalidPassRequest = new PersonnelRequest();
        invalidPassRequest.setPassword("");
        invalidPassRequest.setEmail("valid@email.com");
        invalidPassRequest.setTelephoneNumber("12345678910");
        assertThrows(PersonnelInvalidInformationException.class, () -> personnelService.savePersonnel(1L, invalidPassRequest));
    }
}
