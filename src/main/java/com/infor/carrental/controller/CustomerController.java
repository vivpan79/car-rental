package com.infor.carrental.controller;

import com.infor.carrental.controller.model.RestCustomer;
import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.findAll();
    }

    @PostMapping(value = "register")
    public RestCustomer registerCustomer(@RequestBody RestCustomer customer){
        Customer savedCustomer = customerService.save(customer.toJpa());
        return new RestCustomer(savedCustomer);
    }

}
