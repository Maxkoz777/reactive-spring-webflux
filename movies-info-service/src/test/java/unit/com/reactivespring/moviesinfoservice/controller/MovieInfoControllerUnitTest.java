package com.reactivespring.moviesinfoservice.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.reactivespring.moviesinfoservice.domain.dto.MovieInfoDto;
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
import reactor.core.publisher.Mono;
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

    @Test
    void addMovieInfoValidationFailed() {
        var movieInfo = new MovieInfoDto("Batman Begins1", null, List.of("Christian Bale", "Michael Cane"),
                                         LocalDate.parse("2005-06-15"));

        webTestClient.post()
            .uri(MOVIES_INFO_URL)
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void getById() {
        var movieInfo = new MovieInfo("id", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
                                      LocalDate.parse("2012-07-20"));

        Mockito.when(movieInfoService.getMovieInfoById(Mockito.isA(String.class))).thenReturn(Mono.just(movieInfo));

        webTestClient.get()
            .uri(MOVIES_INFO_URL + "/{id}", "id")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(MovieInfo.class)
            .consumeWith(movieInfoEntityExchangeResult -> {
                var result = movieInfoEntityExchangeResult.getResponseBody();
                assertNotNull(result);
                assertEquals("Dark Knight Rises", result.getName());
            });
    }

    @Test
    void update() {
        var movieInfoDto = new MovieInfoDto("name", 2005, List.of("Christian Bale", "Michael Cane"),
                                            LocalDate.parse("2005-06-15"));
        var movieInfo = new MovieInfo("id", "name", 2012, List.of("Christian Bale", "Tom Hardy"),
                                      LocalDate.parse("2012-07-20"));

        Mockito.when(movieInfoService.updateMovieInfo(Mockito.isA(String.class), Mockito.isA(MovieInfoDto.class)))
                .thenReturn(Mono.just(movieInfo));

        webTestClient.put()
            .uri(MOVIES_INFO_URL + "/{id}", "id")
            .bodyValue(movieInfoDto)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(MovieInfo.class)
            .consumeWith(movieInfoEntityExchangeResult -> {
                var result = movieInfoEntityExchangeResult.getResponseBody();
                assertNotNull(result);
                assertEquals("id", result.getMovieInfoId());
                assertEquals("name", result.getName());
            });
    }

    @Test
    void updateValidationFailed() {
        var movieInfoDto = new MovieInfoDto("name", 2005, List.of(),
                                            LocalDate.parse("2005-06-15"));
        webTestClient.put()
            .uri(MOVIES_INFO_URL + "/{id}", "id")
            .bodyValue(movieInfoDto)
            .exchange()
            .expectStatus().isBadRequest();
    }


    @Test
    void deleteById() {
        var movieInfoList = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo(null, "The Dark Knight", 2005, List.of("Christian Bale", "Heath Ledger"),
                          LocalDate.parse("2008-07-18"))
        );

        Mockito.when(movieInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfoList));
        Mockito.when(movieInfoService.deleteMovieInfoById(Mockito.isA(String.class))).thenReturn(Mono.empty());

        webTestClient.delete()
            .uri(MOVIES_INFO_URL + "/{id}", "id")
            .exchange()
            .expectStatus().is2xxSuccessful();
        webTestClient.get()
            .uri(MOVIES_INFO_URL)
            .exchange()
            .expectBodyList(MovieInfo.class)
            .hasSize(2);
    }

    @Test
    void deleteAll() {
        List<MovieInfo> movieInfos = List.of();

        Mockito.when(movieInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));
        Mockito.when(movieInfoService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete()
            .uri(MOVIES_INFO_URL)
            .exchange()
            .expectStatus().is2xxSuccessful();
        webTestClient.get()
            .uri(MOVIES_INFO_URL)
            .exchange()
            .expectBodyList(MovieInfo.class)
            .hasSize(0);
    }

}
