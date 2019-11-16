package com.spring.source.code.study.EntityResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;

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
    public void test() throws IOException {
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        // 获得输出流
        ClassPathResource classPathResource = new ClassPathResource("03.EntityResolver/dtd.xml");
        InputStream inputStream = classPathResource.getInputStream();
        InputSource inputSource = new InputSource(inputStream);
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        ResourceEntityResolver resourceEntityResolver = new ResourceEntityResolver(resourceLoader);

        final Log logger = LogFactory.getLog(getClass());
        ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
        try {
            Document document = documentLoader.loadDocument(inputSource, resourceEntityResolver, errorHandler, XmlValidationModeDetector.VALIDATION_DTD, false);
            NodeList beans = document.getElementsByTagName("beans");
            System.out.println(beans.getLength());
            NodeList bean = document.getElementsByTagName("bean");
            for (int i = 0; i < bean.getLength(); i++) {
                Node item = bean.item(i);
                NamedNodeMap attributes = item.getAttributes();
                Node id = attributes.getNamedItem("id");
                System.out.println(id);
            }
            System.out.println(bean.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
