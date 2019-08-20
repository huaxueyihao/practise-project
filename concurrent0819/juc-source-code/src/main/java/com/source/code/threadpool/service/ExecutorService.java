package com.source.code.threadpool.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public interface ExecutorService  extends Executor{

    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    <T> Future<T> submit(Callable<T> callable);

    <T> Future<T> submit(Runnable runnable, T result);

    Future<?> submit(Runnable runnable);

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables) throws InterruptedException;

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables, long timeout, TimeUnit unit) throws InterruptedException;

    <T> T invokeAny(Collection<? extends Callable<T>> callables) throws InterruptedException, ExecutionException;

    <T> T invokeAny(Collection<? extends Callable<T>> callables, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
