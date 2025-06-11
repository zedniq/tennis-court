package repository;

import com.IQproject.court.model.Court;
import com.IQproject.court.repository.CourtRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.IQproject.court.CourtApplication.class)
@Transactional
public class CourtRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CourtRepository courtRepository;

    @BeforeEach
    void setUp() {
        em.createQuery("DELETE FROM Court").executeUpdate();
    }

    @Test
    void testSaveUpdateCourt() {
        String originalName = "Center Court";
        String newName = "Left Court";

        Court court = new Court(originalName, 1L);
        Court saved = courtRepository.save(court);

        assertNotNull(saved);
        assertNotNull(saved.getId());

        Court found = em.find(Court.class, saved.getId());

        assertNotNull(found);
        assertEquals(originalName, found.getName());

        court.setName(newName);
        saved = courtRepository.save(court);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(newName, saved.getName());
    }

    @Test
    void testFindAllReturnsOnlyNotDeleted() {
        Court court1 = new Court("A", 1L);
        Court court2 = new Court("B", 1L);
        Court court3 = new Court("C", 1L);
        court3.setDeleted(true);

        em.persist(court1);
        em.persist(court2);
        em.persist(court3);
        em.flush();

        List<Court> courts = courtRepository.findAll();
        assertEquals(2, courts.size());
        assertTrue(courts.stream().noneMatch(Court::isDeleted));
    }

    @Test
    void testFindByIdReturnsNullIfDeleted() {
        Court court = new Court("Hidden Court", 1L);
        em.persist(court);
        em.flush();

        court.setDeleted(true);
        em.merge(court);
        em.flush();

        Court result = courtRepository.findById(court.getId());
        assertNull(result);
    }

    @Test
    void testSoftDeleteMarksCourtAsDeleted() {
        Court court = new Court("SoftDelete Court", 1L);
        em.persist(court);
        em.flush();

        courtRepository.softDelete(court.getId());
        em.flush();

        Court updated = em.find(Court.class, court.getId());
        assertTrue(updated.isDeleted());
    }
}