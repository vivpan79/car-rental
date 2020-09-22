package com.infor.carrental.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Car;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class AvailabilityRepositoryTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void givenAvailabilityRepositoryWhenSaveAndRetrieveAvailabilityEntityThenExactMatch() {
        Car car = new Car();
        car.setNumberPlate("ABC-123");
        Car savedCar = carRepository.save(car);
        Availability availability = new Availability();
        LocalDateTime currentDataTime = LocalDateTime.now();
        availability.setCar(savedCar);
        availability.setFromDate(currentDataTime);
        availability.setToDate(currentDataTime.plusMinutes(1L));
        Availability savedAvailability = availabilityRepository.save(availability);
        List<Availability> availabilities = availabilityRepository.findAll(Example.of(savedAvailability));
        assertFalse(availabilities.isEmpty());
        assertEquals(currentDataTime, availabilities.get(0).getFromDate());
        assertEquals(currentDataTime.plusMinutes(1L), availabilities.get(0).getToDate());
        assertEquals("ABC-123", availabilities.get(0).getCar().getNumberPlate());
    }
}
