package com.fader.vnote.concurrent.atomic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author FaderW
 * 普通的原子类都是比较值，不能防止ABA问题
 */
public class AtomicStampedReferenceSimple {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Player {
        private String name;

        private Integer age;
    }

    public static void main(String[] args) {
        /**
         * 定义Pair类，持有reference和stamped两个变量，更新前会比较对象以及stamped值。
         *
         */
        Player player = new Player("FaderW", 26);
        AtomicStampedReference<Player> stampedReference = new AtomicStampedReference<>(player, 0);
        stampedReference.compareAndSet(player, new Player("James", 33), 0, 1);
        System.out.print(stampedReference.get(new int[1]).getAge());

    }
}
