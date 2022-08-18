package com.reactivespring.exception.handler;

import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.exception.ReviewNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception message: {}", ex.getMessage(), ex);
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());
        HttpStatus statusCode;
        if (ex instanceof ReviewDataException) {
            statusCode = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ReviewNotFoundException) {
            statusCode = HttpStatus.NOT_FOUND;
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        exchange.getResponse().setStatusCode(statusCode);
        return exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
}
