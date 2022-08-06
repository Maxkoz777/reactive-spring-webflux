package com.reactivespring.moviesinfoservice.service.impl;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import com.reactivespring.moviesinfoservice.domain.mapper.MovieInfoMapper;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieInfoServiceImpl implements MovieInfoService {

    private final MovieInfoRepository movieInfoRepository;
    private final MovieInfoMapper movieInfoMapper;

    @Override
    public Mono<MovieInfo> addMovieInfo(MovieInfoDto movieInfoDto) {
        var movieInfo = movieInfoMapper.movieInfoDtoToMovieInfo(movieInfoDto);
        return movieInfoRepository.save(movieInfo).log();
    }

    @Override
    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoRepository.findAll().log();
    }
}
