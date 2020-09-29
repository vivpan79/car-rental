package com.infor.carrental.controller;

import static com.infor.carrental.controller.model.Constants.DATE_TIME;

import com.infor.carrental.controller.model.RestBooking;
import com.infor.carrental.controller.model.RestCar;
import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.service.BookingService;
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
@RequestMapping("booking")
public class BookingController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @ApiOperation(
        value = "Get all Car bookings.",
        notes = "Get all Car bookings.",
        response = RestBooking.class, responseContainer = "List")
    public List<RestBooking> getAll() {
        List<Booking> bookings = bookingService.findAll();
        return bookings.stream().map(RestBooking::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/car/{numberPlate}")
    @ApiOperation(
        value = "Get all bookings for a car.",
        notes = "Get all bookings for a car.",
        response = RestBooking.class, responseContainer = "List")
    public List<RestBooking> getBookings(
        @ApiParam(value = "Car numberPlate")
        @PathVariable(name = "numberPlate") String numberPlate
    ) {
        LOGGER.info("get booking for car : {}", numberPlate);
        List<Booking> bookings = bookingService.findBookings(numberPlate);
        return bookings.stream().map(RestBooking::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/car/{numberPlate}/check/from/{fromDate}/to/{toDate}")
    @ApiOperation(
        value = "Check booking of a car between dates.",
        notes = "Check booking of a car between dates.",
        response = Boolean.class)
    public Boolean checkBooking(
        @ApiParam(value = "Number plate of the car")
        @PathVariable(name = "numberPlate") String numberPlate,
        @ApiParam(value = "Car booking start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car booking end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate
    ) {
        LOGGER.info("Checking booking of {} fromDate: {} toDate: {} ", numberPlate, fromDate, toDate);
        return bookingService.isAvailable(numberPlate, fromDate, toDate);
    }

    @PostMapping(value = "/car/{numberPlate}/register/from/{fromDate}/to/{toDate}/for/{userName}")
    @ApiOperation(
        value = "Register booking of car between dates with booking rate for customer.",
        notes = "Register booking of car between dates with booking rate for customer.",
        response = RestBooking.class)
    public RestBooking registerBooking(
        @ApiParam(value = "Number plate of the car")
        @PathVariable(name = "numberPlate") String numberPlate,
        @ApiParam(value = "Car booking start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car booking end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate,
        @ApiParam(value = "Customer username")
        @PathVariable(name = "userName") String userName
    ) {
        LOGGER.info("Register booking of {} fromDate: {} toDate: {} by {}", numberPlate, fromDate, toDate);
        Booking booking = bookingService.registerBooking(numberPlate, fromDate, toDate, userName);
        return new RestBooking(booking);
    }

    @GetMapping(value = "/find/from/{fromDate}/to/{toDate}")
    @ApiOperation(
        value = "All booked cars between dates.",
        notes = "All booked cars between dates.",
        response = RestCar.class, responseContainer = "List")
    public List<RestCar> findBookedCars(
        @ApiParam(value = "Car booking start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car booking end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate
    ) {
        LOGGER.info("find Booked Cars fromDate: {} toDate: {} ", fromDate, toDate);
        List<Car> bookedCars = bookingService.findBookedCars(fromDate, toDate);
        return bookedCars.stream().map(RestCar::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/frequency/from/{fromDate}/to/{toDate}")
    @ApiOperation(
        value = "Get car booking frequency between dates.",
        notes = "Get car booking frequency between dates.",
        response = Double.class)
    public Double getCarBookingFrequency(
        @ApiParam(value = "Car booking start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car booking end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate
    ) {
        LOGGER.info("find Car booking frequency fromDate: {} toDate: {} ", fromDate, toDate);
        return bookingService.getCarBookingFrequency(fromDate, toDate);
    }

    @GetMapping(value = "/payments/from/{fromDate}/to/{toDate}")
    @ApiOperation(
        value = "Get total payment from car bookings between dates.",
        notes = "Get total payment from car bookings between dates.",
        response = Double.class)
    public Long getCarBookingPayment(
        @ApiParam(value = "Car booking start date.")
        @PathVariable(name = "fromDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime fromDate,
        @ApiParam(value = "Car booking end date.")
        @PathVariable(name = "toDate") @DateTimeFormat(pattern = DATE_TIME) LocalDateTime toDate
    ) {
        LOGGER.info("find payment from booked cars fromDate: {} toDate: {} ", fromDate, toDate);
        return bookingService.paymentFromBookedCars(fromDate, toDate);
    }
}
