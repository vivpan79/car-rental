package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Boolean isAvailable(LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<Availability> availabilities = availabilityRepository
            .findOptionalByFromDateLessThanEqualAndToDateGreaterThanEqual(fromDate, toDate);
        System.out.println(format(" Availability fromDate: %s toDate: %s \n", fromDate, toDate));
        return availabilities.isPresent();
    }

    private Availability findConflictingAvailability(LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<Availability> availabilities = availabilityRepository
            .findOptionalByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        System.out.println(format(" Availability fromDate: %s toDate: %s \n", fromDate, toDate));
        return availabilities.get();
    }

    public List<Availability> findAll() {
        return availabilityRepository.findAll();
    }
}
