package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @GetMapping(value = "/check/from/{fromDate}/to/{toDate}")
    public List<Booking> checkBooking(
        @PathVariable(name = "fromDate")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime fromDate,
        @PathVariable(name = "toDate")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime toDate
    ) {
        System.out.println(fromDate + " , " + toDate);
        return bookingRepository.findAll();
    }
}
