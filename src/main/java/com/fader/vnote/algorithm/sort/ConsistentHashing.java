package com.fader.vnote.algorithm.sort;

import org.springframework.util.DigestUtils;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author FaderW
 *
 * 用于解决分布式缓存数据分布问题。
 * 在传统的哈希算法下，每条缓存数据落在那个节点是通过哈希算法和服务器节点数量计算出来的，
 * 一旦服务器节点数量发生增加或者介绍，哈希值需要重新计算，此时几乎所有的数据和服务器节点的对应关系也会随之发生变化，进而会造成绝大多数缓存的失效。
 * 一致性哈希算法通过环形结构和虚拟节点的概念，确保了在缓存服务器节点数量发生变化时大部分数据保持原地不动，从而大幅提高了缓存的有效性。
 */
public class ConsistentHashing {

    private final SortedMap<Long, String> map = new TreeMap<>();


    /**
     * 新增节点
     * @param node
     */
    public void addNode(String node) {
//        long hashCode = hash(node);
//        map.put(hashCode, node);
        // 优化，创建多个虚拟节点。虚拟节点的数量可以根据节点数调整。
        for (int i = 0; i < 10; i++) {
            long hashCode = hash(node + i);
            map.put(hashCode, node);
        }
    }

    /**
     * 顺时针寻找
     * @param key
     * @return
     */
    public String getServer(String key) {
        long hashCode = hash(key);
        SortedMap<Long, String> tailMap = map.tailMap(hashCode);
        Long serverKey = tailMap.isEmpty() ? map.firstKey() : tailMap.firstKey();

        return map.get(serverKey);
    }

    /**
     * hash函数
     * @param key
     * @return
     */
    private long hash(String key) {
        String md5HexKey = DigestUtils.md5DigestAsHex(key.getBytes());
        return Long.parseLong(md5HexKey.substring(0, 15), 16) & ((long) Math.pow(2, 32) - 1);
    }


    public static void main(String[] args) {
        System.out.print(Long.parseLong(DigestUtils.md5DigestAsHex("hello".getBytes()).substring(0, 15), 16));
    }
}
