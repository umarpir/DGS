package com.dgs.dgs_backend.rest.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientOrganisationDTO {
    private Long id;
    private String name;
    private Boolean enabled;
    private Date expiryDate;
    private Date registrationDate;
}
