package com.fader.vnote.algorithm;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author FaderW
 * @Date 2020/12/2 14:48
 */
public class BitSetSimple {

    /**
     * BitSet称作位图。可以看作一个布尔数组，但是只使用 1 bit 来存储(0/1 ===> true/false)。节省空间。
     * 比如我们需要存储数值5=true，我们就把二进制中的第5位置为1。一个long类型64位，超过64怎么存储呢。我们可以使用一个long数组
     * 数组下标index/64  下标中第几位index%64
     */


    /**
     * 使用BitSet移除数组中指定元素
     * @param array
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T[] removeElements(final T[] array, final T ... value) {
        if (ArrayUtils.isEmpty(array) || ArrayUtils.isEmpty(value)) {
            return ArrayUtils.clone(array);
        }
        Map<T, MutableInt> mutableIntMap = new HashMap<>();
        Arrays.stream(value).forEach(t -> {
            MutableInt count;
            if ((count = mutableIntMap.get(t)) == null) {
                mutableIntMap.put(t, new MutableInt(1));
            } else {
                count.increment();
            }
        });

        BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            T key = array[i];
            MutableInt count = mutableIntMap.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    mutableIntMap.remove(key);
                }
                toRemove.set(i);
            }
        }

        final Object result = Array.newInstance(array.getClass().getComponentType(), array.length - toRemove.cardinality());
        int set;
        int count;
        int srcIndex = 0;
        int destIndex = 0;
        // 获取从srcIndex开始的第一个true的下标
        while ((set = toRemove.nextSetBit(srcIndex)) != -1) {
            count = set - srcIndex;
            if (count > 0) {
                System.arraycopy(array, srcIndex, result, destIndex, count);
                destIndex += count;
            }
            srcIndex = toRemove.nextClearBit(set);
        }
        count = array.length - srcIndex;
        if (count > 0) {
            System.arraycopy(array, srcIndex, result, destIndex, count);
        }

        return (T[]) result;
    }

    public static void main(String[] args) {
//        BitSet bitSet = new BitSet(2 << 10);
//        Stream.of(1, 5, 14, 26, 46, 78)
//                .forEach(bitSet::set);
//
//        int srcIndex = 0;
//        int set = 0;
//        while ((set = bitSet.nextSetBit(srcIndex)) != -1) {
//            System.out.println("nextBitSet:" + set);
//            srcIndex = bitSet.nextClearBit(set);
//            System.out.println("netSrcIndex:" + srcIndex);


        String[] array = Stream.of("a", "b", "c", "a").collect(Collectors.toList()).toArray(new String[0]);

        String[] removed = removeElements(array, "a", "c", "a", "b");
        Stream.of(removed).forEach(System.out::println);
    }
}
