package com.company.asyncprogram.annotation;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Real
 * @since 2022/12/9 1:46
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Async("defaultThreadPoolExecutor")
    public Boolean execute(Integer num) {
        System.out.println("线程：" + Thread.currentThread().getName() + " , 任务：" + num);
        return true;
    }

}
