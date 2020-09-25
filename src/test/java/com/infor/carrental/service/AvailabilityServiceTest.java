package com.infor.carrental.service;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class AvailabilityServiceTest {

    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private CarRepository carRepository;

    @Test
    void givenAvailabilityServiceWhenGetAvailabilityForCarThenReturnJsonArray() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        List<Availability> availabilityList = availabilityService.getAvailability("ABC123");

        assertNotNull(availabilityList);
        assertEquals(1, availabilityList.size());
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarThenReturnJsonArray() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        Boolean isAvailable = availabilityService.isAvailable("ABC123", now, now.plusHours(1L));

        assertTrue(isAvailable);
    }
}