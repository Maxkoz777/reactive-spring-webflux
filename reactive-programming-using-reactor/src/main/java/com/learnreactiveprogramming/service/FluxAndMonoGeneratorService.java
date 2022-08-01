package com.learnreactiveprogramming.service;

import java.util.List;
import reactor.core.publisher.Flux;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        // This flux can be created from DB or remote service call
        return Flux.fromIterable(List.of("Max", "Alex", "Nick"));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
            .subscribe(name -> System.out.println("Name is " + name ));

    }

}
