package com.example.crawlordermultithread.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.crawlordermultithread.entity.Order;
import com.example.crawlordermultithread.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wei.song
 * @since 2023/2/15 21:32
 */
@Slf4j
@Service
public class CrawlOrderCountdownLatch {

    private static final int NETWORK_THREAD_NUM = 2 * Runtime.getRuntime().availableProcessors() + 1;

    public List<Order> crawlOrders() {
        Integer orderCount = getOrderCount();
        if (ObjectUtil.isNull(orderCount) || orderCount == 0) {
            return Collections.emptyList();
        }

        int offset = 0;
        int pageSize = 200;

        int threadCount = (int) Math.ceil(1.0 * orderCount / pageSize);
        final ThreadPoolExecutor threadPool = ThreadPoolUtil.initPool(threadCount, threadCount, "crawl-order");
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        List<Runnable> taskList = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Request request = buildCrawlOrderRequest(pageSize, offset);
            Runnable runnable = () -> {
                try {
                    Response responseBody = new OkHttpClient().newCall(request).execute();
                    if (!responseBody.isSuccessful() || responseBody.body() == null) {
                        log.error("请求失败，返回数据：{}", responseBody.body());
                        return;
                    }
                    if (responseBody.body() != null) {
                        String response = Objects.requireNonNull(responseBody.body()).string();
                    }
                    log.info("XXX 店铺抓单结束");
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            };
            offset += pageSize;
        }


        return Collections.emptyList();
    }


    public Integer getOrderCount() {
        return 0;
    }

    public Request buildCrawlOrderRequest(int pageSize, int offset) {
        // 创建Request
        return new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .url("https://www.order.com?pageSize=" + pageSize + "&offset=" + offset)
                .get().build();
    }

}
