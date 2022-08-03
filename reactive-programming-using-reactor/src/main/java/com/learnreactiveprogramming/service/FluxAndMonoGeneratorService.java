package com.learnreactiveprogramming.service;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
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

    public Mono<List<String>> namesMonoFlatMap() {
        return Mono.just("Max")
            .flatMap(name -> {
                var letters = name.split("");
                return Mono.just(List.of(letters));
            })
            .log();
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

    public Flux<String> namesFluxTransform() {

        Function<Flux<String>, Flux<String>> commonLogic = element -> element.filter(name -> name.length() < 4)
            .flatMap(name -> {
                var chars = name.split("");
                return Flux.fromArray(chars);
            });

        return Flux.fromIterable(List.of("Max", "Alex", "Nick"))
            .transform(commonLogic)
            .log();
    }

    public Flux<String> namesFluxFlatMapMany() {
        return Mono.just("Max")
            .filter(name -> name.length() < 4)
            .flatMapMany(name -> {
                var chars = name.split("");
                return Flux.fromArray(chars);
            })
            .log();
    }

    public Flux<String> concatExample() {
        var a = Flux.just("a");
        var b = Flux.just("b");
        var ab = Flux.concat(a, b);
        var c = Flux.just("c");
        return ab.concatWith(c);
    }

    public Flux<String> mergeSequentialExample() {
        var a = Flux.just("a");
        var b = Flux.just("b");
        var ab = Flux.concat(a, b).delayElements(Duration.ofMillis(100));
        var cd = Flux.just("c", "d").delayElements(Duration.ofMillis(90));
        return Flux.mergeSequential(ab, cd);
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
            .subscribe(name -> System.out.println("Name is " + name ));
        fluxAndMonoGeneratorService.nameMono()
            .subscribe(name -> System.out.println("Mono name is " + name ));

    }

}
