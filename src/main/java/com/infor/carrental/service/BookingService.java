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
import java.util.List;
import java.util.stream.Collectors;
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

    public Booking registerBooking(String numberPlate, LocalDateTime fromDate, LocalDateTime toDate) {
        if (!isAvailable(numberPlate, fromDate, toDate)) {
            throw new AlreadyBookedException(format("Car with numberPlate %s already booked", numberPlate));
        }
        if (!availabilityService.isAvailable(numberPlate, fromDate, toDate)) {
            throw new NoAvailabilityException(format("Car with numberPlate %s is not available", numberPlate));
        }
        Booking entity = new Booking();
        entity.setFromDate(fromDate);
        entity.setToDate(toDate);
        return bookingRepository.save(entity);
    }

    public List<Car> findBookedCars(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = bookingRepository.findByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        return bookings.stream().map(Booking::getCar).collect(Collectors.toList());
    }

    public Long findBookedHours(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Booking> bookings = bookingRepository.findByFromDateGreaterThanOrToDateLessThan(fromDate, toDate);
        return bookings.stream().map(x -> from(x.getFromDate()).until(x.getToDate(), ChronoUnit.HOURS))
            .mapToLong(Long::longValue).sum();
    }

    public Double getCarBookingFrequency(LocalDateTime fromDate, LocalDateTime toDate) {
        long totalCarsBooked = findBookedCars(fromDate, toDate).size();
        long totalBookedHours = findBookedHours(fromDate, toDate);
        return ((double) totalCarsBooked / totalBookedHours);
    }

    public Long paymentFromBookedCars(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Car> carsBooked = findBookedCars(fromDate, toDate);

        return carsBooked.stream().map(x ->
            {
                Availability availability = availabilityService.getAvailability(x.getNumberPlate(), fromDate, toDate);
                long hoursBooked = from(availability.getFromDate()).until(availability.getToDate(), ChronoUnit.HOURS);
                return (availability.getPricePerHour() * hoursBooked);
            }
        ).mapToLong(Long::longValue).sum();
    }
}
