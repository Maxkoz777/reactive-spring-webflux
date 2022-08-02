package com.learnreactiveprogramming.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        // given

        // when
        var flux = fluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(flux)
//            .expectNext("Max", "Alex", "Nick")
//            .expectNextCount(3)
            .expectNext("Max")
            .expectNextCount(2)
            .verifyComplete();

    }

    @Test
    void namesFluxMap() {
        // given

        // when
        var flux = fluxAndMonoGeneratorService.namesFluxMap();

        // then
        StepVerifier.create(flux)
            .expectNext("MAX", "ALEX", "NICK")
            .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        // given

        // when
        var flux = fluxAndMonoGeneratorService.namesFluxImmutability();

        // then
        StepVerifier.create(flux)
            .expectNext("Max", "Alex", "Nick")
            .verifyComplete();
    }

    @Test
    void namesFluxFilter() {
        // given

        // when
        var flux = fluxAndMonoGeneratorService.namesFluxFilter();

        // then
        StepVerifier.create(flux)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        // when
        var flux = fluxAndMonoGeneratorService.namesFluxFlatMap();

        // then
        StepVerifier.create(flux)
            .expectNext("M", "a", "x")
            .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        // when
        var value = fluxAndMonoGeneratorService.namesMonoFlatMap();

        // then
        StepVerifier.create(value)
            .expectNext(List.of("M", "a", "x"))
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMapMany() {
        // when
        var value = fluxAndMonoGeneratorService.namesFluxFlatMapMany();

        // then
        StepVerifier.create(value)
            .expectNext("M", "a", "x")
            .verifyComplete();
    }

    @Test
    void nameMono() {
    }

    @Test
    void main() {
    }
}