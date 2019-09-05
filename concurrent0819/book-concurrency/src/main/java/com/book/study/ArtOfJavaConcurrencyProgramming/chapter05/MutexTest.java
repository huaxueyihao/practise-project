package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MutexTest {


    public static void main(String[] args) {

        Mutex mutex = new Mutex();


        Runnable runnable = () -> {
            mutex.lock();
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ": acquire mutex lock");
            } finally {
                mutex.unlock();
            }
        };


        IntStream.range(1, 10).forEach(index -> {
            new Thread(runnable).start();
        });


    }

}
