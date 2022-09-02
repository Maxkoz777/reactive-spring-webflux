package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoviesInfoRestClient {

    private final WebClient webClient;

    @Value("${restClient.movieInfoUrl}")
    private String movieInfoUrl;

    public Mono<MovieInfo> retrieveMovieInfo(String id) {

        var url = movieInfoUrl + "/{id}";

        return webClient.get()
            .uri(url, id)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    log.error("Error status code: {}", clientResponse.statusCode().value());
                    return Mono.error(new MoviesInfoClientException(
                        "There is no movie Info for provided id: " + id,
                        HttpStatus.NOT_FOUND.value()
                    ));
                } else {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(responseMessage -> Mono.error(
                            new MoviesInfoClientException(
                                responseMessage,
                                clientResponse.statusCode().value()
                            )
                        ));
                }
            })
            .bodyToMono(MovieInfo.class).log();

    }

}
