package com.infor.carrental.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class AvailabilityRepositoryTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void givenAvailabilityRepositoryWhenSaveAndRetrieveAvailabilityEntityThenExactMatch() {
        Car car = new Car();
        car.setNumberPlate("ABC-111");
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
        assertEquals("ABC-111", availabilities.get(0).getCar().getNumberPlate());
    }

    @Test
    void givenAvailabilityRepositoryWhenBookingDateMatchAvailabilityThenReturnEmptyRejectedByAvailability() {
        Car car = new Car();
        car.setNumberPlate("ABC-222");
        Car savedCar = carRepository.save(car);
        Availability availability = new Availability();
        LocalDateTime currentDataTime = LocalDateTime.now();
        LocalDateTime availabilityStart = currentDataTime;
        LocalDateTime bookingStart = currentDataTime;
        LocalDateTime bookingEnd = currentDataTime.plusHours(1L);
        LocalDateTime availabilityEnd = currentDataTime.plusHours(1L);
        availability.setCar(savedCar);
        availability.setFromDate(availabilityStart);
        availability.setToDate(availabilityEnd);
        availability.setPricePerHour(Long.MAX_VALUE);
        availabilityRepository.save(availability);

        List<Availability> availabilities =
            availabilityRepository
                .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual("ABC-222", bookingStart,
                    bookingEnd, Long.MAX_VALUE);
        assertFalse(availabilities.isEmpty());
        assertEquals("ABC-222", availabilities.get(0).getCar().getNumberPlate());
    }

    @Test
    void givenAvailabilityRepositoryWhenBookingDateBetweenAvailabilityThenReturnEmptyRejectedByAvailability() {
        Car car = new Car();
        car.setNumberPlate("ABC-333");
        Car savedCar = carRepository.save(car);
        Availability availability = new Availability();
        LocalDateTime currentDataTime = LocalDateTime.now();
        LocalDateTime availabilityStart = currentDataTime;
        LocalDateTime bookingStart = currentDataTime.plusHours(1L);
        LocalDateTime bookingEnd = currentDataTime.plusHours(2L);
        LocalDateTime availabilityEnd = currentDataTime.plusHours(3L);
        availability.setCar(savedCar);
        availability.setFromDate(availabilityStart);
        availability.setToDate(availabilityEnd);
        availability.setPricePerHour(10L);
        availabilityRepository.save(availability);

        List<Availability> availabilities =
            availabilityRepository
                .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual("ABC-333", bookingStart,
                    bookingEnd, Long.MAX_VALUE);

        assertFalse(availabilities.isEmpty());
        assertEquals("ABC-333", availabilities.get(0).getCar().getNumberPlate());
    }

    @Test
    void givenAvailabilityRepositoryWhenBookingDateFromDateExceedsAvailabilityThenReturnRejectedByAvailability() {
        Car car = new Car();
        car.setNumberPlate("ABC-444");
        Car savedCar = carRepository.save(car);
        Availability availability = new Availability();
        LocalDateTime currentDataTime = LocalDateTime.now();
        LocalDateTime availabilityStart = currentDataTime;
        LocalDateTime bookingStart = currentDataTime.minusMinutes(1L);
        LocalDateTime bookingEnd = currentDataTime.plusHours(1L);
        LocalDateTime availabilityEnd = currentDataTime.plusHours(1L);
        availability.setCar(savedCar);
        availability.setFromDate(availabilityStart);
        availability.setToDate(availabilityEnd);
        availabilityRepository.save(availability);

        List<Availability> availabilities =
            availabilityRepository
                .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual("ABC-444", bookingStart,
                    bookingEnd, Long.MAX_VALUE);

        assertTrue(availabilities.isEmpty());
    }

    @Test
    void givenAvailabilityRepositoryWhenBookingDateToDateExceedsAvailabilityThenReturnRejectedByAvailability() {
        Car car = new Car();
        car.setNumberPlate("ABC-555");
        Car savedCar = carRepository.save(car);
        Availability availability = new Availability();
        LocalDateTime currentDataTime = LocalDateTime.now();
        LocalDateTime availabilityStart = currentDataTime;
        LocalDateTime bookingStart = currentDataTime;
        LocalDateTime bookingEnd = currentDataTime.plusMinutes(61L);
        LocalDateTime availabilityEnd = currentDataTime.plusHours(1L);
        availability.setCar(savedCar);
        availability.setFromDate(availabilityStart);
        availability.setToDate(availabilityEnd);
        availabilityRepository.save(availability);

        List<Availability> availabilities =
            availabilityRepository
                .findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual("ABC-555", bookingStart,
                    bookingEnd, Long.MAX_VALUE);

        assertTrue(availabilities.isEmpty());
    }

}
