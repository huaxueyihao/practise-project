package com.spring.source.code.study.EntityResolver;

import org.junit.Test;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;

public class EntityResolverTest {
   

    @Test
    public void resourceEntityResolver() {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        GenericApplicationContext app = new GenericApplicationContext();
        ResourceEntityResolver resourceEntityResolver = new ResourceEntityResolver(app);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(app);
        beanDefinitionReader.setEnvironment(new StandardEnvironment());
        beanDefinitionReader.setResourceLoader(app);
        beanDefinitionReader.setEntityResolver(resourceEntityResolver);

        beanDefinitionReader.loadBeanDefinitions("03.EntityResolver/xsd.xml");

    }


    @Test
    public void testDTDConfig() throws IOException {
        // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.dtd
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/dtd.xml");
    }

    @Test
    public void testXsdConfig() throws IOException {
        // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.xsd
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/xsd.xml");
    }

    @Test
    public void testDTDLocalConfig() throws IOException {
        // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.dtd
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/dtdLocal.xml");
    }

    @Test
    public void testXsdLocalConfig() throws IOException {
        // 这个结果是：03.EntityResolver/spring-beans-2.0.dtd
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/xsdLocal.xml");
    }

}
