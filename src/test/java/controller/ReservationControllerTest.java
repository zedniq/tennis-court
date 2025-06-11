package controller;

import com.IQproject.court.controller.ReservationController;
import com.IQproject.court.model.Reservation;
import com.IQproject.court.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService service;

    @InjectMocks
    private ReservationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Reservation createReservation(Long id, boolean deleted) {
        Reservation r = new Reservation();
        r.setId(id);
        r.setDeleted(deleted);
        r.setPrice(BigDecimal.valueOf(42));
        r.setStartTime(LocalDateTime.now().plusHours(1));
        r.setEndTime(LocalDateTime.now().plusHours(2));
        return r;
    }

    @Test
    void getAllReturnsList() {
        List<Reservation> list = List.of(createReservation(1L, false));
        when(service.getAllReservations()).thenReturn(list);

        List<Reservation> result = controller.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getByIdReturnsReservationIfExistsAndNotDeleted() {
        Reservation r = createReservation(1L, false);
        when(service.getReservationById(1L)).thenReturn(r);

        ResponseEntity<Reservation> response = controller.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(r, response.getBody());
    }

    @Test
    void getByIdReturns404IfMissing() {
        long id = 95L;

        when(service.getReservationById(id)).thenReturn(null);

        ResponseEntity<Reservation> response = controller.getById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createReturnsCreatedPrice() {
        Reservation input = createReservation(null, false);
        Reservation saved = createReservation(5L, false);

        when(service.createReservation(input)).thenReturn(saved);

        ResponseEntity<BigDecimal> response = controller.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved.getPrice(), response.getBody());
    }

    @Test
    void createReturnsBadRequestOnFailure() {
        Reservation input = createReservation(null, false);
        when(service.createReservation(input)).thenReturn(null);

        ResponseEntity<BigDecimal> response = controller.create(input);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateReturnsUpdatedReservation() {
        long id = 7L;

        Reservation updated = createReservation(id, false);
        when(service.updateReservation(eq(id), any())).thenReturn(updated);

        ResponseEntity<Reservation> response = controller.update(id, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void updateReturnsNotFoundIfMissing() {
        long id = 3L;

        when(service.updateReservation(eq(id), any())).thenReturn(null);

        ResponseEntity<Reservation> response = controller.update(id, new Reservation());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteReturnsNoContentIfFoundAndNotDeleted() {
        long id = 9L;

        Reservation r = createReservation(id, false);
        when(service.getReservationById(id)).thenReturn(r);

        ResponseEntity<Void> response = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteReservation(id);
    }

    @Test
    void deleteReturnsNotFoundIfMissing() {
        long notExistingId = 1000L;

        when(service.getReservationById(notExistingId)).thenReturn(null);

        ResponseEntity<Void> response = controller.delete(notExistingId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getByCourtReturnsList() {
        long id = 7L;

        List<Reservation> list = List.of(createReservation(1L, false));
        when(service.getReservationsByCourtId(id)).thenReturn(list);

        List<Reservation> result = controller.getByCourt(id);

        assertEquals(1, result.size());
    }

    @Test
    void getByPhoneReturnsReservations() {
        String phoneNumber = "123456789";

        List<Reservation> reservations = List.of(createReservation(1L, false));
        when(service.getReservationsByPhone(phoneNumber, true)).thenReturn(reservations);

        List<Reservation> result = controller.getByPhone(phoneNumber, true);

        assertEquals(1, result.size());
        verify(service).getReservationsByPhone(phoneNumber, true);
    }
}
