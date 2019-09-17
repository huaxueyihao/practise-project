package com.book.study.ArtOfJavaConcurrencyProgramming.chapter05;

import java.util.concurrent.locks.ReentrantLock;

public class FairLock {

    static class MyService {

        private ReentrantLock lock;

        public MyService(ReentrantLock lock) {
            this.lock = lock;
        }

        public void serviceMethod() {
            lock.lock();
            try {
                System.out.println("ThreadName " + Thread.currentThread().getName() + " acquire lock");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void lockOneSecond() {
            lock.lock();
            try {
                System.out.println("lock begin");
                Thread.sleep(1000);
                System.out.println("lock end");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("release lock");
            }
        }
    }

    static class MyThread extends Thread {
        private MyService myService;

        public MyThread(String name, MyService myService) {
            super(name);
            this.myService = myService;
        }

        @Override
        public void run() {
            System.out.println(currentThread().getName() + "开始运行");
            myService.serviceMethod();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        MyService myService = new MyService(new ReentrantLock(false));

        // 起一个线程抢占一秒钟
        new Thread(() -> {
            myService.lockOneSecond();
        }).start();

        // 创建10个线程
        int num = 10;
        Thread[] threads = new Thread[num];
        for (int i = 0; i < num; i++) {
            threads[i] = new MyThread("T" + i, myService);
        }
        Thread.sleep(10);
        // 依次启动线程，启动一个后休眠50ms，保证线程按照顺序启动（数字小的线程优先抢占锁）
        for (int i = 0; i < num; i++) {
            threads[i].start();
            Thread.sleep(50);
        }


    }


}
