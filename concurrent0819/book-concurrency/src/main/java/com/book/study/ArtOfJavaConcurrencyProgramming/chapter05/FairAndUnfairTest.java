package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {

    private static RenntrantLock2 fairLock = new RenntrantLock2(true);

    private static RenntrantLock2 unfairLock = new RenntrantLock2(false);


    @Test
    public void testFair() throws IOException {
        testLock(fairLock);
    }

    @Test
    public void testUnFair() throws IOException {
        testLock(unfairLock);
    }

    private void testLock(RenntrantLock2 lock) throws IOException {
        for (int i = 0; i < 5; i++) {
            new Job(lock, "" + i).start();
        }
        System.in.read();
    }

    private static class Job extends Thread {
        private RenntrantLock2 lock;

        public Job(RenntrantLock2 lock, String name) {
            super(name);
            this.lock = lock;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                lock.lock();
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("lock by [" + Thread.currentThread().getName() + "],wait by " + lock.getQueuedThreads());
                } finally {
                    lock.unlock();
                }
            }
        }

        @Override
        public String toString() {
            return getName();
        }
    }


    private static class RenntrantLock2 extends ReentrantLock {
        public RenntrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        protected Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<Thread>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }


}
