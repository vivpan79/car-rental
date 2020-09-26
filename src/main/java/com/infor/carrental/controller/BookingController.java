package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.service.BookingService;
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
@RequestMapping("booking")
public class BookingController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.findAll();
    }

    @GetMapping(value = "/car/{numberPlate}")
    public List<Booking> getBookings(
        @PathVariable(name = "numberPlate") String numberPlate
    ) {
        LOGGER.info("get booking for car : {}", numberPlate);
        return bookingService.findBookings(numberPlate);
    }

    @GetMapping(value = "/car/{numberPlate}/check/from/{fromDate}/to/{toDate}")
    public Boolean checkBooking(
        @PathVariable(name = "numberPlate") String numberPlate,
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("Checking booking of {} fromDate: {} toDate: {} ", numberPlate, fromDate, toDate);
        return bookingService.isAvailable(numberPlate, fromDate, toDate);
    }

    @PostMapping(value = "/car/{numberPlate}/register/from/{fromDate}/to/{toDate}")
    public Booking registerBooking(
        @PathVariable(name = "numberPlate") String numberPlate,
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("Register booking of {} fromDate: {} toDate: {} ", numberPlate, fromDate, toDate);
        return bookingService.registerBooking(numberPlate, fromDate, toDate);
    }

    @GetMapping(value = "/find/from/{fromDate}/to/{toDate}")
    public List<Car> findBookedCars(
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("find Booked Cars fromDate: {} toDate: {} ", fromDate, toDate);
        return bookingService.findBookedCars(fromDate, toDate);
    }

    @GetMapping(value = "/frequency/from/{fromDate}/to/{toDate}")
    public Double getCarBookingFrequency(
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("find Car booking frequency fromDate: {} toDate: {} ", fromDate, toDate);
        return bookingService.getCarBookingFrequency(fromDate, toDate);
    }

    @GetMapping(value = "/payments/from/{fromDate}/to/{toDate}")
    public Double getCarBookingPayment(
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fromDate,
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime toDate
    ) {
        LOGGER.info("find payment from booked cars fromDate: {} toDate: {} ", fromDate, toDate);
        return bookingService.paymentFromBookedCars(fromDate, toDate);
    }
}
