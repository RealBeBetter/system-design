package com.company.guavacache.CacheBuilder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Real
 * Date: 2022/11/6 23:36
 */
public class Cache {

    /**
     * 创建缓存项
     */
    public static final Map<String, String> DATA_MAP = Maps.newHashMap();

    static {
        DATA_MAP.put("1", "张三");
        DATA_MAP.put("2", "李四");
        DATA_MAP.put("3", "王五");
        DATA_MAP.put("4", "赵六");
    }

    public static void main(String[] args) {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                // 最大缓存数量
                .maximumSize(3)
                // 缓存失效时间
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key)  {
                        return DATA_MAP.get(key);
                    }
                });

        try {
            System.out.println(cache.get("1"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
