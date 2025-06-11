package com.IQproject.court.controller;

import com.IQproject.court.model.Reservation;
import com.IQproject.court.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing reservations in the tennis club.
 * Provides CRUD operations (Create, Read, Update, Delete) on reservations.
 * All endpoints are prefixed with "/api/reservations".
 *
 * @author Vojtech Zednik
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    /**
     * Constructor for ReservationController.
     *
     * @param service the reservation service handling logic
     */
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Retrieves all reservations.
     *
     * @return list of all reservations
     */
    @GetMapping
    public List<Reservation> getAll() {
        return service.getAllReservations();
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param id the ID of the reservation
     * @return the reservation if found and not deleted, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        Reservation reservation = service.getReservationById(id);
        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservation);
    }

    /**
     * Creates a new reservation.
     *
     * @param reservation the reservation to create
     * @return reservation price, or 400 Bad Request if invalid
     */
    @PostMapping
    public ResponseEntity<BigDecimal> create(@Valid @RequestBody Reservation reservation) {
        Reservation created = service.createReservation(reservation);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(created.getPrice());
    }

    /**
     * Updates an existing reservation.
     *
     * @param id      the ID of the reservation to update
     * @param updated the updated reservation data
     * @return updated reservation, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @Valid @RequestBody Reservation updated) {
        Reservation saved = service.updateReservation(id, updated);
        if (saved == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a reservation by ID.
     *
     * @param id the ID of the reservation
     * @return 204 No Content if deleted, or 404 Not Found if not found or already deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Reservation toDelete = service.getReservationById(id);
        if (toDelete == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all reservations for a specific court.
     *
     * @param courtId the ID of the court
     * @return list of reservations for the specific court
     */
    @GetMapping("/court/{courtId}")
    public List<Reservation> getByCourt(@PathVariable Long courtId) {
        return service.getReservationsByCourtId(courtId);
    }

    /**
     * Retrieves reservations by customer phone number.
     * /customer?phone=phoneNumber&futureOnly=bool
     *
     * @param phone      the phone number of the customer
     * @param futureOnly if true, only future reservations will be returned
     * @return list of reservations for the customer
     */
    @GetMapping("/customer")
    public List<Reservation> getByPhone(@RequestParam String phone,
                                        @RequestParam(required = false, defaultValue = "false") boolean futureOnly) {
        return service.getReservationsByPhone(phone, futureOnly);
    }
}