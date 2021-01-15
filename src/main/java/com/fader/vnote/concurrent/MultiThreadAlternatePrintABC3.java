package com.fader.vnote.concurrent;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author FaderW
 * @Date 2021/1/15 14:58
 */
public class MultiThreadAlternatePrintABC3 {

    private Semaphore semaphoreA = new Semaphore(1);
    private Semaphore semaphoreB = new Semaphore(0);
    private Semaphore semaphoreC = new Semaphore(0);

    public void print(String word, Semaphore current, Semaphore next) {
        for (int i = 0; i < 10; i++) {
            try {
                current.acquire();
                System.out.print(word);
                next.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        MultiThreadAlternatePrintABC3 printABC3 = new MultiThreadAlternatePrintABC3();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(() -> printABC3.print("A", printABC3.semaphoreA, printABC3.semaphoreB));
        pool.submit(() -> printABC3.print("B", printABC3.semaphoreB, printABC3.semaphoreC));
        pool.submit(() -> printABC3.print("C", printABC3.semaphoreC, printABC3.semaphoreA));
    }
}
