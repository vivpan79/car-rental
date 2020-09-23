package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    @Autowired
    private BookingRepository repository;

    @GetMapping("/")
    public List<Booking> getAll() {
        return repository.findAll();
    }
}
