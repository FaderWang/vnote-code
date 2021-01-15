package com.fader.vnote.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author FaderW
 * @Date 2021/1/14 10:29
 */
public class UnsafeUtils {

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
