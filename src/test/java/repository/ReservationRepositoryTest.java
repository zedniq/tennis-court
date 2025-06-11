package repository;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.Customer;
import com.IQproject.court.model.Reservation;
import com.IQproject.court.repository.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.IQproject.court.CourtApplication.class)
@Transactional
public class ReservationRepositoryTest {
    private final static String COURT_NAME = "Test Court";
    private final static String PHONE_NUMBER = "123456789";
    private final static String USER_NAME = "Hubert";

    @Autowired
    private EntityManager em;

    @Autowired
    private ReservationRepository reservationRepository;

    private final Court court = new Court(COURT_NAME, 1L);
    private final Customer customer = new Customer(PHONE_NUMBER, USER_NAME);

    @BeforeEach
    void setUp() {
        em.createQuery("DELETE FROM Reservation").executeUpdate();
        em.createQuery("DELETE FROM Court").executeUpdate();
        em.createQuery("DELETE FROM Customer").executeUpdate();

        em.persist(court);
        em.persist(customer);

        em.flush();
    }

    @Test
    void testSaveNewReservation() {
        Reservation res = new Reservation();
        res.setCourtId(court.getId());
        res.setCustomer(customer);
        res.setStartTime(LocalDateTime.now().plusHours(1));
        res.setEndTime(LocalDateTime.now().plusHours(2));


        Reservation saved = reservationRepository.save(res);
        assertNotNull(saved);
        assertNotNull(saved.getId());

        Reservation found = em.find(Reservation.class, saved.getId());
        assertEquals(PHONE_NUMBER, found.getCustomer().getPhoneNumber());
    }

    @Test
    void testFindAllOnlyNotDeleted() {
        Reservation r1 = new Reservation();
        r1.setCourtId(court.getId());
        r1.setCustomer(customer);
        r1.setStartTime(LocalDateTime.now());
        r1.setEndTime(LocalDateTime.now().plusHours(1));

        Reservation r2 = new Reservation();
        r2.setCourtId(court.getId());
        r2.setCustomer(customer);
        r2.setStartTime(LocalDateTime.now().plusDays(1));
        r2.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        r2.setDeleted(true);

        em.persist(r1);
        em.persist(r2);
        em.flush();

        List<Reservation> all = reservationRepository.findAll();
        assertEquals(1, all.size());
        assertFalse(all.get(0).isDeleted());
    }

    @Test
    void testFindByCourtId() {
        Reservation res = new Reservation();
        res.setCourtId(court.getId());
        res.setCustomer(customer);
        res.setStartTime(LocalDateTime.now().plusDays(1));
        res.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        em.persist(res);
        em.flush();

        List<Reservation> found = reservationRepository.findByCourtId(court.getId());
        assertNotNull(found);
        assertEquals(1, found.size());
    }

    @Test
    void testFindById() {
        long notExistingReservationId = 998L;

        Reservation res = new Reservation();
        res.setCourtId(court.getId());
        res.setCustomer(customer);
        res.setStartTime(LocalDateTime.now().plusDays(1));
        res.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        em.persist(res);
        em.flush();

        Reservation found = reservationRepository.findById(res.getId());
        assertNotNull(found);
        assertNotNull(found.getCustomer());
        assertEquals(USER_NAME, found.getCustomer().getName());

        found = reservationRepository.findById(notExistingReservationId);
        assertNull(found);
    }

    @Test
    void testFindByPhoneNumber() {
        Reservation past = new Reservation();
        past.setCourtId(court.getId());
        past.setCustomer(customer);
        past.setStartTime(LocalDateTime.now().minusHours(2));
        past.setEndTime(LocalDateTime.now().minusHours(1));

        Reservation future = new Reservation();
        future.setCourtId(court.getId());
        future.setCustomer(customer);
        future.setStartTime(LocalDateTime.now().plusHours(1));
        future.setEndTime(LocalDateTime.now().plusHours(2));

        em.persist(past);
        em.persist(future);
        em.flush();

        List<Reservation> results = reservationRepository.findFutureByPhoneNumber(customer.getPhoneNumber());
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(LocalDateTime.now().isBefore(results.get(0).getStartTime()));

        results = reservationRepository.findByPhoneNumber(customer.getPhoneNumber());
        assertEquals(2, results.size());
        assertTrue(LocalDateTime.now().isAfter(results.get(0).getStartTime()));
    }

    @Test
    void testIsOverlapping() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(1);

        Reservation existing = new Reservation();
        existing.setCourtId(court.getId());
        existing.setCustomer(customer);
        existing.setStartTime(start);
        existing.setEndTime(end);

        em.persist(existing);
        em.flush();

        // overlapping
        boolean overlaps = reservationRepository.isOverlapping(court.getId(),
                start.minusMinutes(30), end.plusMinutes(30));
        assertTrue(overlaps);

        // non-overlapping
        boolean noOverlap = reservationRepository.isOverlapping(court.getId(),
                end.plusHours(1), end.plusHours(2));
        assertFalse(noOverlap);
    }

    @Test
    void testSoftDelete() {
        Reservation r = new Reservation();
        r.setCourtId(court.getId());
        r.setCustomer(customer);
        r.setStartTime(LocalDateTime.now().plusHours(1));
        r.setEndTime(LocalDateTime.now().plusHours(2));

        em.persist(r);
        em.flush();

        reservationRepository.softDelete(r.getId());
        em.flush();

        Reservation updated = em.find(Reservation.class, r.getId());
        assertTrue(updated.isDeleted());
    }
}