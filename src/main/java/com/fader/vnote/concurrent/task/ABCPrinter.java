package com.fader.vnote.concurrent.task;

/**
 * @author FaderW
 * 2019/12/1
 */

public class ABCPrinter implements Runnable {

    private int limit;
    private volatile int count;
    private volatile int index;
    private static Object monitor = new Object();
    private static String[] strings = {"A", "B", "C"};

    public ABCPrinter(int limit) {
        this.limit = limit;
    }


    @Override
    public void run() {
        synchronized (monitor) {
            while (count < limit) {
                try {
                    System.out.println(strings[index++]);
                    if (index == 3) {
                        count++;
                        index = 0;
                    }
                    monitor.notifyAll();
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ABCPrinter abcPrinter = new ABCPrinter(5);
        Thread t1 = new Thread(abcPrinter, "t1");
        Thread t2 = new Thread(abcPrinter, "t2");
        Thread t3 = new Thread(abcPrinter, "t3");
        t1.start();
        t2.start();
        t3.start();
    }
}
