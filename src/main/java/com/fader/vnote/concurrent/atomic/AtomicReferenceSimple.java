package com.fader.vnote.concurrent.atomic;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceSimple {

    @Getter
    @Setter
    static class Player {
        private String name;

        private Integer age;
    }

    public static void main(String[] args) {
        Player player = new Player();
        player.setAge(20);
        player.setName("Curry");
        AtomicReference<Player> atomicReference = new AtomicReference<>();
        atomicReference.set(player);

        atomicReference.getAndSet(new Player());
    }
}
