package com.fader.vnote.concurrent.container;

import org.hamcrest.core.Is;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author FaderW
 * @Date 2021/1/18 19:13
 */
public class DelayQueueSimple {

    static class DelayPrint implements Delayed {
        private long time;

        public DelayPrint(long time) {
            this.time = time;
        }

        public void print() {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
            System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        }

        public long now() {
            return LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        }

        static long toLong(LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - now(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (o == this) {
                return 0;
            }

            long diff = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
            if (diff > 0) {
                return 1;
            } else if (diff == 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    static void useCase() {
        DelayQueue<DelayPrint> delayQueue = new DelayQueue<>();
        LocalDateTime now = LocalDateTime.now();
        delayQueue.add(new DelayPrint(DelayPrint.toLong(now.plusSeconds(10))));
        delayQueue.add(new DelayPrint(DelayPrint.toLong(now.plusSeconds(20))));

        new Thread(() -> {
            while (true) {
                try {
                    DelayPrint delayPrint = delayQueue.take();
                    delayPrint.print();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        useCase();
    }
}
