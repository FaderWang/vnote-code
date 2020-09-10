package com.fader.vnote.algorithm.practice;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author FaderW
 * 2019/10/14
 */

public class NiuKeMain {

    public static String reverse(String source) {
        if (source.length() < 1 || source.length() > 10000) {
            return null;
        }
        String[] wordArr = source.split(" ");
        LinkedList<String> stack = new LinkedList<>();
        for (int i = 0; i < wordArr.length; i++) {
            stack.addLast(wordArr[i]);
        }
        StringBuilder builder = new StringBuilder();
        while (!stack.isEmpty()) {
            String word = stack.pollLast();
            builder.append(word).append(" ");
        }

        return builder.toString().trim();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String source = scanner.nextLine();
        System.out.println(reverse(source));
    }
}
