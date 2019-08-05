package com.fader.vnote.tool;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author FaderW
 * 2019/4/22
 */

public class GuavaCache {

    public static void main(String[] args) throws ExecutionException {
        LoadingCache<String, Object> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(4)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return key;
                    }
                });

        System.out.println(loadingCache.get("name", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "name".length();
            }
        }));
        System.out.println(loadingCache.get("age"));
        System.out.println(loadingCache.get("sex"));

        System.out.println(loadingCache.get("name"));
        System.out.println(loadingCache.get("age"));
        System.out.println(loadingCache.get("sex"));
    }
}
