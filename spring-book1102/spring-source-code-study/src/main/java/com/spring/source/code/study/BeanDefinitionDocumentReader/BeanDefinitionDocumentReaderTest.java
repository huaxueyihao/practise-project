package com.spring.source.code.study.BeanDefinitionDocumentReader;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanDefinitionDocumentReaderTest {


    @Test
    public void testImport(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("06.BeanDefinitionDocumentReader/importTest1.xml");
    }


    @Test
    public void testAlias(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("06.BeanDefinitionDocumentReader/aliasTest.xml");
        // 以下三个都是同一个bean
        System.out.println(app.getBean("testA"));
        System.out.println(app.getBean("testA1"));
        System.out.println(app.getBean("testA2"));
    }
}
