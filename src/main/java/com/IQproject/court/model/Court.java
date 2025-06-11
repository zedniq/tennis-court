package com.IQproject.court.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a tennis court in the system.
 *
 * @author Vojtech Zednik
 */
@Entity
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "SurfaceTypeId is required")
    @JoinColumn(name = "surface_type_id")
    private Long surfaceTypeId;
    private boolean deleted = false;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Court() {
    }

    /**
     * Constructs a new Court with the specified name and surface type.
     *
     * @param name        the name of the court
     * @param surfaceTypeId the ID of the surface type associated with the court
     */
    public Court(String name, long surfaceTypeId) {
        this.name = name;
        this.surfaceTypeId = surfaceTypeId;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSurfaceTypeId() {
        return surfaceTypeId;
    }

    public void setSurfaceTypeId(long surfaceTypeId) {
        this.surfaceTypeId = surfaceTypeId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
