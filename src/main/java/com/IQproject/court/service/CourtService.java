package com.IQproject.court.service;

import com.IQproject.court.model.Court;
import com.IQproject.court.repository.CourtRepository;
import com.IQproject.court.repository.SurfaceTypeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link Court} entities.
 * Handles logic related to creating, retrieving, updating, and deleting courts.
 *
 * @author Vojtech Zednik
 */
@Service
public class CourtService {
    private final CourtRepository courtRepository;
    private final SurfaceTypeRepository surfaceRepository;

    /**
     * Constructs a new CourtService with the given repositories.
     *
     * @param repository            the court repository
     * @param surfaceTypeRepository the surface type repository
     */
    public CourtService(CourtRepository repository, SurfaceTypeRepository surfaceTypeRepository) {
        this.courtRepository = repository;
        this.surfaceRepository = surfaceTypeRepository;
    }

    /**
     * Retrieves all courts.
     *
     * @return a list of all available courts
     */
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    /**
     * Retrieves a court by its ID.
     *
     * @param id the ID of the court to retrieve
     * @return the Court with the specified ID, or null if not found or deleted
     */
    public Court getCourt(Long id) {
        return courtRepository.findById(id);
    }

    /**
     * Creates a new court.
     * Validates that the specified surface type exists.
     *
     * @param court the court to create
     * @return the created Court
     * @throws IllegalArgumentException if the specified surface type does not exist
     */
    public Court createCourt(Court court) {
        if (surfaceRepository.findById(court.getSurfaceTypeId()) == null) {
            throw new IllegalArgumentException("SurfaceType with ID " + court.getSurfaceTypeId() + " does not exist.");
        }
        return courtRepository.save(court);
    }

    /**
     * Updates an existing court.
     *
     * @param id           the ID of the court to update
     * @param updatedCourt the new court data
     * @return the updated Court
     * @throws IllegalArgumentException if the court with the specified ID does not exist
     */
    public Court updateCourt(Long id, Court updatedCourt) {
        Court existing = getCourt(id);
        if (existing == null) {
            throw new IllegalArgumentException("Court does not exist");
        }
        existing.setName(updatedCourt.getName());
        existing.setSurfaceTypeId(updatedCourt.getSurfaceTypeId());

        return courtRepository.save(existing);
    }

    /**
     * Soft-deletes a court by marking it as deleted.
     *
     * @param id the ID of the court to delete
     */
    public void deleteCourt(Long id) {
        courtRepository.softDelete(id);
    }
}