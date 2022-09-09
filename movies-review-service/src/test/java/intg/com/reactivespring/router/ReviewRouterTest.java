package com.reactivespring.router;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class ReviewRouterTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository reviewReactiveRepository;

    @BeforeEach
    void setUp() {

        var reviewList = List.of(
            new Review(null, "1", "Awesome Movie", 9.0),
            new Review(null, "1", "Awesome Movie1", 9.0),
            new Review(null, "2", "Excellent Movie", 8.0)
        );

        reviewReactiveRepository.saveAll(reviewList).blockLast();

    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void reviewRoute() {
        /**
         * Tests are missing as they will duplicate the majority of code from movies-info-service microservice
         * Even if this absence is quite unforgivable, I need to go further in project creation
         */
    }
}