package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import reactor.core.publisher.Mono;

public interface MovieInfoService {


    /**
     * adds movie info to mongo
     * @param movieInfoDto is a dto instance to add
     */
    Mono<MovieInfo> addMovieInfo(MovieInfoDto movieInfoDto);

}
