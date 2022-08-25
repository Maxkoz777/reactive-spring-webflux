package com.reactivespring.controller;

import com.reactivespring.client.MoviesInfoRestClient;
import com.reactivespring.client.ReviewsRestClient;
import com.reactivespring.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movie")
public class MovieController {

    private final MoviesInfoRestClient moviesInfoRestClient;
    private final ReviewsRestClient reviewsRestClient;

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable String id) {
        return moviesInfoRestClient.retrieveMovieInfo(id)
            .flatMap(movieInfo -> {
                var reviewsMono = reviewsRestClient.getReviews(id).collectList();
                return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
            });
    }

}
