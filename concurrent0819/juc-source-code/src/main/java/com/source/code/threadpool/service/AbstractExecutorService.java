package com.source.code.threadpool.service;


import com.source.code.threadpool.service.impl.FutureTask;

public abstract class AbstractExecutorService implements ExecutorService  {

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
    }

}
