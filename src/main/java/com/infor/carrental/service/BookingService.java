package com.infor.carrental.service;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    public static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Boolean isAvailable(String carNumberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> availabilities = bookingRepository
            .findByCarNumberPlateFromDateGreaterThanOrToDateLessThan(carNumberPlate, fromDate, toDate);
        logger.info("Availability of {} fromDate: {} toDate: {} ", carNumberPlate, fromDate, toDate);
        return availabilities.isEmpty();
    }

    public List<Booking> findBookings(String carNumberPlate) {
        return bookingRepository.findByCarNumberPlate(carNumberPlate);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);

    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }
}
