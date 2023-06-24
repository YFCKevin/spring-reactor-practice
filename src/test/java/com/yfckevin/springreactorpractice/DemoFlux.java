package com.yfckevin.springreactorpractice;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class DemoFlux {

    @Test
    public void createFlux_just(){
        Flux<String> fruitFlux = Flux.just("apple", "orange");

        fruitFlux.subscribe(f -> System.out.println("here is some " + f));
    }
}
