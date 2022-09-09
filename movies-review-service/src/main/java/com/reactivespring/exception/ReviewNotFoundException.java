package com.reactivespring.exception;

public class ReviewNotFoundException extends RuntimeException{

    public ReviewNotFoundException( String message, Throwable ex) {
        super(message, ex);
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
