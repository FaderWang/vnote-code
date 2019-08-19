package com.fader.vnote.concurrent.base;

/**
 * @author FaderW
 * 2019/8/7
 */

public class ThreadSimple {

    class Task1 implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this is task1");
        }
    }

    class Task2 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this is task2");
        }
    }

    public void run() {
        Task1 task1 = new Task1();
        Task2 task2 = new Task2();

        new Thread(task1).start();
        task2.start();
        System.out.println("this is main");
    }

    public static void main(String[] args) {
        ThreadSimple simple = new ThreadSimple();
        simple.run();
    }
}
