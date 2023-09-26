package com.company.guavacache.CacheBuilder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author wei.song
 * @since 2023/9/26 14:06
 */
public class CacheBuilderTest {

    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(2L, TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException {
        CACHE.asMap().put("1", "1");
        Thread.sleep(1900L);
        String value = CACHE.asMap().get("1");
        System.out.println(value);
        Thread.sleep(1900L);

        System.out.println(CACHE.getIfPresent("1"));
    }

}
