package repository;

import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.SurfaceTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.IQproject.court.CourtApplication.class)
@Transactional
public class SurfaceTypeRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @BeforeEach
    void setUp() {
        em.createQuery("DELETE FROM SurfaceType").executeUpdate();
    }

    @Test
    void testSaveNewSurfaceType() {
        String SurfaceName = "Clay";
        BigDecimal bigNumber = new BigDecimal(456456);

        SurfaceType surface = new SurfaceType(SurfaceName, bigNumber);
        SurfaceType saved = surfaceTypeRepository.save(surface);
        assertNotNull(saved);
        assertNotNull(saved.getId());

        SurfaceType found = em.find(SurfaceType.class, saved.getId());
        assertNotNull(found);
        assertEquals(SurfaceName, found.getName());
        assertEquals(bigNumber, found.getPricePerMinute());
    }

    @Test
    void testFindAllReturnsOnlyNotDeleted() {
        SurfaceType s1 = new SurfaceType("Grass", new BigDecimal(2));
        SurfaceType s2 = new SurfaceType("Hard", new BigDecimal(2));
        SurfaceType s3 = new SurfaceType("Carpet", new BigDecimal(2));
        s3.setDeleted(true);

        em.persist(s1);
        em.persist(s2);
        em.persist(s3);
        em.flush();

        List<SurfaceType> all = surfaceTypeRepository.findAll();
        assertNotNull(all);
        assertEquals(2, all.size());
        assertTrue(all.stream().noneMatch(SurfaceType::isDeleted));
    }

    @Test
    void testFindByIdReturnsNullIfDeleted() {
        SurfaceType surface = new SurfaceType("Clay", new BigDecimal(2));
        em.persist(surface);
        em.flush();

        surface.setDeleted(true);
        em.merge(surface);
        em.flush();

        SurfaceType result = surfaceTypeRepository.findById(surface.getId());
        assertNull(result);
    }

    @Test
    void testSoftDeleteMarksSurfaceTypeAsDeleted() {
        SurfaceType surface = new SurfaceType("DeleteMe", new BigDecimal(2));
        em.persist(surface);
        em.flush();

        surfaceTypeRepository.softDelete(surface.getId());
        em.flush();

        SurfaceType updated = em.find(SurfaceType.class, surface.getId());
        assertTrue(updated.isDeleted());
    }
}