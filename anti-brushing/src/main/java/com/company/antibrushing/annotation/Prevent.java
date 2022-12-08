package com.company.antibrushing.annotation;

import com.company.antibrushing.config.PreventStrategy;

import java.lang.annotation.*;

/**
 * @author Real
 * Date: 2022/12/9 0:40
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Prevent {

    /**
     * 限制的时间值（秒）
     *
     * @return 时间
     */
    String value() default "60";

    /**
     * 提示
     */
    String message() default "";

    /**
     * 策略
     *
     * @return 使用的防刷策略
     */
    PreventStrategy strategy() default PreventStrategy.DEFAULT;
}
