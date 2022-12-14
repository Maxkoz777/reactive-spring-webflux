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

    /**
     *
     * @param year is a filtering year
     * @return all movie infos with provided year
     */
    Flux<MovieInfo> getAllMovieInfosByYear(Integer year);

    /**
     *
     * @param id - movie info id
     * @return movieInfo by provided id
     */
    Mono<MovieInfo> getMovieInfoById(String id);

    /**
     *
     * @param id - id for movie info
     * @param movieInfoDto - dto to update existing entity
     * @return updated movieInfo
     */
    Mono<MovieInfo> updateMovieInfo(String id, MovieInfoDto movieInfoDto);

    /**
     *
     * @param id - id of entity to remove
     * @return void type
     */
    Mono<Void> deleteMovieInfoById(String id);

    /**
     * deletes all elements from storage
     * @return void type
     */
    Mono<Void> deleteAll();

}
