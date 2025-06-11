package com.IQproject.court.service;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.Customer;
import com.IQproject.court.model.Reservation;
import com.IQproject.court.repository.CourtRepository;
import com.IQproject.court.repository.CustomerRepository;
import com.IQproject.court.repository.ReservationRepository;
import com.IQproject.court.repository.SurfaceTypeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * Service class for managing court {@link Reservation}.
 * Handles business logic related to creating, updating, retrieving, and deleting reservations.
 *
 * @author Vojtech Zednik
 */
@Service
public class ReservationService {
    private static final double DOUBLES_PRICE_MULTIPLIER = 1.5;

    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;
    private final CustomerRepository customerRepository;
    private final SurfaceTypeRepository surfaceTypeRepository;

    /**
     * Constructs a new ReservationService with the required repositories.
     *
     * @param reservationRepository the reservation repository
     * @param courtRepository       the court repository
     * @param customerRepository    the customer repository
     * @param surfaceTypeRepository the surface type repository
     */
    public ReservationService(
            ReservationRepository reservationRepository,
            CourtRepository courtRepository,
            CustomerRepository customerRepository,
            SurfaceTypeRepository surfaceTypeRepository) {
        this.reservationRepository = reservationRepository;
        this.courtRepository = courtRepository;
        this.customerRepository = customerRepository;
        this.surfaceTypeRepository = surfaceTypeRepository;
    }

    /**
     * Retrieves all reservations that are not deleted.
     *
     * @return a list of all reservations
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the ID of the reservation
     * @return the reservation with the given ID, or null if not found or deleted
     */
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    /**
     * Retrieves all reservations for a specific court.
     *
     * @param courtId the ID of the court
     * @return a list of reservations for the court
     */
    public List<Reservation> getReservationsByCourtId(Long courtId) {
        return reservationRepository.findByCourtId(courtId);
    }

    /**
     * Retrieves reservations by phone number.
     *
     * @param phone      the customer's phone number
     * @param futureOnly if true, returns only future reservations
     * @return a list of reservations for the customer
     */
    public List<Reservation> getReservationsByPhone(String phone, boolean futureOnly) {
        if (futureOnly) {
            return reservationRepository.findFutureByPhoneNumber(phone);
        }
        return reservationRepository.findByPhoneNumber(phone);
    }

    /**
     * Creates a new reservation. Validates the court, customer, time validity, and overlap.
     *
     * @param reservation the reservation to create
     * @return the created reservation
     * @throws IllegalArgumentException if any validation fails
     */
    public Reservation createReservation(Reservation reservation) {
        Court court = courtRepository.findById(reservation.getCourtId());
        if (court == null) {
            throw new IllegalArgumentException("Court does not exist");
        }

        if (reservation.getStartTime().isAfter(reservation.getEndTime())) {
            throw new IllegalArgumentException("StartTime is after endTime");
        }

        if (reservationRepository.isOverlapping(court.getId(),
                reservation.getStartTime(), reservation.getEndTime())) {
            throw new IllegalArgumentException("Reservation time is overlapping with another reservation");
        }

        Customer customer = customerRepository.findByPhoneNumber(reservation.getCustomer().getPhoneNumber());
        if (customer == null) {
            customer = new Customer(reservation.getCustomer().getPhoneNumber(),
                    reservation.getCustomer().getName());
            customerRepository.save(customer);
        }

        reservation.setCourtId(court.getId());
        reservation.setCustomer(customer);
        reservation.setPrice(calculatePrice(reservation));

        return reservationRepository.save(reservation);
    }

    /**
     * Updates an existing reservation with new data. Validates time, overlap, court, and customer.
     *
     * @param id      the ID of the reservation to update
     * @param updated the updated reservation data
     * @return the updated reservation
     * @throws IllegalArgumentException if the reservation, court, or customer is invalid
     */
    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = reservationRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Reservation does not exist");
        }

        if (updated.getStartTime().isAfter(updated.getEndTime())) {
            throw new IllegalArgumentException("StartTime is after endTime");
        }

        if (reservationRepository.isOverlapping(updated.getCourtId(),
                updated.getStartTime(), updated.getEndTime())) {
            throw new IllegalArgumentException("Reservation time is overlapping with another reservation");
        }

        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());
        existing.setDoubles(updated.isDoubles());

        Court court = courtRepository.findById(updated.getCourtId());
        if (court == null) {
            throw new IllegalArgumentException("Court does not exist");
        }

        Customer customer = customerRepository.findById(updated.getCustomer().getId());
        if (customer == null) {
            throw new IllegalArgumentException("Customer does not exist");
        }

        existing.setCourtId(court.getId());
        existing.setCustomer(customer);
        existing.setPrice(calculatePrice(existing));

        return reservationRepository.save(existing);
    }

    /**
     * Soft-deletes a reservation by marking it as deleted.
     *
     * @param id the ID of the reservation to delete
     */
    public void deleteReservation(Long id) {
        reservationRepository.softDelete(id);
    }

    /**
     * Calculates the price of a reservation based on duration and surface type price.
     * Applies a multiplier if the reservation is for doubles.
     *
     * @param reservation the reservation for which to calculate the price
     * @return the calculated price
     */
    private BigDecimal calculatePrice(Reservation reservation) {
        long minutes = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toMinutes();
        BigDecimal basePrice = surfaceTypeRepository.findById(courtRepository.findById(reservation.getCourtId()).getSurfaceTypeId())
                .getPricePerMinute().multiply(BigDecimal.valueOf(minutes));

        if (reservation.isDoubles()) {
            return basePrice.multiply(BigDecimal.valueOf(DOUBLES_PRICE_MULTIPLIER));
        }
        return basePrice;
    }
}