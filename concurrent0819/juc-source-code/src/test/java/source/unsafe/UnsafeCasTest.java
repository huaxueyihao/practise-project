package source.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UnsafeCasTest {

    /**
     * 将一个线程进行挂起是通过park方法实现的，调用 park后，线程将一直阻塞直到超时或者中断等条件出现。
     * unpark可以终止一个挂起的线程，使其恢复正常。
     * Java对线程的挂起操作被封装在 LockSupport类中，
     * LockSupport类中有各种版本pack方法，其底层实现最终还是使用Unsafe.park()方法和Unsafe.unpark()方法
     *
     */

    @Before
    public void setUp(){
        BasicConfigurator.configure();
    }

    @Test
    public void test() throws NoSuchFieldException {

        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);// 初始的内存值

        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        Field height = Rectangle.class.getDeclaredField("height");
        long hOffset = unsafe.objectFieldOffset(height);
        // 9是希望内存的值，3是要更新的值
        boolean flag = unsafe.compareAndSwapInt(rectangle, hOffset, 9, 3);

        int h = unsafe.getInt(rectangle, hOffset);
        log.info("flag=[{}],Rectangle.height=[{}]",flag,h);

    }



}
