package com.dgs.dgs_backend.controller;

import com.dgs.dgs_backend.domain.Personnel;
import com.dgs.dgs_backend.service.PersonnelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/dgs-api/v1/personnel", produces = APPLICATION_JSON_VALUE)
public class PersonnelController {
    private final PersonnelService personnelService;
    @GetMapping
    public ResponseEntity<List<Personnel>> getAllPersonnel() {
        List<Personnel> response =  personnelService.findAllPersonnel();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{personnelId}")
    public ResponseEntity<Personnel> getClientOrganisationById(@PathVariable String personnelId) {
        Personnel response = personnelService.findPersonnelById(Long.valueOf(personnelId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
//
    @DeleteMapping("/{personnelId}")
    public ResponseEntity<String> deletePersonnelById(@PathVariable String personnelId) {
        personnelService.deletePersonnelById(Long.valueOf(personnelId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
//    @PutMapping("/{personnelId}")
//    public ResponseEntity<ClientOrganisation> updateOrganisation(
//            @PathVariable Long id,
//            @RequestBody ClientOrganisation updatedOrganisation) {
//        ClientOrganisation updatedOrg = clientOrganisationService.updateOrganisation(id, updatedOrganisation);
//        return ResponseEntity.ok(updatedOrg);
//    }
//
//    @PostMapping("/personnelId")
//    public ResponseEntity<ClientOrganisation> saveOrganisation(@RequestBody ClientOrganisation clientOrganisation) {
//        ClientOrganisation savedOrganisation = clientOrganisationService.saveOrganisation(clientOrganisation);
//        return ResponseEntity.ok(savedOrganisation);
//    }

}
