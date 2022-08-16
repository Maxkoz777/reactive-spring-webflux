package com.reactivespring.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
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
            .nest(path("/v1/review"), builder -> builder
                .POST("", reviewHandler::addReview)
                .GET("", reviewHandler::getReviews)
                .PUT("/{id}", reviewHandler::updateReview)
            )
            .GET("/v1/helloworld", request -> ServerResponse.ok().bodyValue("Hello World!"))
            .build();
    }

}
