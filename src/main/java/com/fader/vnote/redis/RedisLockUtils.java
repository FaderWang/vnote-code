package com.fader.vnote.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author FaderW
 * redis锁工具类
 */
public class RedisLockUtils {

    private static final int DEFAULT_EXPIRE = 5;

    private static final String UNLOCK_LUA_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                    "   return redis.call('del',KEYS[1]) " +
                    "else" +
                    "   return 0 " +
                    "end";

    /**
     * 获取锁
     * @param redisTemplate
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public static boolean tryLock(RedisTemplate redisTemplate, String key, String value, long timeout) {
        Boolean lock = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                long start = System.currentTimeMillis();
                for (;;) {
                    SetParams setParams = SetParams.setParams().nx().ex(DEFAULT_EXPIRE);
                    Jedis jedis = (Jedis) redisConnection.getNativeConnection();
                    String result = jedis.set(key, value, setParams);
                    if ("OK".equals(result)) {
                        return true;
                    }
                    long now = System.currentTimeMillis();
                    if (now - start > timeout) {
                        return false;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return Boolean.TRUE.equals(lock) ? true : false;
    }

    /**
     * 释放锁
     * @param redisTemplate
     * @param key
     * @param value
     */
    public static void unlock(RedisTemplate redisTemplate, String key, String value) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Object nativeConnection = redisConnection.getNativeConnection();
                if (nativeConnection instanceof JedisCluster) {
                    JedisCluster jedisCluster = (JedisCluster) nativeConnection;
                    return jedisCluster.eval(UNLOCK_LUA_SCRIPT, Collections.singletonList(key), Collections.singletonList(value));
                } else {
                    Jedis jedis = (Jedis) redisConnection.getNativeConnection();
                    return jedis.eval(UNLOCK_LUA_SCRIPT, Collections.singletonList(key), Collections.singletonList(value));
                }
            }
        });
    }

}
