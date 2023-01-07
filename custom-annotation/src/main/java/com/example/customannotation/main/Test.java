package com.example.customannotation.main;


import com.example.customannotation.entity.Apple;
import com.example.customannotation.util.FruitUtil;

/**
 * @author wei.song
 * @since 2023/1/7 21:49
 */
public class Test {

    public static void main(String[] args) {
        FruitUtil.getFruitInfo(Apple.class);
    }

}
