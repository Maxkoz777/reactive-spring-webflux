package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.domain.mapper.ReviewMapper;
import com.reactivespring.repository.ReviewReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewReactiveRepository repository;

    private final ReviewMapper mapper;

    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
            .flatMap(repository::save)
            .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
            .log();

    }

    public Mono<ServerResponse> getReviews(ServerRequest ignoredRequest) {
        var flux = repository.findAll().log();
        return ServerResponse.ok().body(flux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {

        return repository.findById(request.pathVariable("id"))
            .flatMap(review -> request.bodyToMono(Review.class)
                .flatMap(reqReview -> {
                    mapper.updateMovieReviewFromReview(reqReview, review);
                    return repository.save(review);
                })
                .flatMap(saved -> ServerResponse.ok().bodyValue(saved))
            );

    }
}
