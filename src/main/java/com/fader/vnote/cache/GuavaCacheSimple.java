package com.fader.vnote.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author FaderW
 * guava cache使用
 */
public class GuavaCacheSimple {

    @Data
    static class CacheBody {
        private String message;
    }

    /**
     * 创建缓存
     */
    static void loadCache() throws ExecutionException {
        LoadingCache loadingCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES) //过期时间
                .build(new CacheLoader() {
                    // 找不到时调用load方法
                    @Override
                    public Object load(Object key) throws Exception {
                        CacheBody cacheBody = new CacheBody();
                        cacheBody.setMessage(key.toString());
                        return cacheBody;
                    }
                });

        System.out.println(loadingCache.get("wang"));
    }

    /**
     * get key时获取不到指定回调
     * @throws ExecutionException
     */
    static void getCall() throws ExecutionException {
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(100).build();
        final String key = "wang";
        String value = cache.get(key, () -> {
            String result = key.toUpperCase();
            return result;
        });

        System.out.println(value);

    }

    public static void main(String[] args) throws ExecutionException {
        /**
         * load方式在创建缓存时指定未命中时对所有key生效的统一加载逻辑
         * callable回调方式可以指定某个key获取不到时的回调逻辑
         */
//        loadCache();
        getCall();
    }
}
