package com.IQproject.court.repository;

import com.IQproject.court.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for managing {@link Customer} entities.
 *
 * @author Vojtech Zednik
 */
@Repository
public class CustomerRepository {
    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all customers that are not marked as deleted.
     *
     * @return list of active (non-deleted) customers
     */
    public List<Customer> findAll() {
        return em.createQuery("SELECT c FROM Customer c WHERE c.deleted = false", Customer.class)
                .getResultList();
    }

    /**
     * Finds an active customer by phone number.
     *
     * @param phoneNumber the phone number to search by
     * @return the found customer or null if not found
     */
    public Customer findByPhoneNumber(String phoneNumber) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.phoneNumber = :phone AND c.deleted = false", Customer.class)
                .setParameter("phone", phoneNumber)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a customer by its ID.
     *
     * @param id the customer ID
     * @return the customer, or null if not found or deleted
     */
    public Customer findById(Long id) {
        Customer customer = em.find(Customer.class, id);
        return (customer != null && !customer.isDeleted()) ? customer : null;
    }

    /**
     * Saves a new or existing customer.
     * If the customer does not have an ID, it is persisted, otherwise, it is merged.
     *
     * @param customer the customer to save
     * @return the managed customer entity
     */
    @Transactional
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            em.persist(customer);
            return customer;
        }
        return em.merge(customer);
    }
}