package com.infor.carrental.service;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Booking;
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
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CarRepository carRepository;

    @Test
    void givenBookingServiceWhenGetBookingForCarThenReturnJsonArray() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        booking.setCar(savedCar);
        bookingService.save(booking);

        List<Booking> bookingList = bookingService.findBookings("ABC123");

        assertNotNull(bookingList);
        assertEquals(1, bookingList.size());
    }

    @Test
    void givenBookingServiceWhenCheckBookingForCarExactDateMatchThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        booking.setCar(savedCar);
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now, now.plusHours(1L));

        assertFalse(isAvailable);
        bookingService.deleteAll();
    }

    @Test
    void givenBookingServiceWhenCheckBookingForCarThenReturnTrue() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        booking.setCar(savedCar);
        bookingService.save(booking);

        Boolean isAvailable = bookingService
            .isAvailable("ABC123", now.plusMinutes(20L), now.plusHours(1L).minusMinutes(10L));

        assertTrue(isAvailable);
        bookingService.deleteAll();
    }

    @Test
    void givenBookingServiceWhenCheckBookingForCarExceedFromDateThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        booking.setCar(savedCar);
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now.minusNanos(1L), now.plusHours(1L));

        assertFalse(isAvailable);
        bookingService.deleteAll();
    }

    @Test
    void givenBookingServiceWhenCheckBookingForCarExceedToDateThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        Car car = new Car();
        car.setNumberPlate("ABC123");
        Car savedCar = carRepository.save(car);
        booking.setCar(savedCar);
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now, now.plusMinutes(61L));

        assertFalse(isAvailable);
        bookingService.deleteAll();
    }
}