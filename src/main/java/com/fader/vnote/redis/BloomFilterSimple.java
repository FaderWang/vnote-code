package com.fader.vnote.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @author FaderW
 * 布隆过滤器，可用于缓存穿透
 */
public class BloomFilterSimple {

    private static final int total = 1000000;

    private static final BloomFilter<Integer> BLOOM_FILTER = BloomFilter.create(Funnels.integerFunnel(), total);

    public static void main(String[] args) {
        for (int i = 0; i < total; i++) {
            BLOOM_FILTER.put(i);
        }

        // 如果存在一定判断存在
        for (int i = 0; i < total; i++) {
            if (!BLOOM_FILTER.mightContain(i)) {
                System.out.print("有坏人逃脱了！");
            }
        }

        int count = 0;
        // 不存在有一定的几率返回存在，因为hash函数存在冲突
        for (int i = total; i < total + 10000; i++) {
            if (BLOOM_FILTER.mightContain(i)) {
                count+=1;
            }
        }
        System.out.print("误伤的概率：" + count);
        // https://github.com/AobingJava/JavaFamily/blob/master/docs/redis/%E5%B8%83%E9%9A%86%E8%BF%87%E6%BB%A4%E5%99%A8(BloomFilter).md
    }
}
