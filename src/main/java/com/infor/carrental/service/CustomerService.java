package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.exception.AlreadyRegisteredUserException;
import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.persistence.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer save(Customer customer) {
        if (userExits(customer.getUserName())) {
            throw new AlreadyRegisteredUserException(
                format("User with username %s already exists", customer.getUserName()));
        }
        return repository.save(customer);
    }

    private boolean userExits(String userName) {
        return repository.findByUserName(userName).isPresent();
    }
}
