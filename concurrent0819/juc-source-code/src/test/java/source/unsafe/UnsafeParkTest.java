package source.unsafe;

import org.junit.Test;
import source.util.ToolUtil;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * park方法阻塞当前线程
 * unpark方法唤醒阻塞的线程
 */
public class UnsafeParkTest {

    public static void main(String[] args) {

        Unsafe unsafe = UnsafeWrapper.getUnsafe();

        Thread currentThread = Thread.currentThread();
//        unsafe.unpark(currentThread);
//        unsafe.unpark(currentThread);
//        unsafe.unpark(currentThread);

        unsafe.park(false, 3000000000l);


        System.out.println("SUCCESS!!!");

    }

    /**
     * park方法第一个参数是是否绝对时间，false，第二个参数单位是纳秒，指在这个时间后阻塞就会被唤醒
     * true，第二个参数单位是毫秒，指在某个指定的时间点后会被唤醒
     */
    @Test
    public void testPark() {
        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        // 三秒后自动被唤醒，单位纳秒
        unsafe.park(false, 3000000000l);
        System.out.println("SUCCESS!!!");

        // 在未来某个时间点后被唤醒
        long time = System.currentTimeMillis() + 3000;
        unsafe.park(true, time);
        System.out.println("SUCCESS!!!");
    }

    @Test
    public void testUnPark() {
        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        Thread currentThread = Thread.currentThread();
        // 后面的3秒设定就没有作用了，立即返回了
        unsafe.unpark(currentThread);
        unsafe.park(false, 3000000000l);
        System.out.println("SUCCESS!!!");
    }

    @Test
    public void testUnPark2() {
        final Unsafe unsafe = UnsafeWrapper.getUnsafe();
        final Thread currentThread = Thread.currentThread();
        // 后面的3秒设定就没有作用了，立即返回了
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("i=" + i);
                        currentThread.interrupt();
//                        if (i == 2) {
//                            unsafe.unpark(currentThread);
//                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        unsafe.park(false, 0);
        System.out.println("SUCCESS!!!");
    }


}
