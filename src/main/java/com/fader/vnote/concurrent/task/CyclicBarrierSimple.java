package com.fader.vnote.concurrent.task;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author FaderW
 * 2019/12/10
 */

public class CyclicBarrierSimple implements Runnable {

    private final Map<String, String> map = new ConcurrentHashMap<>();



    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4, this);

    public void doWork() {
        for (int i = 0; i < 4; i++) {
            String value = i + "";
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    map.put(Thread.currentThread().getName(), value);
                    try {
                        System.out.println(Thread.currentThread().getName() + "计算完成");
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }

        );
        }
    }

    @Override
    public void run() {
        System.out.println("任务计算完成");
        System.out.println(map);
    }

    public static void main(String[] args) {
        CyclicBarrierSimple cyclicBarrierSimple = new CyclicBarrierSimple();
        cyclicBarrierSimple.doWork();
    }


}
