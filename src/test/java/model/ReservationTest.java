package model;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.Customer;
import com.IQproject.court.model.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    @Test
    void testSettersAndGetters() {
        Reservation reservation = new Reservation();

        Long id = 42L;
        Court court = new Court("Test Court", 1L);
        Customer customer = new Customer("123456789", "Karel");
        LocalDateTime start = LocalDateTime.of(2025, 6, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 10, 10, 0);
        boolean doubles = true;
        BigDecimal price = new BigDecimal("25.50");
        boolean deleted = true;

        reservation.setId(id);
        reservation.setCourtId(court.getId());
        reservation.setCustomer(customer);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setDoubles(doubles);
        reservation.setPrice(price);
        reservation.setDeleted(deleted);

        assertEquals(id, reservation.getId());
        assertEquals(court.getId(), reservation.getCourtId());
        assertEquals(customer, reservation.getCustomer());
        assertEquals(start, reservation.getStartTime());
        assertEquals(end, reservation.getEndTime());
        assertTrue(reservation.isDoubles());
        assertEquals(price, reservation.getPrice());
        assertTrue(reservation.isDeleted());
    }
}
