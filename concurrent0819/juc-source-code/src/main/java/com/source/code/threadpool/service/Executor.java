package com.source.code.threadpool.service;

public interface Executor {
    void execute(Runnable runnable);
}
