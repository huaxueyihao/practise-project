package source.unsafe;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * 加载字节码文件，获取Class对象，反射调用
 *
 *
 *
 */
@Slf4j
public class UnsafeDefineClassTest {

    @Before
    public void setUp() {
        BasicConfigurator.configure();
    }



    @Test
    public void test() throws Exception{
        // 编译后的字节码文件 绝对路径
        String path = "/Users/amao/Documents/code_temp/study/git/practise-project/concurrent0819/juc-source-code/target/test-classes/source/unsafe/Hello.class";
        File helloClassFlie = new File(path);
        FileInputStream input = new FileInputStream(helloClassFlie);
        byte[] content = new byte[(int) helloClassFlie.length()];
        input.read(content);
        input.close();


        Unsafe unsafe = UnsafeWrapper.getUnsafe();
        Class<?> clazz = unsafe.defineClass(null, content, 0, content.length, null, null);
        Method method = clazz.getMethod("sayHi");
        method.invoke(clazz.newInstance(),null);

    }



}
