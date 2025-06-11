package com.IQproject.court.repository;

import com.IQproject.court.model.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for accessing and managing {@link Court} entities.
 * Implements basic CRUD operations using {@link EntityManager}.
 *
 * @author Vojtech Zednik
 */
@Repository
public class CourtRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all non-deleted courts from the database.
     *
     * @return list of all active courts
     */
    public List<Court> findAll() {
        return em.createQuery("SELECT c FROM Court c WHERE c.deleted = false", Court.class)
                .getResultList();
    }

    /**
     * Finds a court by its ID.
     *
     * @param id the ID of the court
     * @return the court entity, or null if not found or deleted
     */
    public Court findById(Long id) {
        Court court = em.find(Court.class, id);
        return (court != null && !court.isDeleted()) ? court : null;
    }

    /**
     * Saves a new or existing court.
     * Persists the court if it has no ID, otherwise merges it.
     *
     * @param court the court to save
     * @return the managed court entity
     */
    @Transactional
    public Court save(Court court) {
        if (court.getId() == null) {
            em.persist(court);
            return court;
        }
        return em.merge(court);
    }

    /**
     * Soft-deletes a court by setting its flag 'delete' to true.
     *
     * @param id the ID of the court to soft-delete
     */
    @Transactional
    public void softDelete(Long id) {
        Court court = em.find(Court.class, id);
        if (court != null) {
            court.setDeleted(true);
            em.merge(court);
        }
    }
}
