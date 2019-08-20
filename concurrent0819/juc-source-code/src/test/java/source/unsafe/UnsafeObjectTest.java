package source.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@Slf4j
public class UnsafeObjectTest {

    @Before
    public void setUp() {
        BasicConfigurator.configure();
    }


    @Test
    public void test() throws NoSuchFieldException {

        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        int height = 5, width = 6;

        Rectangle rectangle = new Rectangle();

        Field heightField = Rectangle.class.getDeclaredField("height");
        Field widthField = Rectangle.class.getDeclaredField("width");

        // 获取对象字段的偏移位置
        long hOffset = unsafe.objectFieldOffset(heightField);
        long wOffset = unsafe.objectFieldOffset(widthField);

        log.info("hOffset=[{}],wOffset=[{}]",hOffset,wOffset);

        // 设置对象上的字段值
        unsafe.putOrderedInt(rectangle,hOffset,height);
        unsafe.putInt(rectangle,wOffset,width);
        // 获取对象上的字段字
        log.info("get rectangle.height=[{}],height=[{}]",unsafe.getInt(rectangle,hOffset),height);
        log.info("get rectangle.width=[{}],width=[{}]",unsafe.getInt(rectangle,wOffset),width);
        log.info("hOffset=[{}],wOffset=[{}]",unsafe.objectFieldOffset(heightField),unsafe.objectFieldOffset(widthField));





    }

}
