package com.fader.vnote.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author FaderW
 * @Date 2021/1/6 17:30
 * redis bitSet 实现签到功能
 */
public class BitSetCheckSimple {

    private RedisTemplate<String, Object> redisTemplate;

    private static final String userSignKey = "user:sign:%s:%s";

    public void doCheck() {
        // 获取当月日期
        LocalDate localDate = LocalDate.now();
        int day = localDate.getDayOfMonth();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        Boolean signFlag = redisTemplate.opsForValue().setBit(String.format(userSignKey, "100001", localDate.format(dateTimeFormatter)), day-1, true);
        if (signFlag) {
            System.out.println("当前已签到");
        } else {
            System.out.println("签到成功");
        }
    }

    public Long signCountOfCurrentMonth() {
        final String signKey = String.format(userSignKey, "100001", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
        Long count = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Long count = redisConnection.bitCount(signKey.getBytes());
                return count;
            }
        });

        return count;
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().getDayOfMonth());
    }
}
