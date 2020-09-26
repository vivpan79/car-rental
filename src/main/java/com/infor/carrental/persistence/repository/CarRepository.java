package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByNumberPlate(String numberPlate);
}
