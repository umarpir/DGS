package com.dgs.dgs_backend.repository;

import com.dgs.dgs_backend.domain.ClientOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientOrganisationRepository extends JpaRepository<ClientOrganisation, Long> {
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByName(String name);
}
