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

    @Override
    public Flux<MovieInfo> getAllMovieInfosByYear(Integer year) {
        return movieInfoRepository.findByYear(year).log();
    }

    @Override
    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id).log();
    }

    @Override
    public Mono<MovieInfo> updateMovieInfo(String id, MovieInfoDto movieInfoDto) {
        return movieInfoRepository.findById(id).log()
            .flatMap(movieInfo -> {
                movieInfoMapper.updateMovieInfoFromDto(movieInfoDto, movieInfo);
                return movieInfoRepository.save(movieInfo);
            });
    }

    @Override
    public Mono<Void> deleteMovieInfoById(String id) {
        return movieInfoRepository.deleteById(id).log();
    }

    @Override
    public Mono<Void> deleteAll() {
        return movieInfoRepository.deleteAll().log();
    }
}
