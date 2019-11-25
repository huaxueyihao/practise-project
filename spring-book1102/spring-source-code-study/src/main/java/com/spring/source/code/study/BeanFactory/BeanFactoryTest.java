package com.spring.source.code.study.BeanFactory;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryTest {

    @Test
    public void testXmlBeanFactory() {
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("13.BeanFactory/beanFactoryTest.xml"));
        Object demo = beanFactory.getBean("demo");
        // class com.spring.source.code.study.BeanFactory.Demo
        System.out.println(demo.getClass());
    }


    @Test
    public void testFactoryBean() {

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("13.BeanFactory/beanFactoryTest.xml");
        // 这个获得的就是正常的bean
        Object studentFactoryBean = app.getBean("studentFactoryBean");
        // class com.spring.source.code.study.BeanFactory.Student
        System.out.println(studentFactoryBean.getClass());
        // "&"获得的是StudentFactoryBean
        Object student = app.getBean("&studentFactoryBean");
        //class com.spring.source.code.study.BeanFactory.StudentFactoryBean
        System.out.println(student.getClass());
    }


    @Test
    public void testGetBeanByAlisaName() {

        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("13.BeanFactory/beanFactoryTest.xml");
        // 这个获得的就是正常的bean
        Object demo1 = app.getBean("demo1");
        Object demo2 = app.getBean("demo2");
        // 这个demo3的获取，就是do.. while循环的作用
        // demo3拿到demo1，然后demo1再拿到demo
        Object demo3 = app.getBean("demo3");
        Object demo = app.getBean("demo");
        System.out.println(demo1.getClass());
        System.out.println(demo2.getClass());
        System.out.println(demo3.getClass());
        System.out.println(demo.getClass());
    }

}
