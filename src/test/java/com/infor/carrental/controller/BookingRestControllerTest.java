package com.infor.carrental.controller;

import static java.time.LocalDateTime.now;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.repository.BookingRepository;
import java.time.LocalDateTime;
import java.util.Collections;
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
public class BookingRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingRepository bookingRepository;

    @Test
    void givenBookingServiceWhenGetBookingsThenReturnJsonArray() throws Exception {
        Booking booking = new Booking();
        LocalDateTime date = now();
        booking.setFromDate(date);
        booking.setToDate(date);
        List<Booking> bookings = Collections.singletonList(booking);
        given(bookingRepository.findAll()).willReturn(bookings);
        mvc.perform(get("/booking")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].fromDate", is(date.toString())))
            .andExpect(jsonPath("$[0].toDate", is(date.toString())))
        ;
    }

    @Test
    void givenBookingServiceWhenCheckBookingThenReturnTrue() throws Exception {
        Booking booking = new Booking();
        LocalDateTime date = now();
        booking.setFromDate(date);
        booking.setToDate(date);
        List<Booking> bookings = Collections.singletonList(booking);
        given(bookingRepository.findAll()).willReturn(bookings);
        mvc.perform(get(
            "/booking/check/from/2020-03-01T13:00/to/2020-03-01T13:00"
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].fromDate", is(date.toString())))
            .andExpect(jsonPath("$[0].toDate", is(date.toString())))
        ;
    }
}
