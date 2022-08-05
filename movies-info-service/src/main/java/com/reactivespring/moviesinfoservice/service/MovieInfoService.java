package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import reactor.core.publisher.Mono;

public interface MovieInfoService {


    /**
     * adds movie info to mongo
     * @param movieInfo is an instance to add
     */
    Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo);

}
