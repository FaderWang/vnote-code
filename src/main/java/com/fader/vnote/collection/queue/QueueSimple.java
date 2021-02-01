package com.fader.vnote.collection.queue;

import java.util.PriorityQueue;

/**
 * @author FaderW
 * @Date 2021/2/1 17:53
 */
public class QueueSimple {

    public static void arrayDequeTest() {

    }

    public static void priorityQueueTest() {
        PriorityQueue<PriorityQueueCase.Student> queue = new PriorityQueue<>();
        PriorityQueueCase.Student s1 = new PriorityQueueCase.Student("kobe", 10);
        PriorityQueueCase.Student s2 = new PriorityQueueCase.Student("james", 5);
        PriorityQueueCase.Student s3 = new PriorityQueueCase.Student("curry", 15);

        queue.add(s1);
        queue.add(s2);
        queue.add(s3);

        for (int i = 0; queue.size() > 0; i ++) {
            System.out.println(queue.poll().getName());
        }
    }

    static class Student implements Comparable<Student> {

        private String name;
        private int priority;

        public Student(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return this.name;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Student o) {
            return Integer.compare(this.priority, o.getPriority());
        }
    }

    public static void main(String[] args) {
        priorityQueueTest();
    }
}
