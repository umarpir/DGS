package com.dgs.dgs_backend.repository;

import com.dgs.dgs_backend.domain.ClientOrganisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ClientOrganisationRepositoryITest {

    @Autowired
    private ClientOrganisationRepository clientOrganisationRepository;

    @Test
    void testExistsByNameAndIdNot() {
        boolean exists = clientOrganisationRepository.existsByNameAndIdNot("Org One", 2L);
        assertTrue(exists);

        exists = clientOrganisationRepository.existsByNameAndIdNot("invalid Org", 1L);
        assertFalse(exists);
    }

    @Test
    void testExistsByName() {
        boolean exists = clientOrganisationRepository.existsByName("Org One");
        assertTrue(exists);

        exists = clientOrganisationRepository.existsByName("Nonexistent Org");
        assertFalse(exists);
    }

//    @Test
//    void testFindExpiredOrganisations() {
//        LocalDate currentDate = LocalDate.now().minusDays(1);
//        Date testDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        List<ClientOrganisation> expiredOrganisations = clientOrganisationRepository.findExpiredOrganisations(currentDate);
//        assertFalse(expiredOrganisations.isEmpty());
//
//        for (ClientOrganisation org : expiredOrganisations) {
//            assertTrue(org.getExpiryDate().before(testDate));
//            assertTrue(org.isEnabled());
//        }
//    }
}