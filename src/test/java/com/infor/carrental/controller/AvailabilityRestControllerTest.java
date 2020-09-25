package com.infor.carrental.controller;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.service.AvailabilityService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@WebMvcTest(AvailabilityController.class)
@ActiveProfiles("dev")
class AvailabilityRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Test
    void givenAvailabilityServiceWhenGetAvailabilityForCarThenReturnJsonArray() throws Exception {
        Availability availability = new Availability();
        LocalDateTime date = now();
        availability.setFromDate(date);
        availability.setToDate(date);
        List<Availability> availabilityList = Collections.singletonList(availability);
        given(availabilityService.getAvailability(anyString())).willReturn(availabilityList);

        mvc.perform(get("/availability/car/ABC123")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].fromDate", is(date.toString())))
            .andExpect(jsonPath("$[0].toDate", is(date.toString())))
        ;
    }

    @Test
    void givenAvailabilityServiceWhenCheckAvailabilityForCarThenReturnTrue() throws Exception {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        given(availabilityService.isAvailable(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .willReturn(TRUE);

        mvc.perform(get(
            "/availability/car/ABC123/check/from/" + formatter.format(date) + "/to/" + formatter.format(date)
        )
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(TRUE)))
        ;
    }
}
