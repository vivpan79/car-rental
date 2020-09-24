package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Boolean isAvailable(LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<Availability> availabilities = availabilityRepository
            .findOptionalByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        System.out.println(format(" Availability fromDate: %s toDate: %s \n", fromDate, toDate));
        return !availabilities.isPresent();
    }
}
