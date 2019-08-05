package com.fader.vnote.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FaderW
 * 2019/3/12
 */

public class AtomicCase {

    private AtomicInteger count = new AtomicInteger();

    public void add() {
        int i = count.incrementAndGet();
        System.out.println("current num : " + i);
    }

}
