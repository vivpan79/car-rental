package com.infor.carrental.persistence.entity;

import static java.lang.Boolean.FALSE;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("N")
@Entity
public class Customer extends User {

    @Override
    public Boolean isAdmin() {
        return FALSE;
    }
}
