package com.reactivespring.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;

import com.reactivespring.domain.Review;
import com.reactivespring.domain.mapper.ReviewMapper;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ReviewHandler.class, ReviewRouter.class})
@AutoConfigureWebTestClient
public class ReviewsUnitTests {

    private final String MOVIES_REVIEW_URL = "/v1/review";

    @MockBean
    private ReviewReactiveRepository reactiveRepository;

    @MockBean
    private ReviewMapper reviewMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void addReview() {
        var reviewDto = new Review(null, 1L, "Wow!!!", 9.0);

        var review = new Review("createdId", 1L, "Wow!!!", 9.0);

        Mockito.when(reactiveRepository.save(isA(Review.class))).thenReturn(Mono.just(review));

        webTestClient.post()
            .uri(MOVIES_REVIEW_URL)
            .bodyValue(reviewDto)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Review.class)
            .consumeWith(movieInfoEntityExchangeResult -> {
                var result = movieInfoEntityExchangeResult.getResponseBody();
                assertNotNull(result);
                assertEquals("createdId", result.getReviewId());
            });
    }

    /**
     * Whole test cases are missing for the same reason as integration tests (((
     */

}
