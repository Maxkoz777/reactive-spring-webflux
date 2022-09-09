package com.reactivespring.exception;

public class MoviesInfoClientException extends RuntimeException{
    private final String message;
    private final Integer statusCode;

    public MoviesInfoClientException(String message, Integer statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

}
