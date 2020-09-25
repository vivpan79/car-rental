package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.service.AvailabilityService;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/availability")
public class AvailabilityController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityController.class);

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping
    public List<Availability> getAll() {
        return availabilityService.findAll();
    }

    @GetMapping(value = "/car/{numberPlate}")
    public List<Availability> getAvailability(
        @PathVariable(name = "numberPlate") String numberPlate
    ) {
        LOGGER.info("get availability for car : {}", numberPlate);
        return availabilityService.getAvailability(numberPlate);
    }

    @GetMapping(value = "/car/{numberPlate}/check/from/{fromDate}/to/{toDate}")
    public Boolean checkAvailability(
        @PathVariable(name = "numberPlate") String numberPlate,
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("Checking availability of {} fromDate: {} toDate: {} ", numberPlate, fromDate, toDate);
        return availabilityService.isAvailable(numberPlate, fromDate, toDate);
    }
}
