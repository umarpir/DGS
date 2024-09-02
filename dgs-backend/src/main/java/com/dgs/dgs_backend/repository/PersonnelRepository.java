package com.dgs.dgs_backend.repository;

import com.dgs.dgs_backend.domain.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsPersonnelByUsername(String username);
    void deleteAllByClientOrganisationId(Long id);
    List<Personnel> findPersonnelByClientOrganisationId(Long id);
}
