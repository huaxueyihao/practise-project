package com.spring.book.study.chapter04;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ap = new ClassPathXmlApplicationContext("customTagTest.xml");
        User user = (User) ap.getBean("testbean");
        // User{userName='aaa', email='bbb'}
        System.out.println(user);

    }

}
