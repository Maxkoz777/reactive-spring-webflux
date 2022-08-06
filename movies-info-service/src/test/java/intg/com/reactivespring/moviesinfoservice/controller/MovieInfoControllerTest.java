package com.reactivespring.moviesinfoservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import com.reactivespring.moviesinfoservice.repository.MovieInfoRepository;
import java.time.LocalDate;
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
class MovieInfoControllerTest {

    private final String MOVIES_INFO_URL = "/v1/movieInfo";

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void beforeEach() {
        var movieInfoList = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo(null, "The Dark Knight", 2005, List.of("Christian Bale", "Heath Ledger"),
                          LocalDate.parse("2008-07-18")),
            new MovieInfo("id", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                          LocalDate.parse("2012-07-20"))
        );
        movieInfoRepository.saveAll(movieInfoList).log().blockLast();
    }

    @AfterEach
    void afterEach() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfoDto("Batman Begins1", 2005, List.of("Christian Bale", "Michael Cane"),
                                         LocalDate.parse("2005-06-15"));

        webTestClient.post()
            .uri(MOVIES_INFO_URL)
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(MovieInfo.class)
            .consumeWith(movieInfoEntityExchangeResult -> {
                var result = movieInfoEntityExchangeResult.getResponseBody();
                assertNotNull(result);
                assertEquals("Batman Begins1", result.getName());
            });
    }
}