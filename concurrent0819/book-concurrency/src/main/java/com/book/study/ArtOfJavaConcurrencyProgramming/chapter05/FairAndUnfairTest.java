package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {

    private static RenntrantLock2 fairLock = new RenntrantLock2(true);

    private static RenntrantLock2 unfairLock = new RenntrantLock2(false);


    @Test
    public void fair() {
        testLock(fairLock);
    }

    private void testLock(RenntrantLock2 lock) {

        List<Job> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(new Job(lock));
        }

        list.forEach(Job::start);




    }

    private static class Job extends Thread {
        private RenntrantLock2 lock;

        public Job(RenntrantLock2 lock) {
            this.lock = lock;
        }

        public void run() {
            lock.lock();
            try {
                System.out.print("lock by [" + Thread.currentThread().getName() + "],");
                System.out.println("wait by " + lock.getQueuedThreads());

            }finally {
                lock.unlock();
            }

//            System.out.println();
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
