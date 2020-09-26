package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Availability;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Query(value = "FROM Availability AS a JOIN Car AS c ON a.car.id = c.id WHERE c.numberPlate = ?1 AND a.fromDate <= ?2 AND a.toDate >= ?3 AND a.pricePerHour <= ?4")
    List<Availability> findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual(String numberPlate,
        LocalDateTime fromDate, LocalDateTime toDate, Long maxPricePerHour);

    @Query(value = "FROM Availability AS a JOIN Car AS c ON a.car.id = c.id WHERE c.numberPlate = ?1")
    List<Availability> findByCarNumberPlate(String numberPlate);

    @Query(value = "FROM Availability AS a JOIN Car AS c ON a.car.id = c.id WHERE a.fromDate <= ?1 AND a.toDate >= ?2 AND a.pricePerHour <= ?3")
    List<Availability> findCarByFromDateLessThanEqualAndToDateGreaterThanEqualAndLessThanEqualPricePerHour(
        LocalDateTime fromDate, LocalDateTime toDate, Long maxPricePerHour);
}
