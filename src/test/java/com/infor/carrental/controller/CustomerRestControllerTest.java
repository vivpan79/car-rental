package com.infor.carrental.controller;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.service.CustomerService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
@ActiveProfiles("dev")
class CustomerRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void givenCustomerServiceWhenGetCustomersThenReturnJsonArray() throws Exception {
        Customer customer = new Customer();
        customer.setUserName("NewUser");
        List<Customer> customers = singletonList(customer);
        given(customerService.findAll()).willReturn(customers);
        mvc.perform(get("/customer")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].userName", is("NewUser")));
    }

    @Test
    void givenCustomerServiceWhenRegisterCustomerThenReturnJsonArray() throws Exception {
        Customer customer = new Customer();
        customer.setUserName("NewUser");
        customer.setPassword("Password");
        given(customerService.save(any(Customer.class))).willReturn(customer);
        mvc.perform(post("/customer/register")
            .content(new ObjectMapper().writeValueAsString(new RestCustomer(customer)))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName", is("NewUser")))
        ;
    }
}
