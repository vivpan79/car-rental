package com.infor.carrental.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Booking;
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
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void givenBookingRepositoryWhenSaveAndRetrieveBookingEntityThenExactMatch() {
        Car car = new Car();
        car.setNumberPlate("ABC-123");
        Car savedCar = carRepository.save(car);
        Booking booking = new Booking();
        LocalDateTime currentDataTime = LocalDateTime.now();
        booking.setCar(savedCar);
        booking.setFromDate(currentDataTime);
        booking.setToDate(currentDataTime.plusMinutes(1L));
        Booking savedBooking = bookingRepository.save(booking);
        List<Booking> availabilities = bookingRepository.findAll(Example.of(savedBooking));
        assertFalse(availabilities.isEmpty());
        assertEquals(currentDataTime, availabilities.get(0).getFromDate());
        assertEquals(currentDataTime.plusMinutes(1L), availabilities.get(0).getToDate());
        assertEquals("ABC-123", availabilities.get(0).getCar().getNumberPlate());
    }
}
