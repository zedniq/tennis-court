package service;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.Customer;
import com.IQproject.court.model.Reservation;
import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.CourtRepository;
import com.IQproject.court.repository.CustomerRepository;
import com.IQproject.court.repository.ReservationRepository;
import com.IQproject.court.repository.SurfaceTypeRepository;
import com.IQproject.court.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private ReservationRepository reservationRepo;
    private CourtRepository courtRepo;
    private CustomerRepository customerRepo;
    private SurfaceTypeRepository surfaceRepo;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepo = mock(ReservationRepository.class);
        courtRepo = mock(CourtRepository.class);
        customerRepo = mock(CustomerRepository.class);
        surfaceRepo = mock(SurfaceTypeRepository.class);

        reservationService = new ReservationService(
                reservationRepo, courtRepo, customerRepo, surfaceRepo
        );
    }

    @Test
    void createShouldCreateNewCustomerAndSaveReservation() {
        long courtId = 1L;
        long surfaceId = 10L;
        String phoneNumber = "773615894";
        BigDecimal pricePerMin = new BigDecimal(15);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Court court = new Court();
        court.setId(courtId);
        court.setSurfaceTypeId(surfaceId);

        SurfaceType surface = new SurfaceType();
        surface.setId(surfaceId);
        surface.setPricePerMinute(pricePerMin);

        Customer newCustomer = new Customer(phoneNumber, "John");

        Reservation reservation = new Reservation();
        reservation.setCourtId(court.getId());
        reservation.setCustomer(newCustomer);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setDoubles(false);

        when(courtRepo.findById(courtId)).thenReturn(court);
        when(reservationRepo.isOverlapping(courtId, start, end)).thenReturn(false);
        when(customerRepo.findByPhoneNumber(phoneNumber)).thenReturn(null);
        when(surfaceRepo.findById(surfaceId)).thenReturn(surface);
        when(reservationRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation saved = reservationService.createReservation(reservation);

        verify(customerRepo).save(any(Customer.class));
        verify(reservationRepo).save(any(Reservation.class));
        assertEquals(BigDecimal.valueOf(60L * pricePerMin.intValue()), saved.getPrice());
    }

    @Test
    void createShouldThrowWhenCourtDoesNotExist() {
        long notExistinqId = 56L;

        Reservation reservation = new Reservation();
        reservation.setCourtId(1L);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(1));

        when(courtRepo.findById(notExistinqId)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(reservation);
        });

        assertEquals("Court does not exist", ex.getMessage());
    }

    @Test
    void createShouldThrowWhenTimeInvalid() {
        Reservation reservation = new Reservation();
        reservation.setCourtId(1L);
        reservation.setStartTime(LocalDateTime.now().plusHours(2));
        reservation.setEndTime(LocalDateTime.now());

        when(courtRepo.findById(1L)).thenReturn(new Court());

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(reservation);
        });
    }


    @Test
    void createShouldThrowWhenTimeOverlaps() {
        Reservation reservation = new Reservation();
        reservation.setCourtId(1L);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(1));

        when(courtRepo.findById(1L)).thenReturn(new Court());
        when(reservationRepo.isOverlapping(anyLong(), any(), any())).thenReturn(true);

        assertThrows(Exception.class, () -> {
            reservationService.createReservation(reservation);
        });
    }

    @Test
    void updateShouldThrowWhenReservationDoesNotExist() {
        when(reservationRepo.findById(1L)).thenReturn(null);

        Reservation updated = new Reservation();
        updated.setCourtId(1L);
        updated.setCustomer(new Customer("123", "Alice"));
        updated.setStartTime(LocalDateTime.now());
        updated.setEndTime(LocalDateTime.now().plusHours(1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservation(1L, updated));

        assertEquals("Reservation does not exist", ex.getMessage());
    }

    @Test
    void updateShouldThrowWhenStartTimeAfterEndTime() {
        long reservationId = 1L;

        Reservation existing = new Reservation();
        existing.setId(reservationId);

        Reservation updated = new Reservation();
        updated.setCourtId(1L);
        updated.setCustomer(new Customer("123", "Alice"));
        updated.setStartTime(LocalDateTime.now().plusHours(2));
        updated.setEndTime(LocalDateTime.now());

        when(reservationRepo.findById(reservationId)).thenReturn(existing);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservation(reservationId, updated));

        assertEquals("StartTime is after endTime", ex.getMessage());
    }

    @Test
    void updateShouldThrowWhenTimeOverlaps() {
        long reservationId = 1L;

        Reservation existing = new Reservation();
        existing.setId(reservationId);

        Reservation updated = new Reservation();
        updated.setCourtId(1L);
        updated.setCustomer(new Customer("123", "Alice"));
        updated.setStartTime(LocalDateTime.now());
        updated.setEndTime(LocalDateTime.now().plusHours(1));

        when(reservationRepo.findById(reservationId)).thenReturn(existing);
        when(reservationRepo.isOverlapping(eq(reservationId), any(), any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservation(reservationId, updated));
    }

    @Test
    void updateShouldThrowWhenCourtDoesNotExist() {
        long reservationId = 1L;
        Reservation existing = new Reservation();
        existing.setId(reservationId);

        Court updatedCourt = new Court("Nonexistent", 1L);

        Reservation updated = new Reservation();
        updated.setCourtId(updatedCourt.getId());
        updated.setCustomer(new Customer("123", "Alice"));
        updated.setStartTime(LocalDateTime.now());
        updated.setEndTime(LocalDateTime.now().plusHours(1));

        when(reservationRepo.findById(reservationId)).thenReturn(existing);
        when(reservationRepo.isOverlapping(anyLong(), any(), any())).thenReturn(false);
        when(courtRepo.findById(1L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservation(1L, updated));

        assertEquals("Court does not exist", ex.getMessage());
    }

    @Test
    void updateShouldThrowWhenCustomerDoesNotExist() {
        long reservationId = 34L;

        Reservation existing = new Reservation();
        existing.setId(reservationId);

        Court court = new Court("Clay", 1L);
        Customer customer = new Customer("123", "Alice");

        Reservation updated = new Reservation();
        updated.setCourtId(court.getId());
        updated.setCustomer(customer);
        updated.setStartTime(LocalDateTime.now());
        updated.setEndTime(LocalDateTime.now().plusHours(1));

        when(reservationRepo.findById(reservationId)).thenReturn(existing);
        when(reservationRepo.isOverlapping(anyLong(), any(), any())).thenReturn(false);
        when(courtRepo.findById(1L)).thenReturn(court);
        when(customerRepo.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservation(1L, updated));
    }
}