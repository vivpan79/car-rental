package com.infor.carrental.service;

import static java.lang.String.format;

import com.infor.carrental.exception.NoRegisteredCarException;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.repository.CarRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    private CarRepository repository;

    public Car findByNumberPlate(String numberPlate) {
        Optional<Car> byNumberPlate = repository.findByNumberPlate(numberPlate);
        if (byNumberPlate.isPresent()) {
            return byNumberPlate.get();
        }
        throw new NoRegisteredCarException(format("Car with number plate %s does not exist!", numberPlate));
    }
}
