package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailabilityController {

    @Autowired
    private AvailabilityRepository repository;

    @GetMapping("/")
    public List<Availability> getAll(){
        return repository.findAll();
    }
}
