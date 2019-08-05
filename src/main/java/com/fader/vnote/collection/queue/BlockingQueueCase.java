package com.fader.vnote.collection.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author FaderW
 * 2019/3/6
 */

public class BlockingQueueCase {

//    private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();



    static class Consumer extends Thread{


        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String value = queue.take();
                    System.out.println("consumer consume value : " + value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Producer extends Thread {

        private BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                try {
                    queue.put("faderw" + i);
                    System.out.println("producer produce value : faderw" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        Consumer consumer = new Consumer(queue);
        Producer producer = new Producer(queue);

        producer.start();
        consumer.start();
        ExecutorService pool = Executors.newFixedThreadPool(5);
    }

}
