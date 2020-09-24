package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Availability;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    Optional<Availability> findOptionalByFromDateGreaterThanOrToDateLessThan(LocalDateTime fromDate,
        LocalDateTime toDate);

    Optional<Availability> findOptionalByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDateTime fromDate,
        LocalDateTime toDate);
}
