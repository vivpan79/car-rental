package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Booking;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByFromDateGreaterThanOrToDateLessThan(LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "FROM Booking AS b JOIN Car AS c ON b.car.id = c.id WHERE c.numberPlate = ?1")
    List<Booking> findByCarNumberPlate(String carNumberPlate);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query(value = "FROM Booking AS b JOIN Car AS c ON b.car.id = c.id WHERE c.numberPlate = ?1 AND b.fromDate >= ?2 AND b.toDate <= ?3 ")
    List<Booking> findByCarNumberPlateFromDateGreaterThanOrToDateLessThan(String carNumberPlate, LocalDateTime fromDate, LocalDateTime toDate);
}
