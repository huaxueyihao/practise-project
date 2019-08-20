package com.source.code.threadpool.service;

import lombok.Builder;

import java.util.concurrent.Callable;

public class Executors {


    public static <T> Callable<T> callable(Runnable task, T result){
        if(task == null){
            throw new NullPointerException();
        }
        return new RunnableAdapter<T> (task,result);
    }

    static final class RunnableAdapter<T> implements Callable<T> {
        final Runnable task;
        final T result;
        RunnableAdapter(Runnable task, T result) {
            this.task = task;
            this.result = result;
        }

        @Override
        public T call() {
            task.run();
            return result;
        }
    }
}
