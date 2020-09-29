package com.infor.carrental.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.infor.carrental.persistence.entity.Booking;
import com.infor.carrental.persistence.entity.Car;
import com.infor.carrental.persistence.entity.Customer;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class RestBooking {

    @JsonIgnore
    private Long id;
    private Car car;
    private Customer customer;

    @DateTimeFormat(pattern = Constants.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fromDate;

    @DateTimeFormat(pattern = Constants.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime toDate;

    public RestBooking() {
    }

    public RestBooking(Booking booking) {
        this.id = booking.getId();
        this.car = booking.getCar();
        this.customer = booking.getCustomer();
        this.fromDate = booking.getFromDate();
        this.toDate = booking.getToDate();
    }

    public Long getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
