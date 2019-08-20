package com.source.code.threadpool.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ExecutorCompletionService<V> implements CompletionService<V> {

    private final Executor executor;

    private final AbstractExecutorService aes;

    private final BlockingQueue<Future<V>> completionQueue;


    public ExecutorCompletionService(Executor executor) {
        if (executor == null) {
            throw new NullPointerException();
        }
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ? (AbstractExecutorService) executor : null;
        this.completionQueue = new LinkedBlockingQueue<Future<V>>();
    }


    @Override
    public Future<V> submit(Callable<V> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        RunnableFuture<V> f = newTaskFor(task);
        executor.execute(new QueueingFuture(f));
        return f;
    }

    private RunnableFuture<V> newTaskFor(Callable<V> task) {
        if (aes == null) {
            return new FutureTask<V>(task);
        } else {
            return aes.newTaskFor(task);
        }
    }

    @Override
    public Future<V> submit(Runnable task, V result) {
        if (task == null) {
            throw new NullPointerException();
        }
        RunnableFuture<V> f = newTaskFor(task, result);



        return null;
    }

    private RunnableFuture<V> newTaskFor(Runnable task, V result) {

        return null;
    }

    @Override
    public Future<V> take() throws InterruptedException {
        return null;
    }

    @Override
    public Future<V> poll() {
        return null;
    }

    @Override
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }


    private class QueueingFuture extends FutureTask<Void> {

        private final Future<V> task;

        QueueingFuture(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }

        @Override
        protected void done() {
            completionQueue.add(task);
        }
    }
}
