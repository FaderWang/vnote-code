

### Redis是什么？
> Redis是一个使用ANSI C编写的开源、支持网络、基于内存、可选持久性的键值对存储数据库。它是一种 NoSQL（not-only sql，泛指非关系型数据库）的数据库。

### Redis支持哪些数据类型？
Redis的外围由一个键、值映射的字典构成。与其他非关系型数据库主要不同在于：Redis中值的类型不仅限于字符串，还支持如下抽象数据类型：
- List（列表）
- Hash（字典）
- Set（无序不重复集合）
- Sorted Set（有序不重复集合）

### Redis内部是如何定义这五种数据类型的

**这里有一张图**

![](https://mmbiz.qpic.cn/mmbiz/JfTPiahTHJhqawVia4Al9NLKSmdc3720GUeVgXnGcUJ4HicZ3ZJ0jmYX8EicuSadC66qAEeSOFO6ljUHdpPhBib3XSQ/640?wx_fmt=other&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

如上图所示：type表示一个value对象具体是何种数据类型，encoding是不同数据类型在Redis内部的存储方式。

#### 参考
- [面试前，我们要复习多少Redis知识点？](https://mp.weixin.qq.com/s/s84wF31mPIuBno1ejHST7g)

### Redis能做什么？
- 数据库
- 缓存
- 队列、消息队列
- 分布式锁

#### 博客系统中具体使用场景
- 记录帖子的点赞数、评论数和点击数 (hash)。
- 记录用户的帖子 ID 列表 (排序)，便于快速显示用户的帖子列表 (zset)。
- 记录帖子的标题、摘要、作者和封面信息，用于列表页展示 (hash)。
- 记录帖子的点赞用户 ID 列表，评论 ID 列表，用于显示和去重计数 (zset)。
- 缓存近期热帖内容 (帖子内容空间占用比较大)，减少数据库压力 (hash)。
- 记录帖子的相关文章 ID，根据内容推荐相关帖子 (list)。
- 如果帖子 ID 是整数自增的，可以使用 Redis 来分配帖子 ID(计数器)。
- 收藏集和帖子之间的关系 (zset)。
- 记录热榜帖子 ID 列表，总热榜和分类热榜 (zset)。
- 缓存用户行为历史，进行恶意行为过滤 (zset,hash)。
- 数据推送去重Bloom filter
- pv，uv统计

### 分布式锁
利用redis的`setnx`命令，key不存在则设置成功。加锁就是去set同一个key。有个问题是如果服务挂了，有可能锁不会释放，所以需要设置过期时间。
Redis2.6.12以上版本，可以用set获取锁。set可以实现`setnx`和`expire`，这个是原子操作。

从 Redis 2.6.12 版本开始， SET 命令的行为可以通过一系列参数来修改：

- EX seconds ： 将键的过期时间设置为 seconds 秒。 执行 SET key value EX seconds 的效果等同于执行 SETEX key seconds value 。
- PX milliseconds ： 将键的过期时间设置为 milliseconds 毫秒。 执行 SET key value PX milliseconds 的效果等同于执行 PSETEX key milliseconds value 。
- NX ： 只在键不存在时， 才对键进行设置操作。 执行 SET key value NX 的效果等同于执行 SETNX key value 。
- XX ： 只在键已经存在时， 才对键进行设置操作。

**解锁**时删除key，直接删除key这里会有一些问题：A线程获取锁，A阻塞了，到了设置的超时时间，锁自动释放了，这时候B获取锁，正在执行逻辑。这时候A恢复执行且执行完毕，
去解锁。这时候就会将B的锁给释放了。

解决办法是加锁的时候同时设置一个唯一的value值，解锁判断当前key的value值是否等于匹配的value值，value值不匹配不解锁。这里匹配和删除是两步操作，需要原子操作，可以利用lua脚本
```lua
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```

#### RedLock
使用`Redission`第三方工具包。

### 缓存击穿、雪崩、穿透
[《吊打面试官》系列-缓存雪崩、击穿、穿透](https://mp.weixin.qq.com/s/knz-j-m8bTg5GnKc7oeZLg)

#### 总结

**穿透**
请求一个不存在的key，缓存不存在，请求会打到数据库。如果恶意大批量的请求，会压垮数据库。解决办法：
- 首选对请求数据做校验，过滤不规范的参数，比如id=-1
- 数据不存在的话，给redis也加一个缓存，值为null，设置30s的过期时间，这样能应对大批量的请求
- 利用布隆过滤器

**雪崩**
热点数据在同一时间失效，数据库qps激增。解决办法：
- 定时任务更新缓存时，设置过期时间加上一个随机值。
- 热点数据永不过期

**击穿** 
而缓存击穿不同的是缓存击穿是指一个Key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，
当这个Key在失效的瞬间，持续的大并发就穿破缓存，直接请求数据库，就像在一个完好无损的桶上凿开了一个洞。
- 简单的方法热点数据永不过期
- 缓存过期了，去数据库获取数据时加上互斥锁，保证只有一个线程访问数据库，后面的请求再过来，缓存已经重新更新了。

### Redis底层数据结构

### 过期与内存淘汰策略

### Redis持久化

### 主从、哨兵、高可用



