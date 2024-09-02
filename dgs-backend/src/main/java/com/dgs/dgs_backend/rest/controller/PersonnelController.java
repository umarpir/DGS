package com.dgs.dgs_backend.rest.controller;
import com.dgs.dgs_backend.rest.dto.PersonnelDTO;
import com.dgs.dgs_backend.rest.requests.personnel.PersonnelRequest;
import com.dgs.dgs_backend.service.PersonnelService;
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
@RequestMapping(value = "/dgs-api/v1/personnel", produces = APPLICATION_JSON_VALUE)
public class PersonnelController {
    private final PersonnelService personnelService;
    @GetMapping
    public ResponseEntity<List<PersonnelDTO>> getAllPersonnel() {
        List<PersonnelDTO> response =  personnelService.findAllPersonnel();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{personnelId}")
    public ResponseEntity<PersonnelDTO> getPersonnelById(@PathVariable Long personnelId) {
        PersonnelDTO response = personnelService.findPersonnelById(Long.valueOf(personnelId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/organisation/{organisationId}")
    public ResponseEntity<List<PersonnelDTO>> getPersonnelByOrganisationId(@PathVariable Long organisationId) {
        List<PersonnelDTO> response = personnelService.findPersonnelByOrganisationId(Long.valueOf(organisationId));
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping("/{personnelId}")
    public ResponseEntity<String> deletePersonnelById(@PathVariable Long personnelId) {
        String response = personnelService.deletePersonnelById(Long.valueOf(personnelId));
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{personnelId}")
    public ResponseEntity<PersonnelDTO> updateOrganisation(
            @PathVariable Long personnelId,
            @RequestBody PersonnelRequest updatePersonnelRequest) {
        PersonnelDTO updatedPersonnel = personnelService.updatePersonnel(personnelId, updatePersonnelRequest);
        return ResponseEntity.ok(updatedPersonnel);
    }

    @PostMapping("/{organisationId}")
    public ResponseEntity<PersonnelDTO> saveOrganisation(@PathVariable Long organisationId,
                                                      @RequestBody PersonnelRequest personnelRequest) {
        PersonnelDTO savedPersonnel = personnelService.savePersonnel(organisationId, personnelRequest);
        return ResponseEntity.ok(savedPersonnel);
    }

}
