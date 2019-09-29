package com.algorithms3.book.chapter10;

/**
 * 父接口不加同步，子接口加synchronized是没有问题的
 */
public class Child implements Parent {
    public synchronized void read() {

    }
}
