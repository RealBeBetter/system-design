package com.company.asyncprogram.annotation;

import org.springframework.scheduling.annotation.Async;

/**
 * @author Real
 * @since 2022/12/9 1:47
 */
public interface AsyncService {

    /**
     * 执行方法
     *
     * @param num 任务标识
     * @return 是否执行成功
     */
    @Async("defaultThreadPoolExecutor")
    Boolean execute(Integer num);

}
