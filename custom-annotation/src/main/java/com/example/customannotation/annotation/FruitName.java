package com.example.customannotation.annotation;

import java.lang.annotation.*;

/**
 * @author wei.song
 * @since 2023/1/7 21:33
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitName {

    /**
     * 值，名称
     *
     * @return {@link String}
     */
    String value() default "";

}
