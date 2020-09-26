package com.infor.carrental.exception;

public class AlreadyBookedException extends RuntimeException {

    public AlreadyBookedException(String message) {
        super(message);
    }
}
