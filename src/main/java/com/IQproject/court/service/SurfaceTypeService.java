package com.IQproject.court.service;

import com.IQproject.court.model.Court;
import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.repository.SurfaceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing {@link SurfaceType} entities.
 * Provides business logic for retrieving, creating, and deleting surface types.
 */
@Service
public class SurfaceTypeService {
    private final SurfaceTypeRepository surfaceTypeRepository;

    /**
     * Constructs a new SurfaceTypeService with the specified repository.
     *
     * @param surfaceTypeRepository the repository for accessing surface type data
     */
    public SurfaceTypeService(SurfaceTypeRepository surfaceTypeRepository) {
        this.surfaceTypeRepository = surfaceTypeRepository;
    }

    /**
     * Retrieves all surface types that are not soft-deleted.
     *
     * @return a list of all surface types
     */
    public List<SurfaceType> getAllSurfaceTypes() {
        return surfaceTypeRepository.findAll();
    }

    /**
     * Retrieves a surface type by its ID.
     *
     * @param id the ID of the surface type
     * @return the found surface type
     * @throws IllegalArgumentException if the surface type does not exist
     */
    public SurfaceType getSurfaceTypeById(Long id) {
        SurfaceType toFind = surfaceTypeRepository.findById(id);
        if (toFind == null) {
            throw new IllegalArgumentException("SurfaceType does not exist");
        }
        return toFind;
    }

    /**
     * Creates a new surface type.
     *
     * @param surfaceType the surface type to create
     * @return the created surface type
     */
    public SurfaceType createSurfaceType(SurfaceType surfaceType) {
        return surfaceTypeRepository.save(surfaceType);
    }

    /**
     * Soft-deletes the surface type with the specified ID.
     *
     * @param id the ID of the surface type to delete
     */
    public void deleteSurfaceType(Long id) {
        surfaceTypeRepository.softDelete(id);
    }
}