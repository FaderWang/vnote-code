package com.fader.vnote.algorithm;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author FaderW
 * @date 2020/12/2 10:58
 */
public class IntBitMap {

    /**
     * int占4个字节，32为长度，这里每个数值占一BIT
     * 数组最大长度设为Integer.MAX_VALUE/32即可。
     */
    private static final int MAX_INT_SIZE = 67108863;
    private final int[] ints;

    public IntBitMap() {
        ints = new int[MAX_INT_SIZE];
    }

    public IntBitMap(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        ints = new int[size];
    }

    public void add(int i) {
        int r = i / 32;
        int c = i % 32;
        ints[r] = ints[r] | ( 1 << c);
    }

    public void remove(int i) {
        int r = i / 32;
        int c = i % 32;
        ints[r] &= ~(1 << c);
    }

    public boolean contains(int i) {
        int r = i / 32;
        int c = i % 32;
        return ((ints[r] >>> i) & 1) == 1;
    }

    public static void main(String[] args) {
        IntBitMap bitMap = new IntBitMap();
        Stream.of(1, 5, 7, 34, 69).forEach(bitMap::add);
        Stream.of(1, 5, 9, 34, 70).forEach(i -> {
            if (bitMap.contains(i)) {
                System.out.println("int :" + i + " exists");
            }
        });
    }
}
