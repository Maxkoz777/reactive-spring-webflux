package com.reactivespring.moviesinfoservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.reactivespring.moviesinfoservice.domain.MovieInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void beforeEach() {
        var movieInfoList = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
            new MovieInfo(null, "The Dark Knight", 2005, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
            new MovieInfo("id", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );
        movieInfoRepository.saveAll(movieInfoList).log().blockLast();
    }

    @AfterEach
    void afterEach() {
        movieInfoRepository.deleteAll().block();
    }


    @Test
    void findAll() {
        var flux = movieInfoRepository.findAll().log();

        StepVerifier.create(flux)
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    void findById() {
        movieInfoRepository.findById("id")
            .log()
            .doOnNext(movieInfo -> System.out.println(movieInfo.getMovieInfoId()))
            .as(StepVerifier::create)
            .assertNext(movieInfo -> assertEquals("Dark Knight Rises", movieInfo.getName()))
            .verifyComplete();
    }

}