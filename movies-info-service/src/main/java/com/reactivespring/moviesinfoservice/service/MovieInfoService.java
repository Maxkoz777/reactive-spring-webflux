package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoService {


    /**
     * adds movie info to mongo
     * @param movieInfoDto is a dto instance to add
     * @return added instance
     */
    Mono<MovieInfo> addMovieInfo(MovieInfoDto movieInfoDto);

    /**
     *
     * @return all movie infos from the system
     */
    Flux<MovieInfo> getAllMovieInfos();

}
