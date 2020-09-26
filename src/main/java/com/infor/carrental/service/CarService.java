package com.infor.carrental.service;

import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    private CarRepository repository;

    public Car findByNumberPlate(String numberPlate) {
        return repository.findByNumberPlate(numberPlate);
    }
}
