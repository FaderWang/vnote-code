package com.fader.vnote.concurrent.atomic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author FaderW
 *
 */

public class AtomicFieldUpdaterSimple {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Player {
        public String name;

        public volatile int age;
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 第一，Updater只能修改它可见范围内的变量。因为Updater使用反射得到这个变量。如果变量不可见，就会出错。比如如果score申明为private，就是不可行的。
         *
         * 第二，为了确保变量被正确的读取，它必须是volatile类型的。如果我们原有代码中未申明这个类型，那么简单得申明一下就行，这不会引起什么问题。
         *
         * 第三，由于CAS操作会通过对象实例中的偏移量直接进行赋值，因此，它不支持static字段（Unsafe. objectFieldOffset()不支持静态变量）。
         */
        AtomicIntegerFieldUpdater<Player> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Player.class, "age");
        Player player = new Player("kikk", 0);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                fieldUpdater.incrementAndGet(player);
            });
        }
        Thread.sleep(3000L);
        pool.shutdown();
        System.out.print(player.getAge());


    }
}
