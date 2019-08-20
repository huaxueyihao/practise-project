package source.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

@Slf4j
public class UnsafeMemoryTest {

    @Before
    public void setUp() {
        BasicConfigurator.configure();
//        PropertyConfigurator.configure("log4j.properties");
    }


    @Test
    public void test() {

        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        int size = 16;
        int data = 1024;
        long memoryAddress = unsafe.allocateMemory(size);
        unsafe.putAddress(memoryAddress, data);
        log.info("memoryAddress={},getAddress={}, data={}", memoryAddress, unsafe.getAddress(memoryAddress), data);

        // putLong 和 putAddress
        data = 2 * data;
        unsafe.putLong(memoryAddress, data);
        unsafe.putAddress(memoryAddress + 8, data * 2);
        unsafe.putAddress(memoryAddress + 16, data * 4);
        unsafe.putAddress(memoryAddress + 24, data * 8);
        log.info("memoryAddress={},getAddress={}, data={}", memoryAddress, unsafe.getAddress(memoryAddress), data);

        long reallocateMemoryAddress= unsafe.reallocateMemory(memoryAddress, size * 1024);
        log.info("memoryAddress={},reallocateMemoryAddress={}",memoryAddress,reallocateMemoryAddress);

        log.info("getAddress:{}, data:{}", unsafe.getAddress(reallocateMemoryAddress), data);
        log.info("getAddress+8:{}, data:{}", unsafe.getAddress(reallocateMemoryAddress + 8), data * 2);
        log.info("getAddress+16:{}, data:{}", unsafe.getAddress(reallocateMemoryAddress + 16), data * 4);
        log.info("getAddress+24:{}, data:{}", unsafe.getAddress(reallocateMemoryAddress + 24), data * 8);
        log.info("getAddress+32:{}, data:{}", unsafe.getAddress(reallocateMemoryAddress + 32), data * 16);


    }


}
