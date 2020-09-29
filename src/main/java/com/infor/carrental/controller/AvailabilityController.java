package com.infor.carrental.controller;

import static com.infor.carrental.controller.model.Constants.DATE_TIME;

import com.infor.carrental.controller.model.RestAvailabilities;
import com.infor.carrental.controller.model.RestAvailability;
import com.infor.carrental.controller.model.RestCar;
import com.infor.carrental.controller.model.RestCars;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.service.AvailabilityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    @ApiOperation(
        value = "Get all car availability information.",
        notes = "Get all car availability information.",
        response = RestAvailabilities.class)
    public RestAvailabilities getAll() {
        List<Availability> availabilities = availabilityService.findAll();
        List<RestAvailability> restAvailabilities = availabilities.stream().map(RestAvailability::new)
            .collect(Collectors.toList());
        return (restAvailabilities.isEmpty()) ? new RestAvailabilities() : new RestAvailabilities(restAvailabilities);
    }

    @GetMapping(value = "/car/{numberPlate}")
    @ApiOperation(
        value = "Get availability of a car.",
        notes = "Get availability of a car.",
        response = RestAvailabilities.class)
    public RestAvailabilities getAvailability(
        @ApiParam(value = "Number plate of the car")
        @PathVariable(name = "numberPlate") String numberPlate
    ) {
        LOGGER.info("get availability for car : {}", numberPlate);
        List<Availability> availabilities = availabilityService.getAvailability(numberPlate);
        List<RestAvailability> restAvailabilities = availabilities.stream().map(RestAvailability::new)
            .collect(Collectors.toList());
        return (restAvailabilities.isEmpty()) ? new RestAvailabilities() : new RestAvailabilities(restAvailabilities);
    }

    @GetMapping(value = "/car/{numberPlate}/check/from/{fromDate}/to/{toDate}/maxrate/{maxPricePerHour}")
    @ApiOperation(
        value = "Check availability of a car between dates.",
        notes = "Check availability of a car between dates.",
        response = Boolean.class)
    public Boolean checkAvailability(
        @ApiParam(value = "Number plate of the car")
        @PathVariable(name = "numberPlate") String numberPlate,
        @ApiParam(value = "Car availability start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car availability end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate,
        @PathVariable(name = "maxPricePerHour") Long maxPricePerHour
    ) {
        LOGGER
            .info("Checking availability of {} fromDate: {} toDate: {} at maxPricePerHour: {} ", numberPlate, fromDate,
                toDate, maxPricePerHour);
        return availabilityService.isAvailable(numberPlate, fromDate, toDate, maxPricePerHour);
    }

    @GetMapping(value = "/find/from/{fromDate}/to/{toDate}/maxrate/{maxPricePerHour}")
    @ApiOperation(
        value = "All available cars between dates.",
        notes = "All available cars between dates.",
        response = RestCars.class)
    public RestCars findAvailableCars(
        @ApiParam(value = "Car availability start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car availability end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate,
        @PathVariable(name = "maxPricePerHour") Long maxPricePerHour
    ) {
        LOGGER
            .info("Find available cars fromDate: {} toDate: {} at maxPricePerHour: {} ", fromDate,
                toDate, maxPricePerHour);
        List<Car> availableCars = availabilityService.findAvailableCars(fromDate, toDate, maxPricePerHour);
        List<RestCar> restCars = availableCars.stream().map(RestCar::new).collect(Collectors.toList());
        return (restCars.isEmpty()) ? new RestCars() : new RestCars(restCars);
    }

    @PostMapping(value = "/car/{numberPlate}/register/from/{fromDate}/to/{toDate}/rate/{pricePerHour}")
    @ApiOperation(
        value = "Register availability of car between dates with booking rate.",
        notes = "Register availability of car between dates with booking rate.",
        response = RestAvailability.class)
    public RestAvailability registerAvailability(
        @ApiParam(value = "Number plate of the car")
        @PathVariable(name = "numberPlate") String numberPlate,
        @ApiParam(value = "Car availability start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car availability end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate,
        @PathVariable(name = "pricePerHour") Long pricePerHour
    ) {
        LOGGER.info("Register availability of {} fromDate: {} toDate: {} at rate: {}", numberPlate, fromDate, toDate,
            pricePerHour);
        return new RestAvailability(
            availabilityService.registerAvailability(numberPlate, fromDate, toDate, pricePerHour));
    }

}
