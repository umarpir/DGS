package com.dgs.dgs_backend.controller;

import com.dgs.dgs_backend.domain.ClientOrganisation;
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
@RequestMapping(value = "/dgs-api/v1", produces = APPLICATION_JSON_VALUE)

public class ClientOrganisationController {
    private ClientOrganisationService clientOrganisationService;
    @GetMapping("/clients")
    public ResponseEntity<List<ClientOrganisation>> getAllClientOrganisations() {
        List<ClientOrganisation> response =  clientOrganisationService.findAllOrganisations();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/clients/{organisationId}")
    public ResponseEntity<ClientOrganisation> getClientOrganisationById(@PathVariable String organisationId) {
            ClientOrganisation response = clientOrganisationService.findOrganisationById(Long.valueOf(organisationId));
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/clients/{organisationId}")
    public ResponseEntity<String> DeleteClientOrganisationById(@PathVariable String organisationId) {
            clientOrganisationService.deleteOrganisationById(Long.valueOf(organisationId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientOrganisation> updateOrganisation(
            @PathVariable Long id,
            @RequestBody ClientOrganisation updatedOrganisation) {
        ClientOrganisation updatedOrg = clientOrganisationService.updateOrganisation(id, updatedOrganisation);
        return ResponseEntity.ok(updatedOrg);
    }

    @PostMapping("/clients")
    public ResponseEntity<ClientOrganisation> saveOrganisation(@RequestBody ClientOrganisation clientOrganisation) {
        ClientOrganisation savedOrganisation = clientOrganisationService.saveOrganisation(clientOrganisation);
        return ResponseEntity.ok(savedOrganisation);
    }

}
