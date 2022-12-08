package com.company.guavacache.CacheBuilder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 触发失效的原因：
 * SIZE：超出最大容量
 * EXPLICT：明确指定失效
 * EXPIRED：过期无效
 *
 * @author Real
 * Date: 2022/11/10 0:08
 */
public class CacheRemove {

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
        // 注意，如果 GET 返回 null，将会出现 NPE 错误。
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                // 过期时间设置为 3 秒
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .removalListener(removalNotification ->
                        System.out.println("删除监听：" + removalNotification.getKey() + "=" + removalNotification.getCause())
                ).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return DATA_MAP.get(key);
                    }
                });

        try {
            cache.get("1");
            // 线程休眠 3 秒，最终全部失效
            Thread.sleep(3000);
            printAll(cache);

            cache.get("1");
            cache.get("2");
            cache.get("3");
            cache.get("4");

            // 失效 3 号 Key
            cache.invalidate("3");
            System.out.println();
            printAll(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印全部输出
     *
     * @param cache Cache对象
     */
    public static void printAll(LoadingCache cache) {
        System.out.println("输出全部");
        Iterator iterator = cache.asMap().entrySet().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }

}
