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
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(value = "/car/{numberPlate}/check/from/{fromDate}/to/{toDate}/maxrate/{maxPricePerHour}")
    public Boolean checkAvailability(
        @PathVariable(name = "numberPlate") String numberPlate,
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate,
        @PathVariable(name = "maxPricePerHour") Long maxPricePerHour
    ) {
        LOGGER
            .info("Checking availability of {} fromDate: {} toDate: {} at maxPricePerHour: {} ", numberPlate, fromDate,
                toDate, maxPricePerHour);
        return availabilityService.isAvailable(numberPlate, fromDate, toDate, maxPricePerHour);
    }

    @PostMapping(value = "/car/{numberPlate}/register/from/{fromDate}/to/{toDate}/rate/{pricePerHour}")
    public Availability registerAvailability(
        @PathVariable(name = "numberPlate") String numberPlate,
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate,
        @PathVariable(name = "pricePerHour") Long pricePerHour
    ) {
        LOGGER.info("Register availability of {} fromDate: {} toDate: {} at rate: {}", numberPlate, fromDate, toDate,
            pricePerHour);
        return availabilityService.registerAvailability(numberPlate, fromDate, toDate, pricePerHour);
    }

}
