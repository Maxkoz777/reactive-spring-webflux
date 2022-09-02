package com.reactivespring.exception.handler;

import com.reactivespring.exception.MoviesInfoClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MoviesInfoClientException.class)
    public ResponseEntity<String> movieInfoClientExceptionHandler(MoviesInfoClientException exception) {
        log.error("Error from movie-info-service: ", exception);
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }

}
