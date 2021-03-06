package com.infor.carrental.service;

import static java.lang.String.format;
import static java.time.LocalDateTime.from;

import com.infor.carrental.exception.AlreadyBookedException;
import com.infor.carrental.exception.NoAvailabilityException;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
public class BookingService {

    public static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private CarService carService;
    @Autowired
    private CustomerService customerService;

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Boolean isAvailable(String carNumberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> availabilities = bookingRepository
            .findByCarNumberPlateFromDateGreaterThanOrToDateLessThan(carNumberPlate, fromDate, toDate);
        logger.info("Availability of {} fromDate: {} toDate: {} ", carNumberPlate, fromDate, toDate);
        return availabilities.isEmpty();
    }

    public List<Booking> findBookings(String carNumberPlate) {
        return bookingRepository.findByCarNumberPlate(carNumberPlate);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);

    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Transactional(TxType.REQUIRED)
    public Booking registerBooking(String numberPlate, LocalDateTime fromDate, LocalDateTime toDate,
        String userName) {
        if (!isAvailable(numberPlate, fromDate, toDate)) {
            throw new AlreadyBookedException(format("Car with numberPlate %s already booked", numberPlate));
        }
        if (!availabilityService.isAvailable(numberPlate, fromDate, toDate)) {
            throw new NoAvailabilityException(format("Car with numberPlate %s is not available", numberPlate));
        }
        Booking entity = new Booking();
        entity.setFromDate(fromDate);
        entity.setToDate(toDate);
        entity.setCar(carService.findByNumberPlate(numberPlate));
        entity.setCustomer(customerService.findByUserName(userName));
        return bookingRepository.save(entity);
    }

    public List<Booking> getBookings(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = bookingRepository.findByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        if (null == bookings || bookings.isEmpty()) {
            return Collections.emptyList();
        }
        return bookings;
    }

    public List<Car> findBookedCars(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = bookingRepository.findByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        if (null == bookings || bookings.isEmpty()) {
            return Collections.emptyList();
        }
        return bookings.stream().map(Booking::getCar).collect(Collectors.toList());
    }

    public Long findBookedHours(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = bookingRepository.findByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        if (null == bookings || bookings.isEmpty()) {
            return 0L;
        }
        return bookings.stream().map(x -> from(x.getFromDate()).until(x.getToDate(), ChronoUnit.HOURS))
            .mapToLong(Long::longValue).sum();
    }

    public Double getCarBookingFrequency(LocalDateTime fromDate, LocalDateTime toDate) {
        long totalCarsBookings = getBookings(fromDate, toDate).size();
        long totalBookedHours = findBookedHours(fromDate, toDate);
        return ((double) totalCarsBookings / totalBookedHours);
    }

    public Long paymentFromBookedCars(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = getBookings(fromDate, toDate);
        if (bookings == null) {
            return 0L;
        }
        return bookings.stream().map(x ->
            {
                Availability availability = availabilityService
                    .getAvailability(x.getCar().getNumberPlate(), x.getFromDate(), x.getFromDate());
                long hoursBooked = from(availability.getFromDate()).until(availability.getToDate(), ChronoUnit.HOURS);
                return (availability.getPricePerHour() * hoursBooked);
            }
        ).mapToLong(Long::longValue).sum();
    }

}
