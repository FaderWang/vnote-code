package com.fader.vnote.jvm;

import sun.misc.Unsafe;

import java.io.FileReader;

/**
 * @author FaderW
 * @Date 2021/1/14 10:30
 */
public class ArrayUnsafeSimple {

    private Object[] table;

    public ArrayUnsafeSimple() {
        table = new Object[16];
    }

    public void init() {
        for (int i = 0; i < 16; i++) {
            table[i] = "" + i;
        }
    }

    public void print() {
        Object v;
        for (int i = 0; i < table.length; i++) {
            v = UNSAFE.getObject(table, (i << OSHIFT) + OBASE);
            System.out.println("this value ：" + v);
        }
        v = null;
    }

    public static void main(String[] args) {
        ArrayUnsafeSimple simple = new ArrayUnsafeSimple();
        simple.init();
        simple.print();
    }




    private static final Unsafe UNSAFE;
    private static final long OBASE;
    private static final int OSHIFT;

    static {
        int os;
        try {
            UNSAFE = UnsafeUtils.getUnsafe();
            Class oc = Object[].class;
            // 获取数组第一个元素的offset
            OBASE = UNSAFE.arrayBaseOffset(oc);
            // 获取数组元素的增量地址，索引i就可以如下定位i*os + OBASE
            os = UNSAFE.arrayIndexScale(oc);
            // 另外一种定位数组的方式，使用位移代替乘操作， i << OSHIFT + OBASE
            // 该方法的作用是返回无符号整数i的最高非0位前面的0的个数
            OSHIFT = 31 - Integer.numberOfLeadingZeros(os);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
