package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.persistence.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository repository;

    @RequestMapping("/")
    public String welcome() {
        return "Welcome to Telenor's take-home assignment: Dynamic Query";
    }

    @GetMapping("/")
    public List<Customer> getAll(){
        return repository.findAll();
    }
}
