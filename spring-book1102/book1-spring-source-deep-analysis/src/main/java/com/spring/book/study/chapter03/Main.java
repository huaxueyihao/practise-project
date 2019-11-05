package com.spring.book.study.chapter03;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ap = new ClassPathXmlApplicationContext("replaceMethodTest.xml");
        TestChangeMethod test = (TestChangeMethod) ap.getBean("testChangeMethod");
        // 输出：i am Teacher
        test.changeMe();
    }
}
