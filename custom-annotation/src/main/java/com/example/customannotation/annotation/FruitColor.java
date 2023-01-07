package com.example.customannotation.annotation;

import java.lang.annotation.*;

/**
 * @author wei.song
 * @since 2023/1/7 21:44
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitColor {

    /**
     * 颜色属性
     */
    Color fruitColor() default Color.GREEN;

    /**
     * 颜色枚举
     */
    public enum Color {
        /**
         * 蓝色
         */
        BLUE,
        /**
         * 红色
         */
        RED,
        /**
         * 绿色
         */
        GREEN,
    }

}
