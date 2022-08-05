package com.reactivespring.moviesinfoservice.service.impl;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieInfoServiceImpl implements MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;
    
    @Override
    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    public void addMovieInfo(MovieInfo movieInfo) {
        movieInfoRepository.save(movieInfo);
    }
}
