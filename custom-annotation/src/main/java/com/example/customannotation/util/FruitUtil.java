package com.example.customannotation.util;

import com.example.customannotation.annotation.FruitColor;
import com.example.customannotation.annotation.FruitName;
import com.example.customannotation.annotation.FruitSupplier;

import java.lang.reflect.Field;

/**
 * @author wei.song
 * @since 2023/1/7 21:46
 */
public class FruitUtil {

    public static void getFruitInfo(Class<?> clazz) {

        String strFruitName = " 水果名称：";
        String strFruitColor = " 水果颜色：";
        String strFruitSupplier = "供应商信息：";

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                strFruitName = strFruitName + fruitName.value();
                System.out.println(strFruitName);
            } else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                strFruitColor = strFruitColor + fruitColor.fruitColor().toString();
                System.out.println(strFruitColor);
            } else if (field.isAnnotationPresent(FruitSupplier.class)) {
                FruitSupplier fruitSupplier = field.getAnnotation(FruitSupplier.class);
                strFruitSupplier = " 供应商编号：" + fruitSupplier.id() + " 供应商名称：" + fruitSupplier.name() + " 供应商地址：" + fruitSupplier.address();
                System.out.println(strFruitSupplier);
            }
        }
    }

}
