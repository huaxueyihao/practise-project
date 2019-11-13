package com.spring.book.study.chapter07;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
//        testAspectJ();
        testWeaver();
    }

    private static void testWeaver() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("weaverTest.xml");
        TestBean testBean = (TestBean) bf.getBean("testBean");
        testBean.test();
    }

    private static void testAspectJ() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("apectjTest.xml");
        TestBean testBean = (TestBean) bf.getBean("testBean");
        testBean.test();
    }

}
