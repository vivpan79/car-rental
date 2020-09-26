package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.exception.AlreadyBookedException;
import com.infor.carrental.exception.NoAvailabilityException;
import com.infor.carrental.persistence.entity.Booking;
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
    private AvailabilityService availabilityService;

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

    public Booking registerBooking(String numberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        if (!isAvailable(numberPlate, fromDate, toDate)) {
            throw new AlreadyBookedException(format("Car with numberPlate %s already booked", numberPlate));
        }
        if (!availabilityService.isAvailable(numberPlate, fromDate, toDate)) {
            throw new NoAvailabilityException(format("Car with numberPlate %s is not available", numberPlate));
        }
        Booking entity = new Booking();
        entity.setFromDate(fromDate);
        entity.setToDate(toDate);
        return bookingRepository.save(entity);
    }
}
