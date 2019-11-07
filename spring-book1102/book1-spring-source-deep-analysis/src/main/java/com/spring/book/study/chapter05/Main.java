package com.spring.book.study.chapter05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static ApplicationContext ap;

    public static void main(String[] args) throws Exception {
        testSetterCircleDepend();
    }

    private static void testSetterCircleDepend() {
        ap = new ClassPathXmlApplicationContext("setterCircleTest.xml");
        TestA testA = ap.getBean("testA", TestA.class);
        System.out.println(testA.getClass());
    }

    private static void testConstructorCirleDepend() throws Exception {
        ap = new ClassPathXmlApplicationContext("constructorCircleTest.xml");
    }

    public static void testFactBean() {
        ap = new ClassPathXmlApplicationContext("factoryBeanTest.xml");
        Car car = (Car) ap.getBean("car");
        // Car{maxSpeed=400, brand='超级跑车', price=400.0}
        System.out.println(car);

        Object carFactoryBean = ap.getBean("&car");
        // class com.spring.book.study.chapter05.CarFactoryBean
        System.out.println(carFactoryBean.getClass());
    }

}
