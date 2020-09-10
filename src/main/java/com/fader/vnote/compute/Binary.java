package com.fader.vnote.compute;

/**
 * @author FaderW
 * 2019/9/24
 */

public class Binary {

    public static void displacement() {
        /**
         * 有符号右移，第一位为符号位，正数前面补0，负数补1
         * 1000 0001
         * 1111 1110 反码 符号位不变，其它位取反
         * 1111 1111 补码 反码加1
         * 1111 1111 右移一位 高位补1
         * 1000 0001 -1
         */
        System.out.println(-1 >> 1);
        System.out.println(-3 >> 1);

        /**
         * 无符号位移 高位补0，负数补码计算
         * 补码 11111111 11111111 11111111 11111111
         * 右移16位 00000000 00000000 00000000 11111111
         *  2^8-1 = 255
         */
        System.out.println(-1 >>> 24);
        System.out.println(-1 >>> 2);


        /**
         * 左移操作 -7
         * 1000 0111
         * 1111 1001 补码
         *11111 001 + 0（补码） 左移1位
         * 求原码为 1000 1110 = -14
         */
        System.out.println(-7 << 1);
        System.out.println(-7 << 2);
        System.out.println(-7 << 29);
        System.out.println(733183670 << 8);
        System.out.println(-8 >>> 2);


    }

    public static void main(String[] args) {
        displacement();
    }
}
