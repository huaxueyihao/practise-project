package com.algorithms3.book.chapter10;

import java.util.ArrayDeque;

public class ArrayQueueTest {
    public static void main(String[] args) {

        ArrayDeque<String> deque = new ArrayDeque<String>(100);
        deque.addLast("tail01");
        deque.addFirst("head");
        deque.addLast("tail02");

    }
}
