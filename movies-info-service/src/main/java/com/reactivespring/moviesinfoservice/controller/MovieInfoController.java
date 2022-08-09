package com.reactivespring.moviesinfoservice.controller;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movieInfo")
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfoDto movieInfoDto) {
        return movieInfoService.addMovieInfo(movieInfoDto);
    }

    @GetMapping
    public Flux<MovieInfo> getAllMovieInfos() {
        return movieInfoService.getAllMovieInfos();
    }

    @GetMapping("/{id}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id) {
        return movieInfoService.getMovieInfoById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoForId(
        @RequestBody @Valid MovieInfoDto movieInfoDto,
        @PathVariable String id)
    {
        return movieInfoService.updateMovieInfo(id, movieInfoDto)
            .map(ResponseEntity.ok()::body)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
            .log();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovieInfoForId(@PathVariable String id) {
        return movieInfoService.deleteMovieInfoById(id);
    }

    @DeleteMapping
    public Mono<Void> deleteAllMovieInfos() {
        return movieInfoService.deleteAll();
    }

}
