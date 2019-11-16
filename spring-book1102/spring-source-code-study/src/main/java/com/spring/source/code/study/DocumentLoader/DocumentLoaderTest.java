package com.spring.source.code.study.DocumentLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
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

public class DocumentLoaderTest {

    @Test
    public void test() throws IOException {
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        // 获得输出流
//        ClassPathResource classPathResource = new ClassPathResource("05.DefaultDocumentLoader/xsd.xml");
        ClassPathResource classPathResource = new ClassPathResource("03.EntityResolver/xsd.xml");
        InputStream inputStream = classPathResource.getInputStream();
        InputSource inputSource = new InputSource(inputStream);
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        ResourceEntityResolver resourceEntityResolver = new ResourceEntityResolver(resourceLoader);

        final Log logger = LogFactory.getLog(getClass());
        ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);
        try {
            Document document = documentLoader.loadDocument(inputSource, resourceEntityResolver, errorHandler, XmlValidationModeDetector.VALIDATION_XSD, false);
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

            /**
             * 输出结果
             * 1
             * id="test1"
             * id="test2"
             * 2
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
