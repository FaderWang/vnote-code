package com.fader.vnote.javabase;

import lombok.Data;

import java.util.Optional;

/**
 * @author FaderW
 */
public class OptionalSimple {

    @Data
    static class Player {
        private String name;
    }

    public static void nullable() {
        /**
         * flatMap和map的区别在于，map返回的optional是对mapper.apply(value)返回值的包装
         * 而flatMap的value本身就是optional类型的
         */
        Player player = new Player();
        Optional<Player> optionalPlayer = Optional.ofNullable(player);
//        System.out.println(optionalPlayer.map(Player::getName).map(String::toString).orElse("yes"));

        System.out.println(optionalPlayer.map(Player::getName).filter(s -> s.contains("yes")).orElse("yes"));
    }

    public static void main(String[] args) {
        nullable();
    }
}
