package com.fader.vnote.concurrent.base;

/**
 * @author FaderW
 * 2019/8/8
 */

public class InterruptSimple {


    static class CountTask implements Runnable {

        private Integer count = 1;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    count++;
                    System.out.println(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void interruptTask() throws InterruptedException {
        Thread thread = new Thread(new CountTask());
        thread.start();

        Thread.sleep(4000);
        thread.interrupt();
    }

    public static void main(String[] args) throws InterruptedException {
        interruptTask();
    }
}
