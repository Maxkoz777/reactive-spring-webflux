package com.reactivespring.intg;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reactivespring.domain.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8084)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = {
        "restClient.movieInfoUrl=http://localhost:8084/v1/movieInfo",
        "restClient.reviewUrl=http://localhost:8084/v1/review"
    }
)
class MovieControllerIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void retrieveMovieById() {

        var movieId = "mockId";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/movieInfo/" + movieId))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("movieInfo.json")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/review"))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("reviews.json")));

        webTestClient.get()
            .uri("/v1/movie/{id}", movieId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Movie.class)
            .consumeWith(movieEntityExchangeResult -> {
                var movie = movieEntityExchangeResult.getResponseBody();
                Assertions.assertNotNull(movie, "movie should exist");
                Assertions.assertEquals(2, movie.getReviewList().size(),
                                        "There should be 2 reviews for movie entity");
                Assertions.assertEquals("Batman", movie.getMovieInfo().getName(),
                                        "Movie name should match the predefined name");

            });

    }

    @Test
    void retrieveMovieByIdMovieInfoNotFound() {

        var movieId = "mockId";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/movieInfo/" + movieId))
                             .willReturn(WireMock.aResponse().withStatus(404)));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/review"))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("reviews.json")));

        webTestClient.get()
            .uri("/v1/movie/{id}", movieId)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(String.class)
            .consumeWith(stringEntityExchangeResult -> {
                var message = stringEntityExchangeResult.getResponseBody();
                Assertions.assertEquals("There is no movie Info for provided id: " + movieId, message,
                                        "Actual error message is different from expected");
            });

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/v1/movieInfo/" + movieId)));

    }

    @Test
    void retrieveMovieByIdReviewNotFound() {

        var movieId = "mockId";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/movieInfo/" + movieId))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("movieInfo.json")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/review"))
                             .willReturn(WireMock.aResponse().withStatus(404)));

        webTestClient.get()
            .uri("/v1/movie/{id}", movieId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Movie.class)
            .consumeWith(movieEntityExchangeResult -> {
                var movie = movieEntityExchangeResult.getResponseBody();
                Assertions.assertNotNull(movie, "movie should exist");
                Assertions.assertEquals(0, movie.getReviewList().size(),
                                        "There should be no reviews for movie entity");
                Assertions.assertEquals("Batman", movie.getMovieInfo().getName(),
                                        "Movie name should match the predefined name");

            });

    }

    @Test
    void retrieveMovieById5xxError() {

        var movieId = "mockId";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/movieInfo/" + movieId))
                             .willReturn(WireMock.aResponse().withStatus(500).withBody("MovieInfo Service Unavailable")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/review"))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("reviews.json")));

        webTestClient.get()
            .uri("/v1/movie/{id}", movieId)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(String.class)
            .consumeWith(stringEntityExchangeResult -> {
                var message = stringEntityExchangeResult.getResponseBody();
                Assertions.assertEquals("Server exception in movie-info-service MovieInfo Service Unavailable", message,
                                        "Actual error message is different from expected");
            });

        WireMock.verify(4, WireMock.getRequestedFor(WireMock.urlEqualTo("/v1/movieInfo/" + movieId)));

    }

    @Test
    void retrieveMovieById5xxErrorReviewService() {

        var movieId = "mockId";

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/movieInfo/" + movieId))
                             .willReturn(WireMock.aResponse()
                                             .withHeader("Content-Type", "application/json")
                                             .withBodyFile("movieInfo.json")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/v1/review"))
                             .willReturn(WireMock.aResponse().withStatus(500).withBody("Review Service Unavailable")));

        webTestClient.get()
            .uri("/v1/movie/{id}", movieId)
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(String.class)
            .consumeWith(stringEntityExchangeResult -> {
                var message = stringEntityExchangeResult.getResponseBody();
                Assertions.assertEquals("Server exception in movie-review-service Review Service Unavailable", message,
                                        "Actual error message is different from expected");
            });

        WireMock.verify(4, WireMock.getRequestedFor(WireMock.urlPathMatching("/v1/review")));

    }

}
