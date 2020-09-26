package com.infor.carrental.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.infor.carrental.Application;
import com.infor.carrental.exception.AlreadyRegisteredUserException;
import com.infor.carrental.persistence.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void givenCustomerServiceWhenSaveCustomerForExistingUserNameThenException() {

        Customer customer = new Customer();
        customer.setUserName("Newuser1");
        customer.setPassword("Password");
        customerService.save(customer);

        assertThrows(AlreadyRegisteredUserException.class, () -> {
            customerService.save(customer);
        });
    }
}