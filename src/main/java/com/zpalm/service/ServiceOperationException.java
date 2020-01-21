package com.zpalm.service;

public class ServiceOperationException extends Exception {

    public ServiceOperationException() {
    }

    public ServiceOperationException(String message) {
        super(message);
    }

    public ServiceOperationException(String message, Throwable reason) {
        super(message, reason);
    }

    public ServiceOperationException(Throwable reason) {
        super(reason);
    }
}
