package com.mybatis.book.study.model;

public enum Enabled {

    disabled(1),//禁用
    enabled(0),
    ;//启用

    private final int value;

    Enabled(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
