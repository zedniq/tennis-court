package com.IQproject.court.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents a surface type in the system.
 *
 * @author Vojtech Zednik
 */
@Entity
public class SurfaceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "pricePerMinute is required")
    private BigDecimal pricePerMinute;
    private boolean deleted = false;

    /**
     * Default no-argument constructor required by JPA.
     */
    public SurfaceType() {
    }

    /**
     * Constructs a new SurfaceType with the specified name and price per minute.
     *
     * @param name           the name of the surface type
     * @param pricePerMinute the cost of using a court per minute
     */
    public SurfaceType(String name, BigDecimal pricePerMinute) {
        this.name = name;
        this.pricePerMinute = pricePerMinute;
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

    public BigDecimal getPricePerMinute() {
        return pricePerMinute;
    }

    public void setPricePerMinute(BigDecimal pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}