package source.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class UnsafeCasTest {

    @Before
    public void setUp(){
        BasicConfigurator.configure();
    }

    @Test
    public void test(){

        AtomicInteger atomicInteger = new AtomicInteger(1);

        boolean flag = atomicInteger.compareAndSet(1, 1);
        log.info("flag={},atomicInteger=[{}]",flag,atomicInteger.get());

//        Unsafe unsafe = UnsafeWrapper.getUnsafe();

//        unsafe.compareAndSwapObject(this,atomicInteger)

    }



}
