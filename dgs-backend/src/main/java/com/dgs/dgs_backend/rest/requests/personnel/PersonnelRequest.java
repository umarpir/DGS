package com.dgs.dgs_backend.rest.requests.personnel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PersonnelRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String telephoneNumber;

}
