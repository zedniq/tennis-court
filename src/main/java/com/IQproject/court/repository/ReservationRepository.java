package com.IQproject.court.repository;

import com.IQproject.court.model.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing {@link Reservation} entities.
 * Supports basic RUD operations using {@link EntityManager}.
 *
 * @author Vojtech Zednik
 */
@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all non-deleted reservations.
     *
     * @return a list of all active reservations
     */
    public List<Reservation> findAll() {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.deleted = false", Reservation.class)
                .getResultList();
    }

    /**
     * Finds a reservation by its ID.
     *
     * @param id the reservation ID
     * @return the reservation if found, or null otherwise
     */
    public Reservation findById(Long id) {
        Reservation reservation = em.find(Reservation.class, id);
        return (reservation != null && !reservation.isDeleted()) ? reservation : null;
    }

    /**
     * Retrieves all reservations for a specific court.
     *
     * @param courtId the court ID
     * @return list of reservations associated with the court, ordered by creation time
     */
    public List<Reservation> findByCourtId(Long courtId) {
        return em.createQuery("""
                        SELECT r FROM Reservation r
                        WHERE r.deleted = false AND r.courtId = :courtId
                        ORDER BY r.createdAt ASC
                        """, Reservation.class)
                .setParameter("courtId", courtId)
                .getResultList();
    }

    /**
     * Retrieves future reservations for a customer by their phone number.
     *
     * @param phoneNumber the customer's phone number
     * @return list of upcoming reservations, ordered by start time
     */
    public List<Reservation> findFutureByPhoneNumber(String phoneNumber) {
        return em.createQuery("""
                        SELECT r FROM Reservation r
                        WHERE r.deleted = false AND r.customer.phoneNumber = :phone
                        AND r.startTime > :now
                        ORDER BY r.startTime ASC
                        """, Reservation.class)
                .setParameter("phone", phoneNumber)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
    }

    /**
     * Retrieves all reservations for a customer by their phone number.
     *
     * @param phoneNumber the customer's phone number
     * @return list of reservations, ordered by start time
     */
    public List<Reservation> findByPhoneNumber(String phoneNumber) {
        return em.createQuery("""
                        SELECT r FROM Reservation r
                        WHERE r.deleted = false AND r.customer.phoneNumber = :phone
                        ORDER BY r.startTime ASC
                        """, Reservation.class)
                .setParameter("phone", phoneNumber)
                .getResultList();
    }

    /**
     * Saves a reservation. If the reservation is new, it is persisted;
     * otherwise, it is updated.
     *
     * @param reservation the reservation to save
     * @return the persisted or updated reservation
     */
    @Transactional
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        }
        return em.merge(reservation);
    }

    /**
     * Soft-deletes a reservation by setting its 'delete' flag to true.
     *
     * @param id the ID of the reservation to soft-delete
     */
    @Transactional
    public void softDelete(Long id) {
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation != null) {
            reservation.setDeleted(true);
            em.merge(reservation);
        }
    }

    /**
     * Checks whether there is any overlapping reservation on a given court within the specified time range.
     *
     * @param courtId the court ID
     * @param start   the desired start time
     * @param end     the desired end time
     * @return true if there is an overlap, false otherwise
     */
    public boolean isOverlapping(Long courtId, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("""
                        SELECT count(r) FROM Reservation r
                        WHERE r.deleted = false AND r.courtId = :courtId
                        AND ((r.startTime < :end) AND (r.endTime > :start))
                        """, Long.class)
                .setParameter("courtId", courtId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult() > 0;
    }
}
