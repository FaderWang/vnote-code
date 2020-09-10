package com.fader.vnote.concurrent.base;

/**
 * @author FaderW
 * 2019/8/7
 */

public class ThreadSimple {

    static void threadGroup() {
        Runnable runnable = () -> {
          System.out.println("线程组名称：" + Thread.currentThread().getThreadGroup());
          System.out.println("线程名称：" + Thread.currentThread().getName());
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        ThreadGroup group = new ThreadGroup("user");
        group.setMaxPriority(1);
        Thread task1 = new Thread(group, runnable, "user-task1");
        Thread task2 = new Thread(group, runnable, "user-task2");
        task1.start();
        task2.start();
        group.list();

    }

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
//        ThreadSimple simple = new ThreadSimple();
//        simple.run();
        threadGroup();
    }
}
