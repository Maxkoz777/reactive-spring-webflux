package com.reactivespring.moviesinfoservice.repository;

import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
