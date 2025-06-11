package com.IQproject.court.repository;

import com.IQproject.court.model.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing {@link SurfaceType} entities.
 *
 * @author Vojtech Zednik
 */
@Repository
public class SurfaceTypeRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all surface types that are not marked as deleted.
     *
     * @return list of active surface types
     */
    public List<SurfaceType> findAll() {
        return em.createQuery("SELECT s FROM SurfaceType s WHERE s.deleted = false", SurfaceType.class)
                .getResultList();
    }

    /**
     * Finds a surface type by ID, returning only if it's not deleted.
     *
     * @param id the surface type ID
     * @return the surface type if found and not deleted, null otherwise
     */
    public SurfaceType findById(Long id) {
        SurfaceType surfaceType = em.find(SurfaceType.class, id);
        return (surfaceType != null && !surfaceType.isDeleted()) ? surfaceType : null;
    }

    /**
     * Saves a surface type. If the entity is new, it is persisted;
     * otherwise, it is updated.
     *
     * @param surfaceType the surface type entity to save
     * @return the persisted or updated surface type
     */
    @Transactional
    public SurfaceType save(SurfaceType surfaceType) {
        if (surfaceType.getId() == null) {
            em.persist(surfaceType);
            return surfaceType;
        }
        return em.merge(surfaceType);
    }

    /**
     * Soft-deletes a surface type by setting the 'deleted' flag to true,
     * only if it exists and is not already deleted.
     *
     * @param id the ID of the surface type to soft-delete
     */
    @Transactional
    public void softDelete(Long id) {
        SurfaceType surfaceType = em.find(SurfaceType.class, id);
        if (surfaceType != null && !surfaceType.isDeleted()) {
            surfaceType.setDeleted(true);
            em.merge(surfaceType);
        }
    }
}