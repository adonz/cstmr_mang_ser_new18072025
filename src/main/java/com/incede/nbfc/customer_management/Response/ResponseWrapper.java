package com.incede.nbfc.customer_management.Response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

 
 


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@JsonPropertyOrder({"code", "status", "data", "message","timestamp"})
public class ResponseWrapper<T> {

    private String status;
    private int code;
  
    
     private T data;
    private String message;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private ResponseWrapper(ResponseStatusCode code, T data, String customMessage, LocalDateTime timestamp) {
        this.status = code.name();
        this.code = code.getCode();
        this.message = customMessage != null ? customMessage : code.getMessage();
        this.data = data;
        this.timestamp = timestamp;
    }


    public static <T> ResponseWrapper<T> success(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.SUCCESS, data, null,LocalDateTime.now());
    }

  

    public static <T> ResponseWrapper<T> created(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.CREATED, data, null,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> badRequest(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.BAD_REQUEST, data, null,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> unauthorized(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.UNAUTHORIZED, data, null,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> forbidden(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.FORBIDDEN, data, null,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> notFound(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.NOT_FOUND, data, null,LocalDateTime.now());
    }


    public static <T> ResponseWrapper<T> error(T data) {
        return new ResponseWrapper<>(ResponseStatusCode.INTERNAL_ERROR, data, null,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.SUCCESS, data, message,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> created(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.CREATED, data, message,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> badRequest(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.BAD_REQUEST, data, message,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> unauthorized(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.UNAUTHORIZED, data, message,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> forbidden(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.FORBIDDEN, data, message,LocalDateTime.now());
    }

    public static <T> ResponseWrapper<T> notFound(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.NOT_FOUND, data, message,LocalDateTime.now());
    }

 
    public static <T> ResponseWrapper<T> failure(String message, T data) {
        return new ResponseWrapper<>(ResponseStatusCode.INTERNAL_ERROR, data, message, LocalDateTime.now());
    }

 
    public static <T> ResponseWrapper<T> error(T data, String message) {
        return new ResponseWrapper<>(ResponseStatusCode.INTERNAL_ERROR, data, message,LocalDateTime.now());
    }
}
 
 