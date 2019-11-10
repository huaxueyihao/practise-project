package com.spring.book.study.chapter06;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class PropertyConfigurerDemo {

    public static void main(String[] args) {
        ConfigurableListableBeanFactory bf = new XmlBeanFactory(new ClassPathResource("BeanFactory.xml"));

        BeanFactoryPostProcessor bfpp = (BeanFactoryPostProcessor) bf.getBean("bfpp");

        bfpp.postProcessBeanFactory(bf);
        // sSimplePostProcessor{connectionString='******', password='imaginecup', username='******'}
        System.out.println(bf.getBean("simpleBean"));
    }

}
