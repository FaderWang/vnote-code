package com.fader.vnote.redis;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

/**
 * @author FaderW
 * @Date 2021/2/3 10:50
 */
public class RedisOperateSimple {

    private static Jedis jedis;

    static {
        jedis = new Jedis("10.10.0.9", 6379);
        jedis.auth("abc1234$");
    }

    public static void stringTest() {
        final String key = "capital:arrival";
        jedis.set(key, "银企直连");
        System.out.println(jedis.get(key));
        System.out.println(jedis.objectEncoding(key));
    }

    public static void main(String[] args) {
        stringTest();
    }
}
