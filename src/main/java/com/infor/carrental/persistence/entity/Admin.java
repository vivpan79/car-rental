package com.infor.carrental.persistence.entity;

import static java.lang.Boolean.TRUE;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("Y")
@Entity
public class Admin extends User {

    @Override
    public Boolean isAdmin() {
        return TRUE;
    }
}
