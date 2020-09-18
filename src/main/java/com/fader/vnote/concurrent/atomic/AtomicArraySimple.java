package com.fader.vnote.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArraySimple {

    public static void main(String[] args) {
        /**
         * 原子的更新数组中某个下标的值
         * 原理跟之前的类似，通过下标计算当前下标在数组对象中的偏移量，然后CAS更新。
         */
        int[] array = {1,2,3,4,5};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(array);

        int value = atomicIntegerArray.incrementAndGet(2);
        System.out.print(atomicIntegerArray.get(2));
    }
}
