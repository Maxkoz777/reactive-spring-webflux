package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MoviesInfoRestClient {

    private final WebClient webClient;

    @Value("${restClient.movieInfoUrl}")
    private String movieInfoUrl;

    public Mono<MovieInfo> retrieveMovieInfo(String id) {

        var url = movieInfoUrl.concat("/{id}");

        return webClient.get()
            .uri(url, id)
            .retrieve()
            .bodyToMono(MovieInfo.class).log();

    }

}
