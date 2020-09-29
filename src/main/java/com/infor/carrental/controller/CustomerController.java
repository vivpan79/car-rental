package com.infor.carrental.controller;

import com.infor.carrental.controller.model.RestCustomer;
import com.infor.carrental.controller.model.RestCustomers;
import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
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
    @ApiOperation(
        value = "Get all Customers information.",
        notes = "Get all Customers information.",
        response = RestCustomers.class)
    public RestCustomers getAll() {
        List<Customer> customers = customerService.findAll();
        List<RestCustomer> restCustomers = customers.stream().map(RestCustomer::new).collect(Collectors.toList());
        return restCustomers.isEmpty() ? new RestCustomers() : new RestCustomers(restCustomers);
    }

    @PostMapping(value = "register")
    @ApiOperation(
        value = "Register a Customer.",
        notes = "Register a Customer.",
        response = RestCustomer.class)
    public RestCustomer registerCustomer(
        @ApiParam(value = "Customer information")
        @RequestBody RestCustomer customer
    ) {
        Customer savedCustomer = customerService.save(customer.toJpa());
        return new RestCustomer(savedCustomer);
    }

}
