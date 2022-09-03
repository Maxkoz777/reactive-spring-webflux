package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.exception.MoviesInfoClientException;
import com.reactivespring.exception.MoviesInfoServerException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

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

        var retrySpec = Retry.fixedDelay(3L, Duration.ofSeconds(1L))
            .filter(MoviesInfoServerException.class::isInstance)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));

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
            .retryWhen(retrySpec)
            .log();

    }

}
