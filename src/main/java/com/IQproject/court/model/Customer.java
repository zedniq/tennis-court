package com.IQproject.court.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Represents a customer in the system.
 *
 * @author Vojtech Zednik
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "phoneNumber"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Name is required")
    private String phoneNumber;
    private boolean deleted = false;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Customer() {

    }

    /**
     * Constructs a new Customer with the specified phone number and name.
     *

     * @param phoneNumber the phone number associated with the customer
     * @param name        the name of the customer
     */
    public Customer(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}