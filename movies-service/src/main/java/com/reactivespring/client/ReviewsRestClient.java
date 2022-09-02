package com.reactivespring.client;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.MoviesInfoClientException;
import com.reactivespring.exception.MoviesInfoServerException;
import com.reactivespring.exception.ReviewsClientException;
import com.reactivespring.exception.ReviewsServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewsRestClient {

    private final WebClient webClient;

    @Value("${restClient.reviewUrl}")
    private String reviewsUrl;

    public Flux<Review> getReviews(String movieId) {

        log.info("Retrieving review by id: {}", movieId);

        var url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
            .queryParam("movieInfoId", movieId)
            .buildAndExpand().toUriString();

        return webClient.get()
            .uri(url)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                log.info("Error status code: {}", clientResponse.statusCode().value());
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                    return Mono.empty();
                } else {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(responseMessage -> Mono.error(
                            new ReviewsClientException(responseMessage)
                        ));
                }
            })
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                log.info("Status code is: {}", clientResponse.statusCode().value());
                return clientResponse.bodyToMono(String.class)
                    .flatMap(message -> Mono.error(
                        new ReviewsServerException("Server exception in movie-review-service " + message))
                    );
            })
            .bodyToFlux(Review.class);

    }

}
