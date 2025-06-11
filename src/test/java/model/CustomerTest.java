package model;

import com.IQproject.court.model.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    @Test
    void testAllGettersAndSetters() {
        Customer customer = new Customer();

        Long expectedId = 1L;
        String expectedName = "Jan Novak";
        String expectedPhone = "123456789";
        boolean expectedDeleted = true;

        customer.setId(expectedId);
        customer.setName(expectedName);
        customer.setPhoneNumber(expectedPhone);
        customer.setDeleted(expectedDeleted);

        assertEquals(expectedId, customer.getId());
        assertEquals(expectedName, customer.getName());
        assertEquals(expectedPhone, customer.getPhoneNumber());
        assertTrue(customer.isDeleted());
    }

    @Test
    void testConstructorWithParameters() {
        String name = "Karel Dvořák";
        String phone = "987654321";

        Customer customer = new Customer(phone, name);

        assertNull(customer.getId()); // ID is not set
        assertEquals(name, customer.getName());
        assertEquals(phone, customer.getPhoneNumber());
        assertFalse(customer.isDeleted()); // default value
    }
    @Test
    void testConstructorWithNoParameters() {
        String name = "Karel Dvořák";
        String phone = "987654321";
        Long id = 1L;

        Customer customer = new Customer();

        assertNotNull(customer);

        customer.setId(id);
        customer.setName(name);
        customer.setPhoneNumber(phone);
        customer.setDeleted(true);

        assertEquals(id, customer.getId());
        assertEquals(name, customer.getName());
        assertEquals(phone, customer.getPhoneNumber());
        assertTrue(customer.isDeleted());
    }
}