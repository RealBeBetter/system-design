package com.company.antibrushing.config;

/**
 * @author Real
 * @since 2022/12/9 0:54
 */
public enum PreventStrategy {

    /**
     * 默认(1分钟内不允许再次请求）
     */
    DEFAULT(1),

    ;

    public int getValue() {
        return value;
    }

    private final int value;

    PreventStrategy(int value) {
        this.value = value;
    }
}
