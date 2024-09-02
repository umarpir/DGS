package com.dgs.dgs_backend.repository;

import com.dgs.dgs_backend.domain.Personnel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
class PersonnelRepositoryITest {

    @Autowired
    private PersonnelRepository personnelRepository;

    @Test
    void testExistsByUsernameAndIdNot() {
        boolean exists = personnelRepository.existsByUsernameAndIdNot("john_doe1", 2L);
        assertTrue(exists);
        exists = personnelRepository.existsByUsernameAndIdNot("invald", 1L);
        assertFalse(exists);
    }
    @Test
    void testExistsPersonnelByUsername() {
        boolean exists = personnelRepository.existsPersonnelByUsername("john_doe1");
        assertTrue(exists);

        exists = personnelRepository.existsPersonnelByUsername("invalid");
        assertFalse(exists);
    }

    @Test
    void testDeleteAllByClientOrganisationId() {
        personnelRepository.deleteAllByClientOrganisationId(1L);
        List<Personnel> personnel = personnelRepository.findPersonnelByClientOrganisationId(1L);
        assertTrue(personnel.isEmpty());
    }

    @Test
    void testFindPersonnelByClientOrganisationId() {
        List<Personnel> personnel = personnelRepository.findPersonnelByClientOrganisationId(1L);
        assertFalse(personnel.isEmpty());

        for (Personnel person : personnel) {
            assertEquals(1L, person.getClientOrganisationId());
        }
    }
}
