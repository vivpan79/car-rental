package com.infor.carrental.controller.model;

import com.infor.carrental.persistence.entity.Customer;

public class RestCustomer {

    private Long id;
    private String userName;
    private String password;

    public RestCustomer() {
    }

    public RestCustomer(Customer customer) {
        this.id = customer.getId();
        this.userName = customer.getUserName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer toJpa(){
        Customer customer = new Customer();
        customer.setUserName(userName);
        customer.setPassword(password);
        return customer;
    }
}
