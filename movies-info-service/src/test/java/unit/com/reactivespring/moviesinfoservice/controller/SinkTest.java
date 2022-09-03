package com.reactivespring.moviesinfoservice.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

class SinkTest {

    @Test
    void sinkTest() {

        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> flux1 = replaySink.asFlux();
        Flux<Integer> flux2 = replaySink.asFlux();

        flux1.subscribe(integer -> System.out.println("Flux 1: " + integer));
        flux2.subscribe(integer -> System.out.println("Flux 2: " + integer));

        replaySink.tryEmitNext(3);
    }

}
