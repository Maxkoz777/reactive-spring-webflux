package com.learnreactiveprogramming.service;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        // This flux can be created from DB or remote service call
        return Flux.fromIterable(List.of("Max", "Alex", "Nick")).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Max").log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("Max", "Alex", "Nick")).map(String::toUpperCase).log();
    }


    public Flux<String> namesFluxImmutability() {
        var flux = Flux.fromIterable(List.of("Max", "Alex", "Nick")).log();
        // new flux is created, elements are not upper case in returning flux
        flux.map(String::toUpperCase);
        return flux;
    }

    public Flux<String> namesFluxFilter() {
        return Flux.fromIterable(List.of("Max", "Alex", "Nick")).filter(name -> name.length() < 4).log();
    }

    public Flux<String> namesFluxFlatMap() {
        return Flux.fromIterable(List.of("Max", "Alex", "Nick"))
            .filter(name -> name.length() < 4)
            .flatMap(name -> {
                var chars = name.split("");
                return Flux.fromArray(chars);
            })
            .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
            .subscribe(name -> System.out.println("Name is " + name ));
        fluxAndMonoGeneratorService.nameMono()
            .subscribe(name -> System.out.println("Mono name is " + name ));

    }

}
