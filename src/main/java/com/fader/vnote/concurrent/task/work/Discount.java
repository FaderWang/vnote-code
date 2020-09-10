package com.fader.vnote.concurrent.task.work;

import java.util.Random;

/**
 * @author FaderW
 * 2019/11/12
 */

public class Discount {

    private static final Random RANDOM = new Random();

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    private static void delay() {
        int delay = 500 + RANDOM.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static double apply(double price, Code code) {
        delay();
        return format(price * (100 - code.percentage) / 100);
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                apply(quote.getPrice(), quote.getCode());
    }

    private static double format(double price) {
        return Double.parseDouble(String.format("%.2f", price));
    }

}
