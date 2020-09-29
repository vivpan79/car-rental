package com.infor.carrental.service;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.exception.NoRegisteredCarException;
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
        car.setNumberPlate("ABC1111");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        List<Availability> availabilityList = availabilityService.getAvailability("ABC1111");

        assertNotNull(availabilityList);
        assertEquals(1, availabilityList.size());
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarExactDateMatchThenReturnTrue() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        availability.setPricePerHour(Long.MAX_VALUE);
        Car car = new Car();
        car.setNumberPlate("ABC2222");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        Boolean isAvailable = availabilityService.isAvailable("ABC2222", now, now.plusHours(1L), Long.MAX_VALUE);

        assertTrue(isAvailable);
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarThenReturnTrue() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        availability.setPricePerHour(Long.MAX_VALUE);
        Car car = new Car();
        car.setNumberPlate("ABC3333");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        Boolean isAvailable = availabilityService
            .isAvailable("ABC3333", now.plusMinutes(20L), now.plusHours(1L).minusMinutes(10L), Long.MAX_VALUE);

        assertTrue(isAvailable);
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarExceedFromDateThenReturnFalse() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC4444");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        Boolean isAvailable = availabilityService.isAvailable("ABC4444", now.minusNanos(1L), now.plusHours(1L),
            Long.MAX_VALUE);

        assertFalse(isAvailable);
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarExceedToDateThenReturnFalse() {
        Availability availability = new Availability();
        LocalDateTime now = now();
        availability.setFromDate(now);
        availability.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC5555");
        Car savedCar = carRepository.save(car);
        availability.setCar(savedCar);
        availabilityService.save(availability);

        Boolean isAvailable = availabilityService.isAvailable("ABC5555", now, now.plusMinutes(61L), Long.MAX_VALUE);

        assertFalse(isAvailable);
    }

    @Test
    void givenAvailabilityServiceWhenRegisterAvailabilityForMissingCarThenException() {
        LocalDateTime now = now();
        assertThrows(NoRegisteredCarException.class,
            () -> availabilityService.registerAvailability("ABC6666", now, now, 100L));
    }

    @Test
    void getAvailability() {

        LocalDateTime now = now();
        Car car = new Car();
        car.setNumberPlate("ABC6666");
        Car savedCar = carRepository.save(car);
        availabilityService.registerAvailability("ABC6666", now, now.plusMinutes(61L), 61L);

        Availability carAvailability = availabilityService.getAvailability("ABC6666", now, now.plusMinutes(61L));

        assertEquals("ABC6666", carAvailability.getCar().getNumberPlate());
        assertEquals(61L, carAvailability.getPricePerHour());
    }
}