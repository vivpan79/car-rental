package com.infor.carrental.controller.model;

import java.util.List;

public class RestCars {

    private List<RestCar> restCars;

    public RestCars() {
    }

    public RestCars(List<RestCar> restCars) {
        this.restCars = restCars;
    }

    public List<RestCar> getRestCars() {
        return restCars;
    }

    public void setRestCars(List<RestCar> restCars) {
        this.restCars = restCars;
    }
}
