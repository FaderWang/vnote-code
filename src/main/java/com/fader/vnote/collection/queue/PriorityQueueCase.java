package com.fader.vnote.collection.queue;

import java.util.PriorityQueue;

/**
 * @author FaderW
 * 2019/3/7
 */

public class PriorityQueueCase {

    static class Student implements Comparable<Student> {

        private String name;
        private int priority;

        public Student(String name, int pripority) {
            this.name = name;
            this.priority = pripority;
        }

        public String getName() {
            return this.name;
        }

        public int getPriority() {
            return priority;
        }

        public int compareTo(Student o) {
            return Integer.compare(this.priority, o.getPriority());
        }
    }

    public static void main(String[] args) {
        PriorityQueue<Student> queue = new PriorityQueue<>();
        Student s1 = new Student("kobe", 10);
        Student s2 = new Student("james", 5);
        Student s3 = new Student("curry", 15);

        queue.add(s1);
        queue.add(s2);
        queue.add(s3);

        for (int i = 0; queue.size() > 0; i ++) {
            System.out.println(queue.poll().getName());
        }

    }
}
