package com.infor.carrental.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Customer;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void givenCustomerRepositoryWhenSaveAndRetrieveCustomerEntityThenExactMatch() {
        Customer entity = new Customer();
        Customer savedCustomer = repository.save(entity);
        Optional<Customer> retrievedCustomer = repository.findById(savedCustomer.getId());
        assertTrue(retrievedCustomer.isPresent());
    }
}