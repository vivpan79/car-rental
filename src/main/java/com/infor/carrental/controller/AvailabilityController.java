package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @GetMapping
    public List<Availability> getAll() {
        return availabilityRepository.findAll();
    }

    @GetMapping(value = "/check/from/{fromDate}/to/{toDate}")
    public List<Availability> checkAvailability(
        @PathVariable(name = "fromDate")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime fromDate,
        @PathVariable(name = "toDate")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime toDate
    ) {
        System.out.println(fromDate + " , " + toDate);
        return availabilityRepository.findAll();
    }

}
