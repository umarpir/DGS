package com.dgs.dgs_backend.rest.service;
import com.dgs.dgs_backend.Exceptions.ClientOrganisation.ClientOrganisationNotFoundException;
import com.dgs.dgs_backend.domain.ClientOrganisation;
import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import com.dgs.dgs_backend.repository.PersonnelRepository;
import com.dgs.dgs_backend.rest.dto.ClientOrganisationDTO;
import com.dgs.dgs_backend.rest.requests.organisation.OrganisationRequest;
import com.dgs.dgs_backend.service.ClientOrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientOrganisationServiceUTests {

    @Mock
    private ClientOrganisationRepository clientOrganisationRepository;
    @Mock
    private PersonnelRepository personnelRepository;
    @InjectMocks
    private ClientOrganisationService clientOrganisationService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void findAllOrganisations() {
        ClientOrganisation org1 = new ClientOrganisation();
        org1.setId(1L);
        org1.setName("Org One");
        ClientOrganisation org2 = new ClientOrganisation();
        org2.setId(2L);
        org2.setName("Org Two");

        when(clientOrganisationRepository.findAll()).thenReturn(Arrays.asList(org1, org2));

        List<ClientOrganisationDTO> organisations = clientOrganisationService.findAllOrganisations();

        assertEquals(2, organisations.size());
        verify(clientOrganisationRepository, times(1)).findAll();
    }

    @Test
    void findOrganisationById() {
        ClientOrganisation org = new ClientOrganisation();
        org.setId(1L);
        org.setName("Org One");
        when(clientOrganisationRepository.findById(1L)).thenReturn(Optional.of(org));
        ClientOrganisationDTO organisation = clientOrganisationService.findOrganisationById(1L);
        assertNotNull(organisation);
        assertEquals("Org One", organisation.getName());
        verify(clientOrganisationRepository, times(1)).findById(1L);
    }

    @Test
    void findOrganisationById_NotFound() {
        when(clientOrganisationRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ClientOrganisationNotFoundException.class, () -> {
            clientOrganisationService.findOrganisationById(1L);
        });
        String expectedMessage = "ClientOrganisation with ID 1 not found.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
        verify(clientOrganisationRepository, times(1)).findById(1L);
    }

    @Test
    void deleteOrganisationById() {
        when(clientOrganisationRepository.existsById(1L)).thenReturn(true);
        String result = clientOrganisationService.deleteOrganisationById(1L);
        assertEquals("Organisation deleted successfully", result);
        verify(clientOrganisationRepository, times(1)).existsById(1L);
        verify(personnelRepository, times(1)).deleteAllByClientOrganisationId(1L);
        verify(clientOrganisationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrganisationById_NotFound() {
        when(clientOrganisationRepository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(ClientOrganisationNotFoundException.class, () -> {
            clientOrganisationService.deleteOrganisationById(1L);
        });

        String expectedMessage = "ClientOrganisation with ID 1 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(clientOrganisationRepository, times(1)).existsById(1L);
        verify(personnelRepository, never()).deleteAllByClientOrganisationId(anyLong());
        verify(clientOrganisationRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateOrganisation() throws Exception {
        OrganisationRequest updatedOrganisation = OrganisationRequest.builder()
                .name("Updated Org").expiryDate("2025-01-01").enabled(true).build();

        ClientOrganisation existingOrg = new ClientOrganisation();
        existingOrg.setId(1L);
        existingOrg.setName("Org One");

        when(clientOrganisationRepository.findById(1L)).thenReturn(Optional.of(existingOrg));
        when(clientOrganisationRepository.existsByNameAndIdNot("Updated Org", 1L)).thenReturn(false);
        when(clientOrganisationRepository.save(any(ClientOrganisation.class))).thenAnswer(i -> i.getArguments()[0]);

        ClientOrganisationDTO result = clientOrganisationService.updateOrganisation(1L, updatedOrganisation);
        assertNotNull(result);
        assertEquals("Updated Org", result.getName());
        verify(clientOrganisationRepository, times(1)).findById(1L);
        verify(clientOrganisationRepository, times(1)).existsByNameAndIdNot("Updated Org", 1L);
        verify(clientOrganisationRepository, times(1)).save(any(ClientOrganisation.class));
    }

    @Test
    void saveOrganisation() throws Exception {
        OrganisationRequest newOrganisation = OrganisationRequest.builder()
                .name("New Org").expiryDate("2025-01-01").enabled(true).build();
        when(clientOrganisationRepository.existsByName("New Org")).thenReturn(false);
        when(clientOrganisationRepository.save(any(ClientOrganisation.class))).thenAnswer(i -> i.getArguments()[0]);

        ClientOrganisationDTO result = clientOrganisationService.saveOrganisation(newOrganisation);

        assertNotNull(result);
        assertEquals("New Org", result.getName());
        verify(clientOrganisationRepository, times(1)).existsByName("New Org");
        verify(clientOrganisationRepository, times(1)).save(any(ClientOrganisation.class));
    }

    @Test
    void disableExpiredOrganisations() {
        ClientOrganisation org1 = new ClientOrganisation();
        org1.setId(1L);
        org1.setName("Org One");
        org1.setEnabled(true);

        ClientOrganisation org2 = new ClientOrganisation();
        org2.setId(2L);
        org2.setName("Org Two");
        org2.setEnabled(true);

        when(clientOrganisationRepository.findExpiredOrganisations(LocalDate.now())).thenReturn(Arrays.asList(org1, org2));
        clientOrganisationService.disableExpiredOrganisations();

        assertFalse(org1.isEnabled());
        assertFalse(org2.isEnabled());
        verify(clientOrganisationRepository, times(1)).findExpiredOrganisations(LocalDate.now());
        verify(clientOrganisationRepository, times(2)).save(any(ClientOrganisation.class));
    }
}
