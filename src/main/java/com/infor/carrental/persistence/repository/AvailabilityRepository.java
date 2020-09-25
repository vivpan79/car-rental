package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Availability;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    Optional<Availability> findOptionalByFromDateGreaterThanOrToDateLessThan(LocalDateTime fromDate,
        LocalDateTime toDate);

    Optional<Availability> findOptionalByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDateTime fromDate,
        LocalDateTime toDate);

    @Query(value = "FROM Availability AS a JOIN Car AS c ON a.car.id = c.id WHERE c.numberPlate = ?1 AND a.fromDate <= ?2 AND a.toDate >= ?3 ")
    List<Availability> findByCarNumberPlateFromDateLessThanEqualAndToDateGreaterThanEqual(String numberPlate,
        LocalDateTime fromDate,
        LocalDateTime toDate);

    @Query(value = "FROM Availability AS a JOIN Car AS c ON a.car.id = c.id WHERE c.numberPlate = ?1")
    List<Availability> findByCarNumberPlate(String numberPlate);
}
