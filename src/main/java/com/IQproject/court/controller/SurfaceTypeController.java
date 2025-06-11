package com.IQproject.court.controller;

import com.IQproject.court.model.SurfaceType;
import com.IQproject.court.service.SurfaceTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tennis court surface types.
 *
 * <p>
 * Provides CRD operations (Create, Read, Delete) on surface type entities.
 * All endpoints are prefixed with "/api/surface-types".
 *
 * @author Vojtech Zednik
 */
@RestController
@RequestMapping("/api/surface-types")
public class SurfaceTypeController {
    private final SurfaceTypeService service;

    /**
     * Constructor for SurfaceTypeController.
     *
     * @param service the surface type service handling logic
     */
    public SurfaceTypeController(SurfaceTypeService service) {
        this.service = service;
    }

    /**
     * Returns a list of all non-deleted surface types.
     *
     * @return a list of surface types
     */
    @GetMapping
    public List<SurfaceType> getAll() {
        return service.getAllSurfaceTypes();
    }

    /**
     * Retrieves a surface type by its ID.
     *
     * @param id the ID of the surface type to retrieve
     * @return HTTP 200 with the surface type if found and not deleted; otherwise HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurfaceType> getById(@PathVariable Long id) {
        SurfaceType toFind = service.getSurfaceTypeById(id);
        if (toFind == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toFind);
    }

    /**
     * Creates a new surface type.
     *
     * @param surfaceType the surface type to create
     * @return HTTP 201 with the created surface type
     */
    @PostMapping
    public ResponseEntity<SurfaceType> create(@Valid @RequestBody SurfaceType surfaceType) {
        SurfaceType toCreate = service.createSurfaceType(surfaceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCreate);
    }

    /**
     * Soft-deletes a surface type by ID.
     *
     * @param id the ID of the surface type to delete
     * @return HTTP 204 if deleted, or HTTP 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SurfaceType toDelete = service.getSurfaceTypeById(id);
        if (toDelete == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteSurfaceType(id);
        return ResponseEntity.noContent().build();
    }
}