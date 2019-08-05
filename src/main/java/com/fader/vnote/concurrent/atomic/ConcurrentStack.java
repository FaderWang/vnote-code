package com.fader.vnote.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author FaderW
 * 2019/3/12
 */

public class ConcurrentStack<V> {

    private final AtomicReference<Node<V>> atomicReference = new AtomicReference<>();

    public void push(V v) {
        Node<V> oldValue = atomicReference.get();
        Node<V> newValue = new Node<>(v, oldValue);
        while (true) {
            if (atomicReference.compareAndSet(oldValue, newValue)) {
                return;
            }
        }
    }

    public V pop() {
        Node<V> oldValue = atomicReference.get();
        Node<V> newValue = oldValue.next;
        while (true) {
            if (atomicReference.compareAndSet(oldValue, newValue)) {
                return oldValue.getObject();
            }
        }
    }

    private static class Node<T> {
        private T Object;
        private Node<T> next;

        public Node(T object, Node<T> next) {
            this.Object = object;
            this.next = next;
        }

        public T getObject() {
            return Object;
        }
    }

    public static void main(String[] args) {
        ConcurrentStack<String> stack = new ConcurrentStack<>();

        stack.push("faderW");
        stack.push("curry");
        stack.push("james");

        for (int i = 0; i < 3; i++) {
            System.out.println(stack.pop());
        }
    }

}
