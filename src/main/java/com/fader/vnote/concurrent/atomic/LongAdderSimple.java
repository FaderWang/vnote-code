package com.fader.vnote.concurrent.atomic;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author FaderW
 */
public class LongAdderSimple {

    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        System.out.print(longAdder.sum());
    }
}
