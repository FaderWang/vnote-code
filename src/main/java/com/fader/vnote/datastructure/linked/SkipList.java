package com.fader.vnote.datastructure.linked;

import java.util.stream.IntStream;

/**
 * @author FaderW
 * 跳跃表实现
 */
public class SkipList {

    private static final float SKIP_LIST_P = 0.5f;
    private static final int MAX_LEVEL = 16;

    private Node head = new Node(MAX_LEVEL);

    /**
     * 当前最高层数
     */
    private int levelCount = 1;

    public Node find(int value) {
        Node p = head;
        for (int i = levelCount - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
            // p.forwards[i] == null or p.forwards[i].data >= value
            // then down
        }

        // 最后都会到最底层
        if (p.forwards[0] != null && p.forwards[0].data == value) {
            return p.forwards[0];
        } else {
            return null;
        }
    }

    public void insert(int value) {
        int level = head.forwards[0] == null ? 1 : randomLevel();
        // 每次最多增加一层
        if (level > levelCount) {
            levelCount++;
        }
        Node newNode = new Node(level);
        newNode.data = value;

        Node p = head;
        for (int i = level - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                // 找到前一个节点
                p = p.forwards[i];
            }
            // 当前遍历的层数高于新生成的level,跳过
            if (i <= level) {
                newNode.forwards[i] = p.forwards[i];
                p.forwards[i] = newNode;
            }
        }
    }

    private void delete(int value) {
        Node[] update = new Node[levelCount];
        Node p = head;
        for (int i = levelCount - 1; i >= 0; i--) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
            update[i] = p;
        }

        Node oldNode;
        for (int i = 0; i < levelCount - 1; i--) {
            if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
                oldNode = update[i].forwards[i];
                update[i].forwards[i] = update[i].forwards[i].forwards[i];
                oldNode.forwards[i] = null;
            }
        }

        // 更新levelCount
        while (levelCount > 1 && head.forwards[levelCount] == null) {
            levelCount--;
        }
    }

    public void printAll() {
        for(int i = levelCount - 1; i >= 0; i--) {
            Node p = head;
            while (p != null && p.forwards[i] != null) {
                System.out.print(p.data + "->");
                p = p.forwards[i];
            }
            System.out.println();
        }


    }

    /**
     * 随机生成层数
     * @return
     */
    private int randomLevel() {
        int level = 1;
        while (Math.random() < SKIP_LIST_P && level < MAX_LEVEL) {
            level += 1;
        }
        return level;
    }

    static class Node {
        int data = -1;

        // 每个节点有维护forwards指针数组，数组长度为该节点的level
        Node[] forwards;

        Node(int level) {
            forwards = new Node[level];
        }
    }



    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        IntStream.range(1, 50).forEach(i -> {
            skipList.insert(i);
//            System.out.print(i + ",");
        });

        skipList.printAll();
    }
}
