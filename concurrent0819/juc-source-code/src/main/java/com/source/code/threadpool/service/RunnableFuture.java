package com.source.code.threadpool.service;

public interface RunnableFuture<V> extends Runnable, Future<V> {

    void run();

}
