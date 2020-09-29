package com.infor.carrental.controller.model;

import java.util.List;

public class RestBookings {

    private List<RestBooking> restBookings;

    public RestBookings() {
    }

    public RestBookings(List<RestBooking> restBookings) {
        this.restBookings = restBookings;
    }

    public List<RestBooking> getRestBookings() {
        return restBookings;
    }

    public void setRestBookings(List<RestBooking> restBookings) {
        this.restBookings = restBookings;
    }
}
