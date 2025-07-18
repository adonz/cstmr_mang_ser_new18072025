package com.incede.nbfc.customer_management.Response;

 
public enum ResponseStatusCode implements IStatusCode{
	
    SUCCESS(200, "Operation successful."),
    CREATED(201, "Resource created successfully."),
    BAD_REQUEST(400, "The request is invalid."),
    UNAUTHORIZED(401, "You are not authorized."),
    FORBIDDEN(403, "Access is forbidden."),
    NOT_FOUND(404, "Resource not found."),
    INTERNAL_ERROR(500, "An internal error occurred.");

   private final int code;
   private final String message;


    ResponseStatusCode(int code, String message) {
        this.code = code;
        this.message = message;

    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}

 