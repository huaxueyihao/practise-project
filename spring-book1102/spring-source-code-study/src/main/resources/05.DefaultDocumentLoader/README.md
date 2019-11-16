DefaultDocumentLoader(默认的文档加载器)

## 1 简介



> DefaultDocumentLoader是将解析配置(xml)为Document的加载器，
  就是配置文件经过一些列操作后封装为InputSourc后，作为入参。
  然后DefaultDocumentLoader就给返回一个解析好Document，
  实际上DefaultDocumentLoader也只是中间作用，为解析器准备些参数
  
  
## 1.1 类关系

```

public interface DocumentLoader {

	Document loadDocument(
			InputSource inputSource, EntityResolver entityResolver,
			ErrorHandler errorHandler, int validationMode, boolean namespaceAware)
			throws Exception;

}

public class DefaultDocumentLoader implements DocumentLoader {}


```
   
> 一个接口，一个默认实现，简单，其实Spring好多类命名：解析器(parser)，加载器(loader)，阅读器(reader)，处理器(resolver)等等
  一般，名称和职责是对的上的，还是蛮形象的；同时，是不是也体现了单一职责的原则(设计模式)呢。我加载就只干加载，解析交给解析器去完成，
  没有把加载和解析搞到一个类中来，即加载，又解析。
   

## 2.XmlBeanDefinitionReader.doLoadDocument(InputSource inputSource, Resource resource)跟踪


```

/**
 * Actually load the specified document using the configured DocumentLoader.
 * @param inputSource the SAX InputSource to read from
 * @param resource the resource descriptor for the XML file
 * @return the DOM Document
 * @throws Exception when thrown from the DocumentLoader
 * @see #setDocumentLoader
 * @see DocumentLoader#loadDocument
 */
protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
    // this.documentLoader = new DefaultDocumentLoader()
    // 这里由beanFactory阅读器交接工作给文档加载器
    return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
            getValidationModeForResource(resource), isNamespaceAware());
}


```




### 2.1DefaultDocumentLoader.loadDocument方法


```


public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
			ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
    
    // 创建一个Document工厂
    DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
    if (logger.isDebugEnabled()) {
        logger.debug("Using JAXP provider [" + factory.getClass().getName() + "]");
    }
    // 获得一个builder
    DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
    // builder开始解析，这后面都是w3c的dom文档解析的问题，不再深入分析
    // 只要晓得这里就是解析xml文档为dom文档(Document)
    return builder.parse(inputSource);
}



protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
			throws ParserConfigurationException {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(namespaceAware);
    
    // 验证模式不是禁止的
    if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
        // 开启验证
        factory.setValidating(true);
        if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
            // Enforce namespace aware for XSD...
            // 若验证模式是XSD
            // 开启对命名空间的验证
            factory.setNamespaceAware(true);
            try {
                factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
            }
            catch (IllegalArgumentException ex) {
                ParserConfigurationException pcex = new ParserConfigurationException(
                        "Unable to validate using XSD: Your JAXP provider [" + factory +
                        "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
                        "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
                pcex.initCause(ex);
                throw pcex;
            }
        }
    }

    return factory;
}


protected DocumentBuilder createDocumentBuilder(
        DocumentBuilderFactory factory, EntityResolver entityResolver, ErrorHandler errorHandler)
        throws ParserConfigurationException {

    DocumentBuilder docBuilder = factory.newDocumentBuilder();
    if (entityResolver != null) {
        docBuilder.setEntityResolver(entityResolver);
    }
    if (errorHandler != null) {
        docBuilder.setErrorHandler(errorHandler);
    }
    return docBuilder;
}


```

### 2.2 demo看看Document里是啥


```

@Test
public void test() throws IOException {
    DocumentLoader documentLoader = new DefaultDocumentLoader();
    // 获得输入流
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


```


## 3 总结

> DefaultDocumentLoader就做了些参数设置问题，真正完成解析是DocumentBuilder，这个已经不再是Spring里的内容了
  就不再往下继续挖掘了
