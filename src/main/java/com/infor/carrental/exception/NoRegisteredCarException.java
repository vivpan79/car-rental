package com.infor.carrental.exception;

public class NoRegisteredCarException extends RuntimeException {

    public NoRegisteredCarException(String message) {
        super(message);
    }
}
