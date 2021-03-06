package com.infor.carrental.persistence.repository;

import com.infor.carrental.persistence.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserName(String userName);
}
