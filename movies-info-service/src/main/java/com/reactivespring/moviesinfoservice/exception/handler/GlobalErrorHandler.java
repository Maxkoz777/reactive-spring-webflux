package com.reactivespring.moviesinfoservice.exception.handler;

import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleValidationExceptions(WebExchangeBindException exception) {
        log.error("Exception while validation : {}", exception.getMessage(), exception);
        var errors = exception.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .filter(Objects::nonNull)
            .sorted()
            .collect(Collectors.joining(", "));
        log.error("All errors: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

}
