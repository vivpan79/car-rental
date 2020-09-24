package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Booking;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByFromDateGreaterThanOrToDateLessThan(LocalDateTime fromDate, LocalDateTime toDate);
}
