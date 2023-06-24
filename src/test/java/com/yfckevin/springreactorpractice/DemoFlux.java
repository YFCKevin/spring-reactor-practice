package com.yfckevin.springreactorpractice;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class DemoFlux {

    @Order(1)
    @DisplayName("just")
    @Test
    public void createAFlux_just(){
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana");

        fruitFlux.subscribe(f -> System.out.println("here is some " + f));

        StepVerifier.create(fruitFlux)
                .expectNext("apple")
                .expectNext("orange")
                .expectNext("banana")
                .verifyComplete();
    }

    @Order(2)
    @DisplayName("fromArray")
    @Test
    public void createAFlux_fromArray(){
        String[] fruits = new String[]{"apple", "orange", "banana"};

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("apple")
                .expectNext("orange")
                .expectNext("banana")
                .verifyComplete();
    }

    @Order(3)
    @Test
    @DisplayName("計數器")
    public void createAFlux_range(){
        // Flux<Integer> range(int start, int count)
        Flux<Integer> intervalFlux =
                Flux.range(1,5);

            StepVerifier.create(intervalFlux)
                    .expectNext(1)
                    .expectNext(2)
                    .expectNext(3)
                    .expectNext(4)
                    .expectNext(5)
                    .verifyComplete();
    }

    @Order(4)
    @Test
    @DisplayName("計數器2")
    public void createAFlux_interval(){
        // Flux<Long> interval(Duration period)
        Flux<Long> intervalFlux =
                Flux.interval(Duration.ofSeconds(1)).take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Order(5)
    @Test
    @DisplayName("merge flux")
    public void mergeFluxes(){
        Flux<String> animalFlux = Flux
                 .just("dog", "cat", "cow")
                 .delayElements(Duration.ofMillis(500));

        Flux<String> fruitFlux = Flux
                .just("apple", "orange", "banana")
                .delayElements(Duration.ofMillis(500))
                .delaySubscription(Duration.ofMillis(250));

        Flux<String> mergeFlux = animalFlux.mergeWith(fruitFlux);

        StepVerifier.create(mergeFlux)
                .expectNext("dog")
                .expectNext("apple")
                .expectNext("cat")
                .expectNext("orange")
                .expectNext("cow")
                .expectNext("banana")
                .verifyComplete();

    }

    @Order(6)
    @Test
    @DisplayName("zip flux")
    public void zipFluxes(){
        Flux<String> animalFlux = Flux
                .just("dog", "cat", "cow");

        Flux<String> fruitFlux = Flux
                .just("apple", "orange", "banana");

        Flux<Tuple2<String, String>> zipFluxes = Flux.zip(animalFlux, fruitFlux);

        StepVerifier.create(zipFluxes)
                .expectNextMatches(p ->
                        p.getT1().equals("dog") &&
                        p.getT2().equals("apple")
                )
                .expectNextMatches(p ->
                        p.getT1().equals("cat") &&
                        p.getT2().equals("orange")
                )
                .expectNextMatches(p ->
                        p.getT1().equals("cow") &&
                        p.getT2().equals("banana")
                ).verifyComplete();
    }

    @Order(7)
    @Test
    @DisplayName("data流經flux時，過濾掉某些值並對其他值處理")
    public void skipFlux(){
        Flux<Integer> countFlux = Flux
                .just(1,2,3,4,5,6).skip(3);

        StepVerifier.create(countFlux)
                .expectNext(4)
                .expectNext(5)
                .expectNext(6)
                .verifyComplete();
    }

    @Order(8)
    @Test
    @DisplayName("filter flux")
    public void filterFlux(){
        Flux<String> spaceFlux = Flux
                .just("kevin", "May", "Chen yoyo").filter(sf -> !sf.contains(" "));

        StepVerifier.create(spaceFlux)
                .expectNext("kevin")
                .expectNext("May")
                .verifyComplete();
    }

    @Order(9)
    @Test
    @DisplayName("transfer map")
    public void map(){
        Flux<Person> peopleFlux = Flux
                .just("Chen Fan", "Wang Wo", "Huang Ken")
                .map(m -> {
                    String[] split = m.split("\\s");
                    return new Person(split[0], split[1]);
                });

        StepVerifier.create(peopleFlux)
                .expectNext(new Person("Chen", "Fan"))
                .expectNext(new Person("Wang", "Wo"))
                .expectNext(new Person("Huang", "Ken"))
                .verifyComplete();
    }

    @Data
    private static class Person {
        private final String firstName;
        private final String lastName;
    }
}
