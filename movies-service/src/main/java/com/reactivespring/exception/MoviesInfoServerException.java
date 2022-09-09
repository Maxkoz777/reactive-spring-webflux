package com.reactivespring.exception;

public class MoviesInfoServerException extends RuntimeException{

    public MoviesInfoServerException(String message) {
        super(message);
    }
}
