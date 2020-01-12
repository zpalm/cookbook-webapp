package com.zpalm.database;

public class DatabaseOperationException extends Exception {

    public DatabaseOperationException() {
    }

    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Throwable reason) {
        super(message, reason);
    }

    public DatabaseOperationException(Throwable reason) {
        super(reason);
    }
}
