package com.reactivespring.moviesinfoservice.controller;


import com.reactivespring.moviesinfoservice.domain.entity.MovieInfo;
import com.reactivespring.moviesinfoservice.service.MovieInfoService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = MovieInfoController.class)
class MovieInfoControllerUnitTest {

    private final String MOVIES_INFO_URL = "/v1/movieInfo";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoService;

    @Test
    void getAll() {
        var movieInfoList = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo(null, "The Dark Knight", 2005, List.of("Christian Bale", "Heath Ledger"),
                          LocalDate.parse("2008-07-18")),
            new MovieInfo("id", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                          LocalDate.parse("2012-07-20"))
        );

        Mockito.when(movieInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfoList));

        webTestClient.get()
            .uri(MOVIES_INFO_URL)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult(MovieInfo.class)
            .getResponseBody()
            .as(StepVerifier::create)
            .expectNextCount(3)
            .verifyComplete();
    }

}
