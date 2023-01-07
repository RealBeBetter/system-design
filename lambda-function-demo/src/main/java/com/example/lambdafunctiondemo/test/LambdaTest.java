package com.example.lambdafunctiondemo.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Java内置四大核心函数式接口：
 * 消费型接口 Consumer<T> void accept(T t);
 * 供给型接口 Supplier<T> T get();
 * 函数型接口 Function<T, R> R apply(T t);
 * 断定型接口 Predicate<T> boolean test(T t);
 *
 * @author Real
 * @since 2023/1/7 19:53
 */
public class LambdaTest {

    public static void main(String[] args) {
        testConsume();
        testPredicate();
    }

    public static void testConsume() {
        BigDecimal money = BigDecimal.valueOf(500);
        consume(money, (dollar) -> {
            BigDecimal subtract = dollar.subtract(BigDecimal.valueOf(200));
            System.out.println("消费了" + 200 + "，还剩下" + subtract.intValue());
        });
    }

    public static void testPredicate() {
        List<String> list = Arrays.asList("北京", "天津", "南京", "东京");
        List<String> listNew = predicate(list, str -> str.contains("京"));
        System.out.println(listNew);
    }

    /**
     * 消费
     *
     * @param money    货币
     * @param consumer 消费者
     */
    public static void consume(BigDecimal money, Consumer<BigDecimal> consumer) {
        consumer.accept(money);
    }

    /**
     * 判断
     *
     * @param list      列表
     * @param predicate 判断
     * @return {@link List}<{@link String}>
     */
    public static List<String> predicate(List<String> list, Predicate<String> predicate) {
        List<String> filterList = new ArrayList<>();

        for (String s : list) {
            if (predicate.test(s)) {
                filterList.add(s);
            }
        }
        return filterList;
    }

}
