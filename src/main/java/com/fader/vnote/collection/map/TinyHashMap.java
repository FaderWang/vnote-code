package com.fader.vnote.collection.map;


import java.util.*;

public class TinyHashMap<K,V> extends AbstractMap<K,V> {


    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 定义node类表示键值对数据结构
     */
    static class Node<K,V> implements Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Entry entry = (Entry) o;
                return Objects.equals(entry.getKey(), key) &&
                        Objects.equals(entry.getValue(), value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    /**
     * node array for store data
     */
    transient Node<K,V>[] table;

    transient Set<Entry<K,V>> entrySet;

    /**
     * number of k-v mappings in the map
     */
    transient int size;

    /**
     * trigger resize
     */
    transient int threshold;

    /**
     * 装载因子，loadFactor * 数组长度 = threshold
     */
    transient float loadFactor;

    /**
     * 记录结构改变的次数
     */
    transient int modCount;

    transient Set<K> keySet;



    public TinyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public TinyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public TinyHashMap(int initialCapCity, float loadFactor) {
        if (initialCapCity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapCity);
        }
        if (initialCapCity > MAXIMUM_CAPACITY) {
            initialCapCity = MAXIMUM_CAPACITY;
        }
        if (loadFactor < 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        }
        this.loadFactor = loadFactor;
        // tableSizeFor返回大于initialCapacity的最小2的n次方，并且该容量值存于阀值中
        this.threshold = tableSizeFor(initialCapCity);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Map.Entry<K,V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    @Override
    public V get(Object key) {
        Node<K,V> node;
        return (node = getNode(hash(key), key)) == null ? null : node.value;
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab;
        Node<K,V> first, e;
        int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n-1) & hash]) != null) {
            if (first.hash == hash &&
                    ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }

        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false);
    }

    @Override
    public V remove(Object key) {
        Node<K,V> node;
        // 这里删除元素不需要值匹配
        return (node = removeNode(hash(key), key, null, false, true)) == null ?
                null : node.value;
    }

    public void clear() {
        Node<K,V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; i++) {
                tab[i] = null;
            }
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    public boolean containsValue(Object value) {
        Node<K,V>[] tab; V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    final Node<K,V> removeNode(int hash, Object key, V value,
                               boolean matchValue, boolean movable) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = hash & (n-1)]) != null) {
            Node<K,V> node = null, e; K k; V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k)))) {
                node = p;
            } else if ((e = p.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key ||
                                    (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    // p节点保存e的前置节点
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(v)))) {
                if (node == p) {
                    // node == p说明为header,只要把当前头指向node的后置，即table[index] = node.next
                    table[index] = node.next;
                } else {
                    p.next = node.next;
                    node.next = null;
                }
                ++modCount;
                --size;
                // todo afterNodeRemoval
                return node;
            }
        }
        return null;
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n,i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n-1) & hash])== null) {
            // 当前下标位没有数据，将数据放在该下标位
            tab[i] = newNode(hash, key, value, null);
        } else {
            // 如果有遍历链表，找到key == p.key
            Node<K,V> e;
            K k;
            // 当前下标头部是否是要插入的
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k)))){
                e = p;
            } else {
                // 遍历链表，找到key与之对应的,未找到则插入末尾
                for (int bitCount = 0; ; bitCount++) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        break;
                    }
                    p = e;
                }
            }
            if (e != null) {
                V oldValue = e.value;
                if (oldValue == null || !onlyIfAbsent) {
                    e.value = value;
                }
                return oldValue;
            }
        }
        // 插入新的元素，结构改变一次
        ++modCount;
        if (++size > threshold) {
            resize();
        }
        return null;

    }

    /**
     * hash函数
     * @param key
     * @return
     */
    static final int hash(Object key) {
        int hash;
        // key为null，hash值为0，说明HashMap只能有一个键为null
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    /**
     *
     */
    static final int tableSizeFor(int cap) {

        // 减去1再操作防止cap正好为2的n次幂的情况，这时得到的值是预期的两倍
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;

        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * resize扩容或初始化node数组
     * @return
     */
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTable = table;
        int oldCap = oldTable == null ? 0 : oldTable.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            //扩容
            // 大于最容量不再扩容
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTable;
            } else if ((newCap = oldCap << 2) <= MAXIMUM_CAPACITY &&
                        oldCap >= 16) {
                // 扩容两倍后，如果容量小于默认容量，阀值不变
                newThr = oldThr << 2;
            }

        } else if (oldThr > 0) {
            // 构造时制定了容量，容量值存于threshold中
            newCap = oldThr;
        } else {
            //没有指定initCapacity
            newCap = 1 << 4; // 默认容量16
            newThr = (int) (DEFAULT_LOAD_FACTOR * 16);
        }
        if (newThr == 0) {
            // 计算新的阀值，不能大于最大容量
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }

        threshold = newThr;
        Node<K,V>[] newTab = new Node[newCap];
        table = newTab;
        if (oldTable != null) {
            // todo 数据迁移到新数组
            for (int j = 0; j < oldTable.length; j++) {
                Node<K,V> e;
                if ((e = oldTable[j]) != null) {
                    // node置为null，便于垃圾回收
                    oldTable[j] = null;
                    if (e.next == null) {
                        // 只有一个node，直接重新寻址插到新的数组位置
                        newTab[(newCap-1)&e.hash] = e;
                    } else {
                        /**
                         * 如果此处为链表，则需要遍历，将数据依次放到新数组的正确位置。
                         * 同一下标下的数据在新数组中的位置是有规律的，因为新的容量是2倍，取模
                         * 的值就多了一位，所以就是看各个hash值高一位的。高位为1，则下标为i+oldCap
                         */
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        while (e != null) {
                            if ((e.hash & oldCap) == 0) {
                                if (loHead == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                if (hiHead == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                            e = e.next;
                        }
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }

        return newTab;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    final class EntrySet extends AbstractSet<Map.Entry<K,V>> {

        @Override
        public final int size() {
            return size;
        }

        public final void clear() {
            TinyHashMap.this.clear();
        }

        public final boolean contains(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry<?,?> entry = (Entry<?, ?>) o;
            Object key = entry.getKey();
            Node<K,V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(entry);

        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }
    }

    final class EntryIterator extends HashIterator
        implements Iterator<Map.Entry<K,V>> {

        @Override
        public Entry<K, V> next() {
            return nextNode();
        }
    }

    final class KeyIterator extends HashIterator
            implements Iterator<K> {
        public final K next() { return nextNode().key; }
    }

    final class ValueIterator extends HashIterator
            implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

    abstract class HashIterator {
        Node<K,V> next;
        Node<K,V> current;
        int expectedModCount; // for fail-fast
        int index;

        HashIterator() {
            expectedModCount = modCount;
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        public final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            // 每次拿next元素，同时要更新next指向
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (e == null) {
                throw new NoSuchElementException();
            }
            // 找到下一个坑遍历元素
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {

                } while (index < t.length && (next = t[index++]) == null);
            }

            return e;
        }

        public final void remove() {
            Node<K,V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }

    }

    public static void main(String[] args) {
//        TinyHashMap<String, String> map = new TinyHashMap<>();
//        map.put("name", "w");
//        map.put("age", "10");
//        map.put("sex", "male");
//        map.put("name", "d");
////        System.out.println(map.get("name"));
////        System.out.println(map.toString());
//        Iterator<Entry<String,String>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()) {
//            iterator.next();
//            iterator.remove();
//        }
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        List<String> syncList = Collections.synchronizedList(list);
        Iterator<String> iterator = syncList.iterator();
        int temp = 0;
        while (iterator.hasNext()) {
            if (temp == 1) {
                list.remove(temp);
            } else {
                iterator.next();
            }
            temp++;

        }

    }
}
