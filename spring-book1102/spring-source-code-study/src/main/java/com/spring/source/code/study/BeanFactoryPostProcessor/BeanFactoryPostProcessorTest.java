package com.spring.source.code.study.BeanFactoryPostProcessor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactoryPostProcessorTest {

    @Test
    public void testCustomBeanFactoryPostProcessor(){
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("14.BeanFactoryPostProcessor/beanFactoryProcessorTest.xml");
    }

}
