package com.company.guavacache.CacheBuilder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Real
 * Date: 2022/12/8 22:12
 */
public class CacheHelper {


    /**
     * 避免空指针方法1
     */
    public void avoidNullPointerException() {
        Cache<String, Object> cache = CacheBuilder.newBuilder()
                .build();

        Object obj = cache.getIfPresent("key");
        if (obj == null) {
            // 从数据源获取数据
            obj = new Object();
            cache.put("key", obj);
        }
    }

    /**
     * 避免空指针方法2
     */
    public void avoidNullPointer() throws ExecutionException {
        Cache<String, Object> cache = CacheBuilder.newBuilder()
                .build();

        Object value = cache.get("key", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 从数据源获取数据
                return new Object();
            }
        });
    }

    Cache<String, Object> cache = CacheBuilder.newBuilder().build();

    Optional<Object> getValue(String key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }


    /**
     * 避免空指针方法3
     */
    public void avoidNull() {
        Optional<Object> value = getValue("someKey");
        if (value.isPresent()) {
            // Do something with the value
            Object obj = value.get();
            // ...
        } else {
            // Handle the case where the value is not present
            // ...
        }
    }

}
