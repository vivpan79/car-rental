package com.infor.carrental.controller.model;

import java.util.List;

public class RestAvailabilities {

    private List<RestAvailability> restAvailabilityList;

    public RestAvailabilities() {
    }

    public RestAvailabilities(List<RestAvailability> restAvailabilityList) {
        this.restAvailabilityList = restAvailabilityList;
    }

    public List<RestAvailability> getRestAvailabilityList() {
        return restAvailabilityList;
    }

    public void setRestAvailabilityList(
        List<RestAvailability> restAvailabilityList) {
        this.restAvailabilityList = restAvailabilityList;
    }
}
