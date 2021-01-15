package com.fader.vnote.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FaderW
 * @Date 2021/1/15 13:45
 * 交替打印数字 字母
 */
public class PrintAlphanumeric {

    private WordEnum wordEnum;

    enum WordEnum {
        NUM,
        ALPHA,
        ;
    }

    public PrintAlphanumeric(WordEnum wordEnum) {
        this.wordEnum = wordEnum;
    }

    private final Object monitor = new Object();

    public void print(WordEnum wordEnum) {
        for (int i = 0; i < 26; i++) {
            synchronized (monitor) {
                try {
                    switch (wordEnum) {
                        case NUM:
                            System.out.print((i+1)+"");
                            monitor.notifyAll();
                            monitor.wait();
                            break;
                        case ALPHA:
                            System.out.print((char)('A' + i));
                            monitor.notifyAll();
                            monitor.wait();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void print2() {
        for (int i = 0; i < 26; i++) {
            synchronized (monitor) {
                try {
                    switch (wordEnum) {
                        case NUM:
                            System.out.print((i+1)+"");
                            break;
                        case ALPHA:
                            System.out.print((char)('A' + i));
                    }
                    resetWord();
                    monitor.notifyAll();
                    monitor.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void resetWord() {
        if (wordEnum.equals(WordEnum.NUM)) {
            wordEnum = WordEnum.ALPHA;
        } else {
            wordEnum = WordEnum.NUM;
        }
    }

    public static void main(String[] args) {
        PrintAlphanumeric print = new PrintAlphanumeric(WordEnum.ALPHA);
        ExecutorService pool = Executors.newFixedThreadPool(2);
//        pool.submit(() -> print.print(WordEnum.NUM));
//        pool.submit(() -> print.print(WordEnum.ALPHA));
        pool.submit(print::print2);
        pool.submit(print::print2);
    }
}
