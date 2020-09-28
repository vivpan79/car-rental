package com.infor.carrental.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.infor.carrental.persistence.entity.Availability;
import com.infor.carrental.persistence.entity.Car;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class RestAvailability {

    @JsonIgnore
    private Long id;
    private Car car;

    @DateTimeFormat(pattern = Constants.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fromDate;

    @DateTimeFormat(pattern = Constants.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime toDate;

    private Long pricePerHour;

    public RestAvailability() {
    }

    public RestAvailability(Availability availability) {
        this.id = availability.getId();
        this.car = availability.getCar();
        this.fromDate = availability.getFromDate();
        this.toDate = availability.getToDate();
        this.pricePerHour = availability.getPricePerHour();
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

    public Long getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Long pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
