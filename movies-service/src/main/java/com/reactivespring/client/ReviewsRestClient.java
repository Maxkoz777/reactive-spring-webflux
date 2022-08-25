package com.reactivespring.client;

import com.reactivespring.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ReviewsRestClient {

    private final WebClient webClient;

    @Value("${restClient.reviewUrl}")
    private String reviewsUrl;

    public Flux<Review> getReviews(String movieId) {

        var url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
            .queryParam("movieInfoId", movieId)
            .buildAndExpand().toUriString();

        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Review.class);

    }

}
