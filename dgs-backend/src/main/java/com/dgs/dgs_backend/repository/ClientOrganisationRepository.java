package com.dgs.dgs_backend.repository;

import com.dgs.dgs_backend.domain.ClientOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientOrganisationRepository extends JpaRepository<ClientOrganisation, Long> {
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByName(String name);
    @Query("SELECT org FROM ClientOrganisation org WHERE org.expiryDate < :currentDate AND org.enabled = true")
    List<ClientOrganisation> findExpiredOrganisations(LocalDate currentDate);
}
