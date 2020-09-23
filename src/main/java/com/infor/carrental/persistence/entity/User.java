package com.infor.carrental.persistence.entity;

import static javax.persistence.DiscriminatorType.CHAR;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = CHAR, name = "admin_privilege")
//Workaround for https://hibernate.atlassian.net/browse/HHH-5798
@DiscriminatorValue("X")
@Entity(name = "users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public abstract Boolean isAdmin();
}
