package com.source.code.threadpool.service;

import java.util.concurrent.Future;

public interface RunnableFuture<V> extends Runnable, Future<V> {

    void run();

}
