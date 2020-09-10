package com.fader.vnote.pattern.单例模式;

import java.util.Arrays;

/**
 * DCL双重加锁
 */
public class Singleton {

    /**
     * 这里加volatile不是为了保证可见性，synchronized关键字已经保证了可见性，
     * 这里是为了防止指令重排序。
     * instance = new instance 不是原子的，可分为三步：1.分配空间，2初始化对象，3.链接引用
     * volatile写之前插入StoreStore内存屏障，防止重排序。
     */
    private static volatile Singleton instance;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }


        return instance;
    }

    public static void main(String[] args) {
//        String s = "we are game";
//        System.out.println(s.charAt(2));
//        System.out.println(s.indexOf(" "));
        int[][] arr = new int[1][1];
        System.out.println(arr[0][0]);
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
//        Arrays.sort(strs);
//        String m = strs[0];
//        String n = strs[strs.length-1];
//        int index = 0;
//        StringBuilder builder = new StringBuilder();
//        while (index < m.length() || index < n.length()) {
//            if (m.charAt(index) == n.charAt(index)) {
//                builder.append(m.charAt(index));
//                index++;
//            } else {
//                break;
//            }
//        }
//        if (index == 0) {
//            return "";
//        }
//        return builder.toString();

        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    public static int findLongest(String A, int n, String B, int m) {
        // write code here
        if (n == 0 || m == 0) {
            return 0;
        }
        int maxLen = 0;
        int maxEnd = 0;
        int[][] arr = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (j == 0 || i == 0) {
                        arr[i][j] = 1;
                    } else {
                        arr[i][j] = arr[i-1][j-1] + 1;
                    }
                    if (arr[i][j] > maxLen) {
                        maxLen = arr[i][j];
                        maxEnd = i;
                    }
                }
            }
        }
        return maxLen;
    }
}