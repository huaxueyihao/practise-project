package com.spring.source.code.study.BeanFactory;

import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryTest {

    @Test
    public void testXmlBeanFactory(){
        XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("13.BeanFactory/beanFactoryTest.xml"));
        Object demo = beanFactory.getBean("demo");
        // class com.spring.source.code.study.BeanFactory.Demo
        System.out.println(demo.getClass());
    }

}
