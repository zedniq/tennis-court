package repository;

import com.IQproject.court.model.Customer;
import com.IQproject.court.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.IQproject.court.CourtApplication.class)
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        em.createQuery("DELETE FROM Customer").executeUpdate();
    }

    @Test
    void testSaveNewCustomer() {
        String phoneNumber = "123456789";
        String name = "Alice";

        Customer customer = new Customer(phoneNumber, name);
        Customer saved = customerRepository.save(customer);

        assertNotNull(saved);
        assertNotNull(saved.getId());

        Customer found = em.find(Customer.class, saved.getId());

        assertNotNull(found);
        assertEquals(name, found.getName());
        assertEquals(phoneNumber, found.getPhoneNumber());
    }

    @Test
    void testFindAllReturnsOnlyNotDeleted() {
        Customer c1 = new Customer("111111111", "Bob");
        Customer c2 = new Customer("222222222", "Vaclav");
        Customer c3 = new Customer("333333333", "David");
        c3.setDeleted(true);

        em.persist(c1);
        em.persist(c2);
        em.persist(c3);
        em.flush();

        List<Customer> customers = customerRepository.findAll();

        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertTrue(customers.stream().noneMatch(Customer::isDeleted));
    }

    @Test
    void testFindByIdReturnsNullIfDeleted() {
        Customer customer = new Customer("444444444", "Erik");
        em.persist(customer);
        em.flush();

        customer.setDeleted(true);
        em.merge(customer);
        em.flush();

        Customer result = customerRepository.findById(customer.getId());
        assertNull(result);
    }

    @Test
    void testFindByPhoneNumber() {
        String phoneNumber = "123456789";
        String name = "Frank";
        String notExistingNumber = "789456123";

        Customer customer = new Customer(phoneNumber, name);
        em.persist(customer);
        em.flush();

        Customer found = customerRepository.findByPhoneNumber(phoneNumber);
        assertNotNull(found);
        assertEquals(name, found.getName());

        Customer notFound = customerRepository.findByPhoneNumber(notExistingNumber);
        assertNull(notFound);
    }
}