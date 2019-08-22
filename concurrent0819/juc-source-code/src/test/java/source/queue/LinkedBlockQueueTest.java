package source.queue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockQueueTest {


    /**
     * LinkedBlockQueue：一个基于链表的FIFO先进先出的对列
     */
    @Test
    public void testApi() throws InterruptedException {

        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>(list);
        System.out.println(blockingQueue);
        String peek = blockingQueue.peek();
        System.out.println(peek);
        System.out.println(blockingQueue);
        String poll = blockingQueue.poll();
        System.out.println(poll);
        System.out.println(blockingQueue);

        String take = blockingQueue.take();
        System.out.println(take);
        System.out.println(blockingQueue);


    }

    @Test
    public void testOffer() throws InterruptedException {
        final LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>(3);
        blockingQueue.offer("1");
        blockingQueue.offer("2");
        blockingQueue.offer("3");
//        System.out.println(blockingQueue);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    blockingQueue.poll();
//                    if (blockingQueue.size() <= 0) {
//                        break;
//                    }
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("i=" + i + ":" + blockingQueue.toString());
//                    if (blockingQueue.size() <= 0) {
//                        break;
//                    }
                    i++;

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1000;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        blockingQueue.put(1000 + "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }).start();


//        System.out.println(blockingQueue);
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(blockingQueue);
    }

    @Test
    public void testOfferThread() {

        final LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<Integer>(20);


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 11; i < 20; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    blockingQueue.offer(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < 10; i++) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        blockingQueue.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(blockingQueue);
    }

}
