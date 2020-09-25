package com.infor.carrental.service;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    public static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Boolean isAvailable(LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<Availability> availabilities = availabilityRepository
            .findOptionalByFromDateLessThanEqualAndToDateGreaterThanEqual(fromDate, toDate);
        logger.info("Availability fromDate: {} toDate: {} ", fromDate, toDate);
        return availabilities.isPresent();
    }

    public List<Availability> findAll() {
        return availabilityRepository.findAll();
    }
}
