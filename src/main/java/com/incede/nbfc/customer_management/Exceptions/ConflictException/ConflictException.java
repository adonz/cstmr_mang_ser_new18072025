package com.incede.nbfc.customer_management.Exceptions.ConflictException;

public class ConflictException extends RuntimeException {

    private final String errorCode;

    public ConflictException() {
        super();
        this.errorCode = "";
    }

    public ConflictException(String message) {
        super(message);
        this.errorCode = "";
    }

    public ConflictException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
