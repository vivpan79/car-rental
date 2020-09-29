package com.infor.carrental.service;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.exception.AlreadyBookedException;
import com.infor.carrental.exception.NoAvailabilityException;
import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.entity.Customer;
import com.infor.carrental.persistence.repository.CarRepository;
import com.infor.carrental.persistence.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        Optional<Car> carOptional = carRepository.findByNumberPlate("ABC123");
        if (!carOptional.isPresent()) {
            Car car = new Car();
            car.setNumberPlate("ABC123");
            carRepository.save(car);
        }
        Optional<Customer> customerOptional = customerRepository.findByUserName("TopGear");
        if (!customerOptional.isPresent()) {
            Customer customer = new Customer();
            customer.setUserName("TopGear");
            customerRepository.save(customer);
        }
    }

    @Test
    void givenBookingServiceWhenGetBookingForCarThenReturnJsonArray() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        List<Booking> bookingList = bookingService.findBookings("ABC123");

        assertNotNull(bookingList);
        assertEquals(1, bookingList.size());
    }

    @Transactional
    @Test
    void givenBookingServiceWhenCheckBookingForCarExactDateMatchThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now, now.plusHours(1L));

        assertFalse(isAvailable);
    }

    @Transactional
    @Test
    void givenBookingServiceWhenCheckBookingForCarThenReturnTrue() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        Boolean isAvailable = bookingService
            .isAvailable("ABC123", now.plusMinutes(20L), now.plusHours(1L).minusMinutes(10L));

        assertTrue(isAvailable);
    }

    @Transactional
    @Test
    void givenBookingServiceWhenCheckBookingForCarExceedFromDateThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now.minusNanos(1L), now.plusHours(1L));

        assertFalse(isAvailable);
    }

    @Transactional
    @Test
    void givenBookingServiceWhenCheckBookingForCarExceedToDateThenReturnFalse() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(1L));
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        Boolean isAvailable = bookingService.isAvailable("ABC123", now, now.plusMinutes(61L));

        assertFalse(isAvailable);
    }

    @Test
    void givenBookingServiceWhenRegisterBookingForAlreadyBookedCarThenException() {
        Booking booking = new Booking();
        LocalDateTime now = now();
        booking.setFromDate(now);
        booking.setToDate(now);
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setCustomer(customerRepository.findByUserName("TopGear").get());
        bookingService.save(booking);

        assertThrows(AlreadyBookedException.class, () -> bookingService.registerBooking("ABC123", now, now, "TopGear"));
    }

    @Test
    void givenBookingServiceWhenRegisterBookingForUnavailableCarThenException() {
        LocalDateTime now = now();

        assertThrows(NoAvailabilityException.class,
            () -> bookingService.registerBooking("ABC123", now, now, "userName"));
    }

    @Test
    void givenBookingServiceWhenFindBookedHoursThenReturnBookedHours() {
        LocalDateTime now = now();
        Booking booking = new Booking();
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setFromDate(now);
        booking.setToDate(now.plusHours(3));
        Customer savedCustomer = customerRepository.findByUserName("TopGear").get();
        booking.setCustomer(savedCustomer);
        bookingService.save(booking);
        booking = new Booking();
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setFromDate(now.plusMonths(1));
        booking.setToDate(now.plusMonths(1).plusHours(7));
        booking.setCustomer(savedCustomer);
        bookingService.save(booking);

        Long bookedHours = bookingService.findBookedHours(now, now.plusMonths(2));

        assertEquals(10, bookedHours);
    }

    @Test
    void givenBookingServiceWhenPaymentFromBookedCarsThenReturnPayment() {
        LocalDateTime now = now();
        Long payment = bookingService.paymentFromBookedCars(now, now.plusMonths(2));
        assertEquals(0, payment);
    }

    @Test
    void givenBookingServiceWhenGetCarBookingFrequencyThenReturnBookingFrequencyT() {
        bookingService.deleteAll();
        LocalDateTime now = now();
        Booking booking = new Booking();
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setFromDate(now.plusYears(1L));
        booking.setToDate(now.plusYears(1L).plusHours(3));
        Customer savedCustomer = customerRepository.findByUserName("TopGear").get();
        booking.setCustomer(savedCustomer);
        bookingService.save(booking);
        booking = new Booking();
        booking.setCar(carRepository.findByNumberPlate("ABC123").get());
        booking.setFromDate(now.plusYears(1L).plusMonths(1));
        booking.setToDate(now.plusYears(1L).plusMonths(1).plusHours(7));
        booking.setCustomer(savedCustomer);
        bookingService.save(booking);

        Double carBookingFrequency = bookingService.getCarBookingFrequency(now.plusYears(1L), now.plusYears(1L).plusMonths(2));

        assertEquals(0.2, carBookingFrequency);
    }

    @AfterEach
    void tearDown() {
        bookingService.deleteAll();
    }
}