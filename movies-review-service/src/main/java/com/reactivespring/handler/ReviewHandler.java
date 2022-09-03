package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.domain.mapper.ReviewMapper;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.repository.ReviewReactiveRepository;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final Validator validator;

    private final ReviewReactiveRepository repository;

    private final ReviewMapper mapper;

    private final Sinks.Many<Review> reviewSink = Sinks.many().replay().latest();

    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
            .doOnNext(this::validate)
            .flatMap(repository::save)
            .doOnNext(reviewSink::tryEmitNext)
            .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue)
            .log();

    }

    private void validate(Review review) {
        var constraintViolations = validator.validate(review);
        log.info("{} constraint violations found", constraintViolations.size());
        if (!constraintViolations.isEmpty()) {
            var errors = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .collect(Collectors.joining(", "));
            throw new ReviewDataException(errors);
        }
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var movieInfoId = request.queryParam("movieInfoId");
        if (movieInfoId.isPresent()) {
            var movies = repository.findByMovieInfoId(movieInfoId.get());
            return ServerResponse.ok().body(movies, Review.class);
        } else {
            var flux = repository.findAll().log();
            return ServerResponse.ok().body(flux, Review.class);
        }
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {

        String id = request.pathVariable("id");
        Mono<Review> reviewMono = repository.findById(id);
//            .switchIfEmpty(Mono.error(new ReviewNotFoundException("No review found for provided id: " + id)));

        return reviewMono
            .flatMap(review -> request.bodyToMono(Review.class)
                .flatMap(reqReview -> {
                    mapper.updateMovieReviewFromReview(reqReview, review);
                    return repository.save(review);
                })
                .flatMap(saved -> ServerResponse.ok().bodyValue(saved))
            )
            // alternative approach for checking id for entity existence in system
            .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewMono = repository.findById(request.pathVariable("id"));
        return reviewMono.flatMap(repository::delete)
            .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getStream(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(reviewSink.asFlux(), Review.class).log();
    }
}
