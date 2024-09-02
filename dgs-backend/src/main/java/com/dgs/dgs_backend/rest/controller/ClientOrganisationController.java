package com.dgs.dgs_backend.rest.controller;

import com.dgs.dgs_backend.rest.dto.ClientOrganisationDTO;
import com.dgs.dgs_backend.rest.requests.organisation.OrganisationRequest;
import com.dgs.dgs_backend.service.ClientOrganisationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/dgs-api/v1/organisations", produces = APPLICATION_JSON_VALUE)
public class ClientOrganisationController {
    private ClientOrganisationService clientOrganisationService;
    @GetMapping
    public ResponseEntity<List<ClientOrganisationDTO>> getAllClientOrganisations() {
        List<ClientOrganisationDTO> response =  clientOrganisationService.findAllOrganisations();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{organisationId}")
    public ResponseEntity<ClientOrganisationDTO> getClientOrganisationById(@PathVariable String organisationId) {
            ClientOrganisationDTO response = clientOrganisationService.findOrganisationById(Long.valueOf(organisationId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{organisationId}")
    public ResponseEntity<String> DeleteClientOrganisationById(@PathVariable String organisationId) {
            String response = clientOrganisationService.deleteOrganisationById(Long.valueOf(organisationId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{organisationId}")
    public ResponseEntity<ClientOrganisationDTO> updateOrganisation(
            @PathVariable String organisationId,
            @RequestBody OrganisationRequest updatedOrganisation) {
        ClientOrganisationDTO updatedOrg = clientOrganisationService.updateOrganisation(Long.valueOf(organisationId), updatedOrganisation);
        return ResponseEntity.status(HttpStatus.OK.value()).body(updatedOrg);
    }

    @PostMapping("/")
    public ResponseEntity<ClientOrganisationDTO> saveOrganisation(@RequestBody OrganisationRequest clientOrganisation) {
        ClientOrganisationDTO savedOrganisation = clientOrganisationService.saveOrganisation(clientOrganisation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganisation);
    }

}
