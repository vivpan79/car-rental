package com.infor.carrental.controller;

import com.infor.carrental.controller.model.RestCar;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("car")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    @ApiOperation(
        value = "Get all registered Cars information.",
        notes = "Get all registered Cars information.",
        response = RestCar.class, responseContainer = "List")
    public List<RestCar> getAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(RestCar::new).collect(Collectors.toList());
    }

    @PostMapping("/register")
    @ApiOperation(
        value = "Register a Car.",
        notes = "Register a Car.",
        response = RestCar.class)
    public RestCar registerCar(
        @ApiParam(value = "Car information")
        @RequestBody RestCar car
    ) {
        Car savedCar = carRepository.save(car.toJpa());
        return new RestCar(savedCar);
    }
}
