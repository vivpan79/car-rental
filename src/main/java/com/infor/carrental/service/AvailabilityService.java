package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.exception.NoAvailabilityException;
import com.infor.carrental.exception.NoRegisteredCarException;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.AvailabilityRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    public static final Logger logger = LoggerFactory.getLogger(AvailabilityService.class);

    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private CarService carService;

    public Boolean isAvailable(String carNumberPlate, LocalDateTime fromDate, LocalDateTime toDate,
        Long maxPricePerHour) {
        List<Availability> availabilities = availabilityRepository
            .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqualPricePerHourLessThanEqual(carNumberPlate, fromDate, toDate,
                maxPricePerHour);
        logger
            .info("Availability of {} fromDate: {} toDate: {} at maxPricePerHour: {}", carNumberPlate, fromDate, toDate,
                maxPricePerHour);
        return !availabilities.isEmpty();
    }

    public List<Availability> findAll() {
        return availabilityRepository.findAll();
    }

    public List<Availability> getAvailability(String numberPlate) {
        return availabilityRepository.findByCarNumberPlate(numberPlate);
    }

    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Transactional(TxType.REQUIRED)
    public Availability registerAvailability(String numberPlate, LocalDateTime fromDate, LocalDateTime toDate,
        Long pricePerHour) {
        Car savedCar = carService.findByNumberPlate(numberPlate);
        if (null == savedCar) {
            throw new NoRegisteredCarException(format("Car with numberPlate %s does not exist", numberPlate));
        }
        if(null == pricePerHour){
            throw new IllegalArgumentException("Booking rate needs to be specified to register availability!");
        }
        Availability availability = new Availability();
        availability.setFromDate(fromDate);
        availability.setToDate(toDate);
        availability.setCar(savedCar);
        availability.setPricePerHour(pricePerHour);
        return availabilityRepository.save(availability);
    }

    public void deleteAll() {
        availabilityRepository.deleteAll();
    }

    public boolean isAvailable(String numberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        return isAvailable(numberPlate, fromDate, toDate, Long.MAX_VALUE);
    }

    public List<Car> findAvailableCars(LocalDateTime fromDate, LocalDateTime toDate, Long maxPricePerHour) {
        List<Availability> availabilityList = availabilityRepository
            .findCarByFromDateLessThanEqualAndToDateGreaterThanEqualAndLessThanEqualPricePerHour(fromDate, toDate,
                maxPricePerHour);
        if (null == availabilityList || availabilityList.isEmpty()) {
            return Collections.emptyList();
        }
        return availabilityList.stream().map(Availability::getCar).collect(Collectors.toList());
    }

    public Availability getAvailability(String carNumberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Availability> availabilities = availabilityRepository
            .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqualPricePerHourLessThanEqual(carNumberPlate, fromDate, toDate,
                Long.MAX_VALUE);
        logger.info("get Availability of {} fromDate: {} toDate: {}", carNumberPlate, fromDate, toDate);
        if (null == availabilities || availabilities.isEmpty()) {
            throw new NoAvailabilityException(
                format("No Availability of %s fromDate: %s toDate: %s", carNumberPlate, fromDate, toDate));
        }
        return availabilities.get(0);
    }
}
