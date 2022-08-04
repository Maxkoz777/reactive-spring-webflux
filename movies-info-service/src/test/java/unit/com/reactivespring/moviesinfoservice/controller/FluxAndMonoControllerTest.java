package com.reactivespring.moviesinfoservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = FluxAndMonoController.class)
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void fluxListSizeTest() {
        webTestClient.get().uri("/flux").exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(Integer.class).hasSize(3);
    }

    @Test
    void fluxListElementsTest() {
        var flux = webTestClient.get().uri("/flux").exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult(Integer.class).getResponseBody();

        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .verifyComplete();
    }

    @Test
    void fluxListExchangeElementsTest() {
        webTestClient.get().uri("/flux").exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(Integer.class)
            .consumeWith(listEntityExchangeResult -> {
                var responseBody = listEntityExchangeResult.getResponseBody();
                assertNotNull(responseBody);
                assertEquals(3, responseBody.size());
            });
    }

}