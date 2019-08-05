package com.fader.vnote.tool;

import lombok.Getter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FaderW
 * 2019/4/23
 * 限流 漏桶法
 */
@Getter
public class LeakyBucketLimit {

    private LeakyBucketLimit(Builder builder) {
        this.capacity = builder.capacity;
        this.count = builder.count;
        this.rate = builder.rate;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 漏桶容量
     */
    private int capacity;

    /**
     * 当前容量
     */
    private int count;

    /**
     * 漏桶速率，/ms
     */
    private int rate;

    /**
     * 初始时间戳
     */
    private long timestamp;

    /**
     * 获取静态builder
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 限流
     * @return
     */
    public boolean accquire() {
        doLeaky();
        System.out.println("this count " + count);
        if (count + 1 <= capacity) {
            count++;
            return true;
        }

        return false;
    }

    private void doLeaky() {
        long now = System.currentTimeMillis();
        count =  count - (int) (((now - timestamp) * rate)/1000);
        if (count < 0) {
            count = 0;
        }
    }



    private static class Builder {
        private int capacity;
        private int count;
        private int rate;

        private Builder() {

        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder rate(int rate) {
            this.rate = rate;
            return this;
        }

        public LeakyBucketLimit build() {
            return new LeakyBucketLimit(this);
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        LeakyBucketLimit limit = LeakyBucketLimit.builder().capacity(10).count(10)
//                .rate(5).build();

        AtomicInteger count = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
//            Thread.sleep(20);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.incrementAndGet();
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        System.out.println(count.get());
    }
}
