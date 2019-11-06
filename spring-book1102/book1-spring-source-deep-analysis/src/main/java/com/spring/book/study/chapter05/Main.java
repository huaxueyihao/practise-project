package com.spring.book.study.chapter05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ap = new ClassPathXmlApplicationContext("factoryBeanTest.xml");
        Car car = (Car) ap.getBean("car");
        // Car{maxSpeed=400, brand='超级跑车', price=400.0}
        System.out.println(car);

        Object carFactoryBean =  ap.getBean("&car");
        // class com.spring.book.study.chapter05.CarFactoryBean
        System.out.println(carFactoryBean.getClass());

    }

}
