package com.infor.carrental.controller;

import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/car")
public class CarController {

    @Autowired
    private CarRepository repository;

    @GetMapping("/")
    public List<Car> getAll() {
        return repository.findAll();
    }
}
