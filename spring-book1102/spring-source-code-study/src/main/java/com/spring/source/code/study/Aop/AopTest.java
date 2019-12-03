package com.spring.source.code.study.Aop;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopTest {

    @Test
    public void testProxyFactoryBean() {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("18.Aop1/aopTest1.xml");
        Object proxy = app.getBean("proxy");
        Sleepable sleep = (Sleepable) proxy;
        sleep.sleep();
        System.out.println(proxy.getClass());
    }

    @Test
    public void testProxy(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("18.Aop1/aopTest1.xml");
        Object proxy = app.getBean("&proxy");
        System.out.println(proxy.getClass());
        Sleepable sleep = (Sleepable) proxy;
        System.out.println(sleep.getClass());
        sleep.sleep();

        /**
         * 输出结果
         * class com.sun.proxy.$Proxy6
         * class com.sun.proxy.$Proxy6
         * take off cloth。。。。
         * sleeping。。。。
         * put on cloth。。。。
         *
         */
    }


    @Test
    public void testProxy2(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("18.Aop1/aopTest1.xml");
        Object proxy = app.getBean("human");
        System.out.println(proxy.getClass());
        Sleepable sleep = (Sleepable) proxy;
        System.out.println(sleep.getClass());
        sleep.sleep();

        /**
         * 输出结果
         * class com.sun.proxy.$Proxy5
         * class com.sun.proxy.$Proxy5
         * take off cloth。。。。
         * sleeping。。。。
         * put on cloth。。。。
         */
    }


    @Test
    public void testAspectJ(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("18.Aop1/aopTest2.xml");
        Object proxy = app.getBean("human");
        System.out.println(proxy.getClass());
        Sleepable sleep = (Sleepable) proxy;
        System.out.println(sleep.getClass());
        sleep.sleep();

        /**
         * 输出结果ßß
         * class com.sun.proxy.$Proxy9
         * class com.sun.proxy.$Proxy9
         * take off cloth。。。。
         * sleeping。。。。
         * put on cloth。。。。
         */
    }


    @Test
    public void testAopConfig(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("18.Aop1/aopTest3.xml");
        Object proxy = app.getBean("human");
        System.out.println(proxy.getClass());
        Sleepable sleep = (Sleepable) proxy;
        System.out.println(sleep.getClass());
        sleep.sleep();

        /**
         * 输出结果
         * class com.sun.proxy.$Proxy6
         * class com.sun.proxy.$Proxy6
         * take off cloth。。。。
         * put on cloth。。。。
         * sleeping。。。。
         */
    }

}
