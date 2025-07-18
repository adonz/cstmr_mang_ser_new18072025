package com.incede.nbfc.customer_management.Exceptions.BadRequestException;

public class BadRequestException extends RuntimeException {

    private final String errorCode;

    public BadRequestException() {
        super();
        this.errorCode = "";
    }

    public BadRequestException(String message) {
        super(message);
        this.errorCode = "";
    }

    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
	
    }

}
