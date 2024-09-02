package com.dgs.dgs_backend.rest.requests.organisation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganisationRequest {
    private String name;
    private String expiryDate;
    private Boolean enabled;
}
