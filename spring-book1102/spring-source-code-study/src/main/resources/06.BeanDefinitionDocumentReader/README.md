BeanDefinitionDocumentReader(beanDefinition文档阅读器)

## 1 简介 

### 1.1 认识


```



BeanDefinitionDocumentReader接口的默认实现，该接口根据“ spring-beans” DTD和XSD格式（Spring的默认XML bean定义格式）读取bean定义。
所需XML文档的结构，元素和属性名称在此类中进行了硬编码。（当然，如果需要生成此格式，可以运行转换）。
<beans>不必是XML文档的根元素：此类将解析XML文件中的所有bean定义元素，而与实际的根元素无关。

/**
 * SPI for parsing an XML document that contains Spring bean definitions.
 * SPI 用于解析一个xml文档，这个xml文档包含了bean的定义信息
 * Used by XmlBeanDefinitionReader for actually parsing a DOM document.
 * XmlBeanDefinitionReader使用该类用于解析DOM文档
 * <p>Instantiated per document to parse: Implementations can hold
 * 
 * state in instance variables during the execution of the
 * {@code registerBeanDefinitions} method, for example global
 * settings that are defined for all bean definitions in the document.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 18.12.2003
 * @see XmlBeanDefinitionReader#setDocumentReaderClass
 */
public interface BeanDefinitionDocumentReader {

	/**
	 * Set the Environment to use when reading bean definitions.
	 * <p>Used for evaluating profile information to determine whether a
	 * {@code <beans/>} document/element should be included or ignored.
	 * @deprecated in favor of {@link XmlReaderContext#getEnvironment()}
	 */
	@Deprecated
	void setEnvironment(Environment environment);

	/**
	 * Read bean definitions from the given DOM document and
	 * register them with the registry in the given reader context.
	 * @param doc the DOM document
	 * @param readerContext the current context of the reader
	 * (includes the target registry and the resource being parsed)
	 * @throws BeanDefinitionStoreException in case of parsing errors
	 */
	void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
			throws BeanDefinitionStoreException;

}


public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {}


```

> 可以看到，这个类就一个接口和一个默认实现



### 2 从XmlBeanDefinitionReader.registerBeanDefinitions(Document doc, Resource resource)开始跟踪


```
这个是方法是在xml配置解析为DOM文档后，要执行的方法
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
    
    // 创建了BeanDefinitionDocumentReader对象，
    BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
    
    documentReader.setEnvironment(getEnvironment());
    int countBefore = getRegistry().getBeanDefinitionCount();
    // 交给documentReader进行注册
    documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
    return getRegistry().getBeanDefinitionCount() - countBefore;
}

// 反射创建
protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
    return BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
}

// 创建一个xml 阅读上下文，这个上下文中持有了NamespaceHandlerResolver命名空间处理器，
// 当前的XmlBeanDefinitionReader,
// problemReporter,这个是用于异常校验用的
// eventListener:事件监听
// resource:当前的资源
// 后面再分析这个，貌似是协助XmlBeanDefinitionReader干活的
public XmlReaderContext createReaderContext(Resource resource) {
    return new XmlReaderContext(resource, this.problemReporter, this.eventListener,
            this.sourceExtractor, this, getNamespaceHandlerResolver());
}


```

### 2.1 DefaultBeanDefinitionDocumentReader.registerBeanDefinitions(Document doc, XmlReaderContext readerContext)方法



```

/**
 * This implementation parses bean definitions according to the "spring-beans" XSD
 * (or DTD, historically).
 * <p>Opens a DOM Document; then initializes the default settings
 * specified at the {@code <beans/>} level; then parses the contained bean definitions.
 */
public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
    this.readerContext = readerContext;
    logger.debug("Loading bean definitions");
    Element root = doc.getDocumentElement();
    // 注册的执行方法在这了
    doRegisterBeanDefinitions(root);
}






```

### 2.2 DefaultBeanDefinitionDocumentReader.doRegisterBeanDefinitions(Element root)方法


```

protected void doRegisterBeanDefinitions(Element root) {
    // Any nested <beans> elements will cause recursion in this method. In
    // order to propagate and preserve <beans> default-* attributes correctly,
    // keep track of the current (parent) delegate, which may be null. Create
    // the new (child) delegate with a reference to the parent for fallback purposes,
    // then ultimately reset this.delegate back to its original (parent) reference.
    // this behavior emulates a stack of delegates without actually necessitating one.
    BeanDefinitionParserDelegate parent = this.delegate;
    // 创建个代理解析器
    this.delegate = createDelegate(getReaderContext(), root, parent);

    if (this.delegate.isDefaultNamespace(root)) {
        String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
        if (StringUtils.hasText(profileSpec)) {
            String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
                    profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
            if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
                return;
            }
        }
    }
    
    // 空方法，允许用户自己重写
    preProcessXml(root);
    // 解析bean和注册bean在这里完成
    parseBeanDefinitions(root, this.delegate);
    // 空方法，允许用户自己重写
    postProcessXml(root);

    this.delegate = parent;
}


/**
 * Allow the XML to be extensible by processing any custom element types first,
 * before we start to process the bean definitions. This method is a natural
 * extension point for any other custom pre-processing of the XML.
 * <p>The default implementation is empty. Subclasses can override this method to
 * convert custom elements into standard Spring bean definitions, for example.
 * Implementors have access to the parser's bean definition reader and the
 * underlying XML resource, through the corresponding accessors.
 * @see #getReaderContext()
 */
 // 在开始处理Bean定义之前，请先处理任何自定义元素类型，以使XML具有可扩展性。
protected void preProcessXml(Element root) {
}

/**
 * Allow the XML to be extensible by processing any custom element types last,
 * after we finished processing the bean definitions. This method is a natural
 * extension point for any other custom post-processing of the XML.
 * <p>The default implementation is empty. Subclasses can override this method to
 * convert custom elements into standard Spring bean definitions, for example.
 * Implementors have access to the parser's bean definition reader and the
 * underlying XML resource, through the corresponding accessors.
 * @see #getReaderContext()
 */
//在完成bean定义的处理之后，最后通过处理任何自定义元素类型来允许XML可扩展。
protected void postProcessXml(Element root) {
}

```

### 2.3 DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate)方法

```

protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
    // 从根元素开始解析
    if (delegate.isDefaultNamespace(root)) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (delegate.isDefaultNamespace(ele)) {
                    // 解析默认的标签
                    parseDefaultElement(ele, delegate);
                }
                else {
                    // 解析自定义的标签，先不研究这个
                    delegate.parseCustomElement(ele);
                }
            }
        }
    }
    else {
        // 解析自定义的标签
        delegate.parseCustomElement(root);
    }
}


```

### 2.4 DefaultBeanDefinitionDocumentReader.parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate)方法

```

// 解析默认的元素
private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
    if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
        // 这里是为了解析 <import resource="classpath:06.BeanDefinitionDocumentReader/importTest2.xml"></import>标签
        importBeanDefinitionResource(ele);
    }
    else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
        // 解析<alias name="testA" alias="testA2,testA3"></alias> 标签，为bean起别名的标签
        processAliasRegistration(ele);
    }
    else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
        // 解析<bean> 标签
        processBeanDefinition(ele, delegate);
    }
    else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
        // recurse
        // 递归调用了注册方法，若是遇到<beans>标签
        doRegisterBeanDefinitions(ele);
    }
}

```


### 2.5 DefaultBeanDefinitionDocumentReader.importBeanDefinitionResource(Element ele)方法


```

protected void importBeanDefinitionResource(Element ele) {
    // 解析<import resource="classpath:06.BeanDefinitionDocumentReader/importTest2.xml"></import>中的resource属性
    String location = ele.getAttribute(RESOURCE_ATTRIBUTE);
    if (!StringUtils.hasText(location)) {
        getReaderContext().error("Resource location must not be empty", ele);
        return;
    }

    // Resolve system properties: e.g. "${user.dir}"
    // 解析占位符等，例如<import resource="classpath:06.BeanDefinitionDocumentReader/${xxx}.xml"></import>中的
    location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);

    // 用户记录import的resource解析之后的Resource对象
    Set<Resource> actualResources = new LinkedHashSet<Resource>(4);

    // Discover whether the location is an absolute or relative URI
    boolean absoluteLocation = false;
    try {
        absoluteLocation = ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute();
    }
    catch (URISyntaxException ex) {
        // cannot convert to an URI, considering the location relative
        // unless it is the well-known Spring prefix "classpath*:"
    }

    // Absolute or relative?
    if (absoluteLocation) {
        try {
            // 这里开始解析标签<import resource="classpath:06.BeanDefinitionDocumentReader/importTest2.xml"></import>中的importTest2.xml文件中的bean
            // 有点递归调用的感觉这里的getReader就是XmlBeanDefinitionReader
            int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
            if (logger.isDebugEnabled()) {
                logger.debug("Imported " + importCount + " bean definitions from URL location [" + location + "]");
            }
        }
        catch (BeanDefinitionStoreException ex) {
            getReaderContext().error(
                    "Failed to import bean definitions from URL location [" + location + "]", ele, ex);
        }
    }
    else {
        // No URL -> considering resource location as relative to the current file.
        try {
            int importCount;
            Resource relativeResource = getReaderContext().getResource().createRelative(location);
            if (relativeResource.exists()) {
                importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
                actualResources.add(relativeResource);
            }
            else {
                String baseLocation = getReaderContext().getResource().getURL().toString();
                importCount = getReaderContext().getReader().loadBeanDefinitions(
                        StringUtils.applyRelativePath(baseLocation, location), actualResources);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Imported " + importCount + " bean definitions from relative location [" + location + "]");
            }
        }
        catch (IOException ex) {
            getReaderContext().error("Failed to resolve current resource location", ele, ex);
        }
        catch (BeanDefinitionStoreException ex) {
            getReaderContext().error("Failed to import bean definitions from relative location [" + location + "]",
                    ele, ex);
        }
    }
    Resource[] actResArray = actualResources.toArray(new Resource[actualResources.size()]);
    getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
}


```


### 2.6 DefaultBeanDefinitionDocumentReader.importBeanDefinitionResource(Element ele)方法

```

protected void processAliasRegistration(Element ele) {
    // 解析alias标签
    // <bean id="testA" class="com.spring.source.code.study.BeanDefinitionDocumentReader.TestA"/>
    // <alias name="testA" alias="testA1"></alias> 为testA在起个别名
    String name = ele.getAttribute(NAME_ATTRIBUTE);
    String alias = ele.getAttribute(ALIAS_ATTRIBUTE);
    boolean valid = true;
    if (!StringUtils.hasText(name)) {
        getReaderContext().error("Name must not be empty", ele);
        valid = false;
    }
    if (!StringUtils.hasText(alias)) {
        getReaderContext().error("Alias must not be empty", ele);
        valid = false;
    }
    if (valid) {
        try {
            // 在注册其中将别名注册到别名的aliasMap中
            // aliasMap的key是别名testA1,testA,值是testA，也就是别名和bean的id是多对一的
            // 在获取bean的时候，若是有别名，先用别名获得id，然后根据id获得bean实例
            getReaderContext().getRegistry().registerAlias(name, alias);
        }
        catch (Exception ex) {
            getReaderContext().error("Failed to register alias '" + alias +
                    "' for bean with name '" + name + "'", ele, ex);
        }
        getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
    }
}


```


### 2.7 DefaultBeanDefinitionDocumentReader.processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate)方法


```

protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
    // 这里是解析<bean>的入口，将解析的<bean>信息封装到BeanDefinitionHolder对象中
    BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
    if (bdHolder != null) {
        // 对BeanDefinitionHolder实例进行装饰一下
        bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
        try {
            // Register the final decorated instance.
            // 将BeanDefinitionHolder对象注册到容器的方法入口
            BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
        }
        catch (BeanDefinitionStoreException ex) {
            getReaderContext().error("Failed to register bean definition with name '" +
                    bdHolder.getBeanName() + "'", ele, ex);
        }
        // Send registration event.
        getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
    }
}


```

### 2.8 import和alias标签的demo

> import

```
@Test
public void testImport(){
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("06.BeanDefinitionDocumentReader/importTest1.xml");
}


importTest1.xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

    <import resource="classpath:06.BeanDefinitionDocumentReader/importTest2.xml"></import>
    <bean id="testA" class="com.spring.source.code.study.BeanDefinitionDocumentReader.TestA"/>
</beans>



importTest2.xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

    <bean id="testB" class="com.spring.source.code.study.BeanDefinitionDocumentReader.TestB"/>

</beans>





```

> alias

```
@Test
public void testAlias(){
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("06.BeanDefinitionDocumentReader/aliasTest.xml");
    // 以下三个都是同一个bean
    System.out.println(app.getBean("testA"));
    System.out.println(app.getBean("testA1"));
    System.out.println(app.getBean("testA2"));
}


aliasTest.xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

    <bean id="testA" class="com.spring.source.code.study.BeanDefinitionDocumentReader.TestA"/>
    <alias name="testA" alias="testA1"></alias>
    <alias name="testA" alias="testA2"></alias>
</beans>




```

## 3.总结

> DefaultBeanDefinitionDocumentReader，主要将标签分类处理了import、alias、bean默认标签，其中前两种
  处理简单，bean标签处理最复杂。接下来会分析BeanDefinitionParserDelegate和SimpleAliasRegistry
