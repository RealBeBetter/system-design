package com.example.customannotation.annotation;

import java.lang.annotation.*;

/**
 * @author wei.song
 * @since 2023/1/7 21:45
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitSupplier {

    /**
     * 供应商编号
     */
    public int id() default -1;

    /**
     * 供应商名称
     */
    public String name() default "";

    /**
     * 供应商地址
     */
    public String address() default "";

}
