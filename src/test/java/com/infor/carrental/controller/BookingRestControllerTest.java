package com.infor.carrental.controller;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.service.BookingService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
@ActiveProfiles("dev")
class BookingRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void givenBookingServiceWhenGetBookingsForCarThenReturnJsonArray() throws Exception {
        Booking booking = new Booking();
        LocalDateTime date = now();
        booking.setFromDate(date);
        booking.setToDate(date);
        List<Booking> bookings = singletonList(booking);
        given(bookingService.findBookings(anyString())).willReturn(bookings);

        mvc.perform(get("/booking/car/ABC123")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].fromDate", is(date.toString())))
            .andExpect(jsonPath("$[0].toDate", is(date.toString())))
        ;
    }

    @Test
    void givenBookingServiceWhenCheckBookingThenReturnTrue() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        given(bookingService.isAvailable(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(TRUE);
        mvc.perform(get(
            "/booking/car/ABC123/check/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(TRUE)))
        ;
    }

    @Test
    void givenBookingServiceWhenRegisterBookingThenReturnTrue() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Booking booking = new Booking();
        booking.setFromDate(date);
        booking.setToDate(date);
        given(bookingService.registerBooking(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(booking);
        mvc.perform(post(
            "/booking/car/ABC123/register/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fromDate", is(date.toString())))
            .andExpect(jsonPath("$.toDate", is(date.toString())))
        ;
    }

    @Test
    void givenBookingServiceWhenGetBookedCarsThenReturnJsonArray() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Car car = new Car();
        car.setNumberPlate("ABC123");
        List<Car> cars = singletonList(car);
        given(bookingService.findBookedCars(any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(cars);
        mvc.perform(get(
            "/booking/find/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].numberPlate", is("ABC123")))
        ;
    }

    @Test
    void givenBookingServiceWhenGetCarBookingFrequencyThenReturnBookingFrequency() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Booking booking = new Booking();
        booking.setFromDate(date);
        booking.setToDate(date);
        given(bookingService.getCarBookingFrequency(any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(0.314);
        mvc.perform(get(
            "/booking/frequency/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(0.314)))
        ;
    }

    @Test
    void givenBookingServiceWhenGetPaymentFromBookedCarsThenReturnPayment() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Booking booking = new Booking();
        booking.setFromDate(date);
        booking.setToDate(date);
        given(bookingService.paymentFromBookedCars(any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(123L);
        mvc.perform(get(
            "/booking/payments/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(123)))
        ;
    }
}
