package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import com.reactivespring.exception.MoviesInfoServerException;
import com.reactivespring.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
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
                log.info("Error status code: {}", clientResponse.statusCode().value());
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
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
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                log.info("Status code is: {}", clientResponse.statusCode().value());
                return clientResponse.bodyToMono(String.class)
                    .flatMap(message -> Mono.error(
                        new MoviesInfoServerException("Server exception in movie-info-service " + message))
                    );
            })
            .bodyToMono(MovieInfo.class)
//            .retry(3L)
            .retryWhen(RetryUtil.retrySpec())
            .log();

    }

    public Flux<MovieInfo> retrieveMovieInfoStream() {

        var url = movieInfoUrl + "/stream";

        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                log.info("Error status code: {}", clientResponse.statusCode().value());
                return clientResponse.bodyToMono(String.class)
                    .flatMap(responseMessage -> Mono.error(
                        new MoviesInfoClientException(
                            responseMessage,
                            clientResponse.statusCode().value()
                        )
                    ));
            })
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                log.info("Status code is: {}", clientResponse.statusCode().value());
                return clientResponse.bodyToMono(String.class)
                    .flatMap(message -> Mono.error(
                        new MoviesInfoServerException("Server exception in movie-info-service " + message))
                    );
            })
            .bodyToFlux(MovieInfo.class)
            .retryWhen(RetryUtil.retrySpec())
            .log();

    }
}
