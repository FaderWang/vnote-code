package com.fader.vnote.concurrent.task.work;

import lombok.Getter;

import java.util.Random;

/**
 * @author FaderW
 * 2019/11/12
 */

public class Shop {

    private static final Random RANDOM = new Random();

    @Getter
    private final String name;

    public Shop(String name) {
        this.name = name;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[RANDOM.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    private double calculatePrice(String product) {
        delay();
        return RANDOM.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    private static void delay() {
        int delay = 500 + RANDOM.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
