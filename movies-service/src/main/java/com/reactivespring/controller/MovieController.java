package com.reactivespring.controller;

import com.reactivespring.domain.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable String id) {
        return null;
    }

}
