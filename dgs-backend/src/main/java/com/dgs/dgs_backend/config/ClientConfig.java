package com.dgs.dgs_backend.config;

import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import com.dgs.dgs_backend.repository.PersonnelRepository;
import com.dgs.dgs_backend.service.ClientOrganisationService;
import com.dgs.dgs_backend.service.PersonnelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ClientConfig {

    @Bean
    public ClientOrganisationService clientOrganisationService (ClientOrganisationRepository clientOrganisationRepository,
                                                                PersonnelRepository personnelRepository) {
        return new ClientOrganisationService(clientOrganisationRepository, personnelRepository);
    }
    @Bean
    public PersonnelService personnelService (PersonnelRepository personnelRepository,
                                              ClientOrganisationRepository clientOrganisationRepository,
                                              PasswordEncoder passwordEncoder) {
        return new PersonnelService(personnelRepository, clientOrganisationRepository, passwordEncoder);
    }

}
