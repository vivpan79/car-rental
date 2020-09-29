package com.infor.carrental.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import java.util.Collections;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
@ActiveProfiles("dev")
class CarRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarRepository carRepository;

    @Test
    void givenCarServiceWhenGetCarsThenReturnJsonArray() throws Exception {
        Car car = new Car();
        car.setNumberPlate("ABC-123");
        List<Car> cars = Collections.singletonList(car);
        given(carRepository.findAll()).willReturn(cars);
        mvc.perform(get("/car")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.restCars", hasSize(1)))
            .andExpect(jsonPath("$.restCars[0].numberPlate", is("ABC-123")));
    }

    @Test
    void givenCarServiceWhenRegisterCarsThenReturnRegisteredCar() throws Exception {
        Car car = new Car();
        car.setNumberPlate("ABC-123");
        given(carRepository.save(any(Car.class))).willReturn(car);
        mvc.perform(post("/car/register")
            .content("{\"id\":null,\"numberPlate\":\"ABC-123\"}")
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.numberPlate", is("ABC-123")));
    }
}
