package com.reactivespring.moviesinfoservice.service;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;

public interface MovieInfoService {


    /**
     * adds movie info to mongo
     * @param movieInfo is a instance to add
     */
    void addMovieInfo(MovieInfo movieInfo);

}
