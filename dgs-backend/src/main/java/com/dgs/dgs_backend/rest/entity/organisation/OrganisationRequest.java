package com.dgs.dgs_backend.rest.entity.organisation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Builder
public class UpdateOrganisationRequest {
    private String name;
    private Date expiryDate;
    private boolean enabled;
}
