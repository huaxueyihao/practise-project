package com.spring.source.code.study.NamespaceHandler;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NamespaceHandlerTest {

    @Test
    public void test() {

        ApplicationContext ap = new ClassPathXmlApplicationContext("09.NamespaceHandler/customDemoElement.xml");
        Demo demo = (Demo) ap.getBean("test");
        // Demo{demoName='demo1', demoDesc='案例1'}
        System.out.println(demo);

    }

}
