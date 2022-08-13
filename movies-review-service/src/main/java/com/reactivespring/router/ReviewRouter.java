package com.reactivespring.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(ReviewHandler reviewHandler) {
        return route()
            .GET("/v1/helloworld", request -> ServerResponse.ok().bodyValue("Hello World!"))
            .POST("/v1/review", reviewHandler::addReview)
            .GET("/v1/review", reviewHandler::getReviews)
            .build();
    }

}
