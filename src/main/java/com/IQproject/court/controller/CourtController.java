package com.IQproject.court.controller;

import com.IQproject.court.model.Court;
import com.IQproject.court.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tennis courts.
 * <p>
 * Provides CRUD operations (Create, Read, Update, Delete) on court entities.
 * All endpoints are prefixed with "/api/courts".
 *
 * @author Vojtech Zednik
 */
@RestController
@RequestMapping("/api/courts")
public class CourtController {
    private final CourtService service;

    /**
     * Constructor for CourtController.
     *
     * @param service the court service handling logic
     */
    public CourtController(CourtService service) {
        this.service = service;
    }

    /**
     * Returns a list of all non-deleted courts.
     *
     * @return list of courts
     */
    @GetMapping
    public List<Court> getAll() {
        return service.getAllCourts();
    }

    /**
     * Returns a specific court by its ID.
     *
     * @param id the ID of the court
     * @return the court if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Court> getById(@PathVariable Long id) {
        Court court = service.getCourt(id);
        if (court == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(court);
    }

    /**
     * Creates a new court.
     *
     * @param court the court to create
     * @return the created court with HTTP 201 Created status
     */
    @PostMapping
    public ResponseEntity<Court> create(@Valid @RequestBody Court court) {
        Court created = service.createCourt(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing court by ID.
     *
     * @param id           the ID of the court to update
     * @param updatedCourt the new data for the court
     * @return the updated court or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Court> update(@PathVariable Long id, @Valid @RequestBody Court updatedCourt) {
        Court court = service.getCourt(id);
        if (court == null) {
            return ResponseEntity.notFound().build();
        }
        Court updated = service.updateCourt(id, updatedCourt);
        return ResponseEntity.ok(updated);
    }

    /**
     * Soft-deletes a court by ID.
     *
     * @param id the ID of the court to delete
     * @return HTTP 204 No Content if deleted successfully, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Court court = service.getCourt(id);
        if (court == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteCourt(id);
        return ResponseEntity.noContent().build();
    }
}