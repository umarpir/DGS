package com.dgs.dgs_backend.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonnelDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String telephoneNumber;
    private Long clientOrganisationId;


}
