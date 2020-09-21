package com.infor.carrental.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infor.carrental.Application;
import com.infor.carrental.persistence.entity.Car;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class CarRepositoryTest {

    @Autowired
    private CarRepository repository;

    @Test
    void givenCarRepositoryWhenSaveAndRetrieveCarEntityThenExactMatch() {
        Car entity = new Car();
        Car savedCar = repository.save(entity);
        Optional<Car> retrievedCar = repository.findById(savedCar.getId());
        assertTrue(retrievedCar.isPresent());
    }
}