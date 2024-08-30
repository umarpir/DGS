package com.dgs.dgs_backend.config;

import com.dgs.dgs_backend.repository.ClientOrganisationRepository;
import com.dgs.dgs_backend.service.ClientOrganisationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientOrganisationConfig {

    @Bean
    ClientOrganisationService clientOrganisationService (ClientOrganisationRepository clientOrganisationRepository) {
        return new ClientOrganisationService(clientOrganisationRepository);
    }

}
