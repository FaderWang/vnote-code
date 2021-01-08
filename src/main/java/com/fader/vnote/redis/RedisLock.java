//package com.fader.vnote.db.redis;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
//import java.util.LinkedList;
//
//
///**
// * @author FaderW
// * 2019/10/10
// */
//
//public class RedisLock {
//
//    private JedisPool jedisPool;
//    private long sleepTime;
//
//    private static final int MILLISECOND_TIMES = 1000;
//    private static final String LOCK_MSG = "OK";
//    private static final String UNLOCK_MSG = "1L";
//
//
//    public RedisLock(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//        this.sleepTime = 100L;
//    }
//
//    private Jedis getConn() {
//        return this.jedisPool.getResource();
//    }
//
//
//    public void lock(String key, String value)  {
//        Jedis jedis = getConn();
//        for (;;) {
//            String msg = jedis.set(key, value, "NX", "PX", MILLISECOND_TIMES * 10);
//            if (msg.equals(LOCK_MSG)) {
//                jedis.close();
//                break;
//            }
//            try {
//                Thread.sleep(sleepTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void lockInterruptibly(String key, String value) throws InterruptedException {
//        if (Thread.interrupted()) {
//            throw new InterruptedException();
//        }
//        Jedis jedis = getConn();
//        for (;;) {
//            if (Thread.interrupted()) {
//                throw new InterruptedException();
//            }
//            String msg = jedis.set(key, value, "NX", "PX", MILLISECOND_TIMES * 10);
//            if (msg.equals(LOCK_MSG)) {
//                jedis.close();
//                break;
//            }
//            try {
//                Thread.sleep(sleepTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * acquire lock block
//     * @param key
//     * @param value
//     * @param acquireTime
//     * @param durationTime
//     */
//    public boolean tryLock(String key, String value, long acquireTime, long durationTime) {
//        Jedis jedis = getConn();
//        long timeout = System.currentTimeMillis() + (acquireTime * 1000);
//
//        for(;;) {
//            if (System.currentTimeMillis() > timeout) {
//                return false;
//            }
//            String msg = jedis.set(key, value, "NX", "PX", MILLISECOND_TIMES * 10);
//            if (msg.equals(LOCK_MSG)) {
//                return true;
//            }
//            try {
//                Thread.sleep(durationTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public boolean tryLock(String key, String value, long acquireTime) {
//        return tryLock(key, value, acquireTime, sleepTime);
//    }
//
//    public boolean tryLock(String key, String value) {
//        boolean res = false;
//        try (Jedis jedis = getConn()) {
//            String msg = jedis.set(key, value, "NX", "PX", MILLISECOND_TIMES * 10);
//            if (msg.equals(LOCK_MSG)) {
//                res = true;
//            }
//        }
//
//        return res;
//    }
//
//    public void unlock(String key, String value) {
//        Jedis jedis = getConn();
//        Object res = jedis.eval("", 1, key, value);
//        if (UNLOCK_MSG.equals(res)) {
//            System.out.println("success unlock");
//        } else {
//            System.out.println("lock not exist or unlock fail");
//        }
//    }
//
//    public static void main(String[] args) {
//        String s = "It's a dog!";
////        System.out.println(s.charAt(5));
//        LinkedList<Character> stack = new LinkedList<>();
//        System.out.println(s.length());
//        for (int i = 0; i < s.length(); i++) {
//            stack.addLast(s.charAt(i));
//        }
//        StringBuilder builder = new StringBuilder();
//        while (true) {
//            Character c = stack.pollLast();
//            if (c == null) {
//                break;
//            }
//            builder.append(c);
//        }
//    }
//
//
//}
