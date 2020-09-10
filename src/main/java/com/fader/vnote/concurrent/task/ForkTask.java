package com.fader.vnote.concurrent.task;

import java.util.concurrent.*;

/**
 * @author FaderW
 * 2020/3/1
 */

public class ForkTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = -4728985631829406803L;

    private int start;
    private int end;
    private int mid;

    public ForkTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        try {
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 小于6直接计算
        if (end - start < 6) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            mid = (end + start)/2;
            ForkTask left = new ForkTask(start, mid);
            ForkTask right = new ForkTask(mid + 1, end);
            left.fork();
            right.fork();

            sum = left.join() + right.join();
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Executors.newWorkStealingPool();
        long start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> result = pool.submit(new ForkTask(1, 1000));
        System.out.println(result.get());
        long end = System.currentTimeMillis();
        System.out.println("执行耗时：" + (end - start) + "ms");

        long begin = System.currentTimeMillis();
        int sum = 0;
        for (int i = 0; i <= 1000; i++) {
            Thread.sleep(1L);
            sum += i;
        }
        System.out.println(sum);
        long after = System.currentTimeMillis();
        System.out.println("执行耗时：" + (after - begin) + "ms");
    }
}
