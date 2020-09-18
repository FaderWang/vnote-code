### 历史版本的演变
jdk1.7采用分段锁，整个Hash表被分成多个段，每个段对应一个Segment段锁，段于段之间可以并发访问，但是多线程
访问同一个段是需要加锁的。

jdk1.8取消了基于Segment的分段锁思想，改用CAS+Synchronized控制并发操作。在某些方面提升了性能。并且追随 1.8 
版本的 HashMap 底层实现，使用数组+链表+红黑树进行数据存储。

### 重要的成员属性
```java
// node节点数组，代表整个哈希表
transient volatile Node<K,V>[] table;

// 哈希表resize时使用
private transient volatile Node<K,V>[] nextTable;

// 基础计数器，节点的数量
private transient volatile long baseCount;

// 关键属性，初始化或者resize都需要依赖此属性
private transient volatile int sizeCtl;
```
sizeCtl属性代表的含义：
- 0：默认值
- -1：代表哈希表正在进行初始化
- 大于0：相当于 HashMap 中的 threshold，表示阈值
- 小于-1：代表有多个线程正在进行扩容

### `put`方法实现并发添加
实现逻辑在putVal方法中。代码比较长，分两部分进行分析：
```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        // 对key进行hash运算
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                // 如果哈希表还未进行初始化，先初始化hash表
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                // 根据hash值找到对应的索引位置且当前位置为空，则尝试通过CAS在当前位置加入新的节点。
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            // 如果hash值为-1，代表节点为forwarding nodes，协助扩容
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
                // 对当前节点加锁
                synchronized (f) {
                    // 检查当前节点是否被修改
                    if (tabAt(tab, i) == f) {
                        // 向普通节点添加元素
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K,V>(hash, key,
                                                              value, null);
                                    break;
                                }
                            }
                        }
                        // 向红黑树添加元素
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
    }
```

这里再分析下`initTable()`方法：
```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    // table未初始化时进行初始化
    while ((tab = table) == null || tab.length == 0) {
        // sizeCtl < 0说明当前有线程在进行初始化，线程让出CPU
        if ((sc = sizeCtl) < 0)
            Thread.yield(); // lost initialization race; just spin
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            // cas更新sizeCtl，设置初始化状态
            try {
                // double check
                if ((tab = table) == null || tab.length == 0) {
                    // sc > 0使用sc的值，否则的话使用默认容量
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    // 设置扩容阈值，n*0.75
                    sc = n - (n >>> 2);
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```
从源码可以发现初始化主要是通过自旋+CAS完成的，保证只要只初始化一次。

### `get()`方法
get方法比较简单，跟HashMap类似，源码：
```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    // 数组非空且定位到的索引处节点不为null
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        // key相等，则获取当前节点
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        // 头结点hash值小于0，说明正在扩容或者是红黑树，find查找
        else if (eh < 0)
            return (p = e.find(h, key)) != null ? p.val : null;
        // key不相等则遍历链表
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```

### ForwardingNode节点
ForwardingNode继承了Node节点，内部持有一个nextTable引用，指向外部的nextTable，在扩容操作中，
我们需要对每个桶中的结点进行分离和转移，如果某个桶结点中所有节点都已经迁移完成了
（已经被转移到新表 nextTable 中了），那么会在原 table 表的该位置挂上一个 ForwardingNode 结点，说明此桶已经完成迁移。
该节点的hash == MOVED

所以在putVal中添加元素时，会遍历table数组，如果当前桶节点hash == MOVED，说明已经有线程正在扩容 rehash 操作，整体上还未完成，
不过我们要插入的桶的位置已经完成了所有节点的迁移。由于检测到当前哈希表正在扩容，于是让当前线程去协助扩容。

### 参考
[解读Java 8 中为并发而生的 ConcurrentHashMap](https://mp.weixin.qq.com/s/K_pdkDVEq-GPbwTz3ow5Xg)
