EntityResolver

## 1 简介

### 1.1 认识
> 1.是org.xml.sax包下的接口,由于为找到合适的资料，只能翻译源码了
  这里的单词entity(实体)，不能单纯的理解为java对象实体，他应该是指实物或者源物，感觉解释成一种源更合适啊
 



```
/**
 * Basic interface for resolving entities.
 * 用于解析实体的基本接口
 * 此模块（包括源代码和文档）均位于Public Domain中，并且不附带任何担保。 有关 更多信息，请参见http://www.saxproject.org。
 * 如果SAX应用程序需要实现对外部实体的自定义处理，则它必须实现此接口并使用该setEntityResolver 方法向SAX驱动程序注册实例 。
 * 然后，XML阅读器将允许应用程序在包含它们之前拦截任何外部实体（包括外部DTD子集和外部参数实体，如果有的话）。
 * 许多SAX应用程序不需要实现此接口，但是对于从数据库或其他专用输入源构建XML文档的应用程序，或使用URL以外的URI类型的应用程序而言，它特别有用。
 * 以下解析器将为应用程序提供具有系统标识符“ http://www.myhost.com/today”的实体的特殊字符流：
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * See <a href='http://www.saxproject.org'>http://www.saxproject.org</a>
 * for further information.
 * 更多信息请观看http://www.saxproject.org
 * </blockquote>
 *
 * <p>If a SAX application needs to implement customized handling
 * for external entities, it must implement this interface and
 * register an instance with the SAX driver using the
 * {@link org.xml.sax.XMLReader#setEntityResolver setEntityResolver}
 * method.</p>
 *
 * <p>The XML reader will then allow the application to intercept any
 * external entities (including the external DTD subset and external
 * parameter entities, if any) before including them.</p>
 *
 * <p>Many SAX applications will not need to implement this interface,
 * but it will be especially useful for applications that build
 * XML documents from databases or other specialised input sources,
 * or for applications that use URI types other than URLs.</p>
 * <p>The following resolver would provide the application
 * with a special character stream for the entity with the system
 * identifier "http://www.myhost.com/today":</p>
 *
 * <pre>
 * import org.xml.sax.EntityResolver;
 * import org.xml.sax.InputSource;
 *
 * public class MyResolver implements EntityResolver {
 *   public InputSource resolveEntity (String publicId, String systemId)
 *   {
 *     if (systemId.equals("http://www.myhost.com/today")) {
 *              // return a special input source
 *       MyReader reader = new MyReader();
 *       return new InputSource(reader);
 *     } else {
 *              // use the default behaviour
 *       return null;
 *     }
 *   }
 * }
 * </pre>
 *
 * <p>The application can also use this interface to redirect system
 * 应用程序还可以使用此接口将系统标识符重定向到本地URI或在目录中查找替换（可能通过使用公共标识符）。
 * identifiers to local URIs or to look up replacements in a catalog
 * (possibly by using the public identifier).</p>
 *
 * @since SAX 1.0
 * @author David Megginson
 * @see org.xml.sax.XMLReader#setEntityResolver
 * @see org.xml.sax.InputSource
 */
public interface EntityResolver {


允许应用程序解析外部实体。
解析器将在打开除顶级文档实体之外的任何外部实体之前调用此方法。
这样的实体包括DTD中引用的外部DTD子集和外部参数实体（无论哪种情况，仅当解析器读取外部参数实体时），
以及文档元素中引用的外部通用实体（如果解析器读取外部通用实体）。
应用程序可以请求解析器定位实体本身，使用替代URI或使用应用程序提供的数据（作为字符或字节输入流）。
应用程序编写者可以使用此方法将外部系统标识符重定向到安全和/或本地URI，
在目录中查找公共标识符，或从数据库或其他输入源（包括例如对话框）中读取实体。 
XML和SAX均未指定使用公共或系统ID来解析资源的首选策略。但是，SAX指定如何解释此方法返回的任何InputSource，
并且如果未返回任何InputSource，则系统ID将作为URL取消引用。
如果系统标识符是URL，则SAX解析器必须完全解析它，然后才能将其报告给应用程序


    /**
     * Allow the application to resolve external entities.
     *
     * <p>The parser will call this method before opening any external
     * entity except the top-level document entity.  Such entities include
     * the external DTD subset and external parameter entities referenced
     * within the DTD (in either case, only if the parser reads external
     * parameter entities), and external general entities referenced
     * within the document element (if the parser reads external general
     * entities).  The application may request that the parser locate
     * the entity itself, that it use an alternative URI, or that it
     * use data provided by the application (as a character or byte
     * input stream).</p>
     *
     * <p>Application writers can use this method to redirect external
     * system identifiers to secure and/or local URIs, to look up
     * public identifiers in a catalogue, or to read an entity from a
     * database or other input source (including, for example, a dialog
     * box).  Neither XML nor SAX specifies a preferred policy for using
     * public or system IDs to resolve resources.  However, SAX specifies
     * how to interpret any InputSource returned by this method, and that
     * if none is returned, then the system ID will be dereferenced as
     * a URL.  </p>
     *
     * <p>If the system identifier is a URL, the SAX parser must
     * resolve it fully before reporting it to the application.</p>
     *
     * @param publicId The public identifier of the external entity
     *        being referenced, or null if none was supplied.
     * @param systemId The system identifier of the external entity
     *        being referenced.
     * @return An InputSource object describing the new input source,
     *         or null to request that the parser open a regular
     *         URI connection to the system identifier.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException A Java-specific IO exception,
     *            possibly the result of creating a new InputStream
     *            or Reader for the InputSource.
     * @see org.xml.sax.InputSource
     */
    public abstract InputSource resolveEntity (String publicId,
                                               String systemId)
        throws SAXException, IOException;

}


```

>  看的懵逼，网上说是获得校验约束文件dtd或者xsd类型文件时，可以远程下载这些文件，也可以本地指定路径或者
   然后这个接口就是为了拦截处理将远程下载的url映射到本地classpath下，就是不做这个映射，加载dtd或xsd要去
   远程下载，做了映射就加载本地设置的，而http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
   就被映射到了org/springframework/beans/xml/spring-beans-2.0.xsd，去org/springframework/beans/xml/这个
   包下面查看确实存在这个配置文件
   如下图
  

![avatar](images/01_spring.schemas.jpg)



### 1.2 介绍Spring中的若干个实现

```

/**
 * {@link EntityResolver} implementation that attempts to resolve schema URLs into
 * local {@link ClassPathResource classpath resources} using a set of mappings files.
 * EntityResolver实现用于尝试解析schema URL映射至classpath下一个映射文件
 * <p>By default, this class will look for mapping files in the classpath using the pattern:
 * {@code META-INF/spring.schemas} allowing for multiple files to exist on the
 * classpath at any one time.
 * 默认情况下，这个类将要在classpath:META-INF/spring.schemas寻找映射，这个路径下熏晕多个文件同时存在
 * The format of {@code META-INF/spring.schemas} is a properties
 * file where each line should be of the form {@code systemId=schema-location}
 * where {@code schema-location} should also be a schema file in the classpath.
 * Since systemId is commonly a URL, one must be careful to escape any ':' characters
 * which are treated as delimiters in properties files.
 *
 * <p>The pattern for the mapping files can be overidden using the
 * {@link #PluggableSchemaResolver(ClassLoader, String)} constructor
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class PluggableSchemaResolver implements EntityResolver {}

/**
 * EntityResolver implementation for the Spring beans DTD,
 * to load the DTD from the Spring class path (or JAR file).
 *
 * <p>Fetches "spring-beans-2.0.dtd" from the class path resource
 * 从classpath资源下抓取"spring-beans-2.0.dtd"
 * "/org/springframework/beans/factory/xml/spring-beans-2.0.dtd",
 * no matter whether specified as some local URL that includes "spring-beans"
 * in the DTD name or as "http://www.springframework.org/dtd/spring-beans-2.0.dtd".
 *
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 * @since 04.06.2003
 * @see ResourceEntityResolver
 */
public class BeansDtdResolver implements EntityResolver {}


/**
 * {@link EntityResolver} implementation that delegates to a {@link BeansDtdResolver}
 * and a {@link PluggableSchemaResolver} for DTDs and XML schemas, respectively.
 * EntityResolver的实现用于代理BeansDtdResolver和PluggableSchemaResolver
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Rick Evans
 * @since 2.0
 * @see BeansDtdResolver
 * @see PluggableSchemaResolver
 */
public class DelegatingEntityResolver implements EntityResolver {

    private final EntityResolver dtdResolver;
    
    	private final EntityResolver schemaResolver;
    
    	public DelegatingEntityResolver(ClassLoader classLoader) {
    		this.dtdResolver = new BeansDtdResolver();
    		this.schemaResolver = new PluggableSchemaResolver(classLoader);
    	}

}

public class ResourceEntityResolver extends DelegatingEntityResolver {}


```

> 可以看到有四个PluggableSchemaResolver，BeansDtdResolver，DelegatingEntityResolver，ResourceEntityResolver实现
  其中ResourceEntityResolver又继承至DelegatingEntityResolver，
  DelegatingEntityResolver是BeansDtdResolver，PluggableSchemaResolver的代理
  PluggableSchemaResolver:先去Spring jar包下的META-INF/spring.schemas里面去找映射路径，然后去org/springframework/beans/xml这个路径找，若是找到则返回找的这个，若没找到，
  则ResourceEntityResolver会继续找，一般这个本地的classpath下就获通过ResourceEntityResolver找到
  BeansDtdResolver：无论本地自定义还是Spring jar下的，都会去org/springframework/beans/xml这个路径下拿到dtd文件
  


## 2 从ResourceEntityResolver跟踪


```

AbstractXmlApplicationContext.loadBeanDefinitions(DefaultListableBeanFactory beanFactory)

protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
    // Create a new XmlBeanDefinitionReader for the given BeanFactory.
    // 创建bean阅读器
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

    // Configure the bean definition reader with this context's
    // resource loading environment.
    // 设置资源加载器，设置环境配置
    beanDefinitionReader.setEnvironment(this.getEnvironment());
    beanDefinitionReader.setResourceLoader(this);
    // 设置了实体处理器(用来寻找本地的约束文件的dtd或xsd)
    beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

    // Allow a subclass to provide custom initialization of the reader,
    // then proceed with actually loading the bean definitions.
    initBeanDefinitionReader(beanDefinitionReader);
    loadBeanDefinitions(beanDefinitionReader);
}


```

> 可以看到，beanDefinition阅读器，设置了ResourceEntityResolver

```

XmlBeanDefinitionReader.doLoadDocument(InputSource inputSource, Resource resource)方法

protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
    // 在文档加载器加载文档时，有需要EntityResolver作为参数，
    // getEntityResolver()的得到的就是上述的ResourceEntityResolver
    // 这个方法解析出Document，其实是走的SAX的api解析出来的，已经也就是解析xml文件最终干的活是工具包SAX
    // SAX包下的源码不在跟进，所以接下来的跟中都是直接跳到Spring下的这四个类中进行断点跟踪
    // 请看DocumentLoader章节，比较简单
    return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
            getValidationModeForResource(resource), isNamespaceAware());
}


```

### 2.1 ResourceEntityResolver.resolveEntity(String publicId, String systemId)方法

```

测试类1，启动进行debug跟踪
@Test
public void testDTDConfig() throws IOException {
    // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.dtd
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/dtd.xml");
}

测试类2
@Test
public void testXsdConfig() throws IOException {
    // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.xsd
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/xsd.xml");
}

测试类3
@Test
public void testDTDLocalConfig() throws IOException {
    // 这个结果是：org/springframework/beans/xml/spring-beans-2.0.dtd
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/dtdLocal.xml");
}

测试类4
@Test
public void testXsdLocalConfig() throws IOException {
    // 这个结果是：03.EntityResolver/spring-beans-2.0.dtd
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("03.EntityResolver/xsdLocal.xml");
}

public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    // 走到这里的时候，xsd.xml，或者dtd.xml配置文件的中的约束文件地址字符串已经被拿到了
    // 测试1：dtd.xml:publicId= -//SPRING//DTD BEAN 2.0//EN ,systemId=http://www.springframework.org/dtd/spring-beans-2.0.dtd
    // 测试2：xsd.xml:publicId=null,systemId=http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
    // 测试3：dtd.xml:publicId= -//SPRING//DTD BEAN 2.0//EN ,systemId=file:///Users/amao/Documents/code_temp/study/git/practise-project/spring-book1102/spring-source-code-study/03.EntityResolver/spring-beans-2.0.dtd
    // 测试4：xsd.xml:publicId=null,systemId=file:///Users/amao/Documents/code_temp/study/git/practise-project/spring-book1102/spring-source-code-study/03.EntityResolver/spring-beans-2.0.xsd
    // 先调用超类中的方法,调用超类，也就是先使用DelegatingEntityResolver这个代理中的方法，代理还是交给了BeansDtdResolver(dtd.xml现在这个用的少了)或PluggableSchemaResolver(xsd.xml)
    // BeansDtdResolver,PluggableSchemaResolver现在spring的相关jar包中进行寻找，找到约束文件后，返回约束文件的输入流资源InputSource
    InputSource source = super.resolveEntity(publicId, systemId);
    // 测试1、2、3返会的source都不是null
    // 测试4返回的是null
    // 若是source=null,说明Spring的jar包中没有找到，就要走if里面的逻辑，if里面的逻辑一般可能是我们自己指定classpath的路径下去寻找的逻辑
    if (source == null && systemId != null) {
        String resourcePath = null;
        try {
            String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
            String givenUrl = new URL(decodedSystemId).toString();
            String systemRootUrl = new File("").toURI().toURL().toString();
            // Try relative to resource base if currently in system root.
            if (givenUrl.startsWith(systemRootUrl)) {
                resourcePath = givenUrl.substring(systemRootUrl.length());
            }
        }
        catch (Exception ex) {
            // Typically a MalformedURLException or AccessControlException.
            if (logger.isDebugEnabled()) {
                logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
            }
            // No URL (or no resolvable URL) -> try relative to resource base.
            resourcePath = systemId;
        }
        if (resourcePath != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
            }
            Resource resource = this.resourceLoader.getResource(resourcePath);
            source = new InputSource(resource.getInputStream());
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            if (logger.isDebugEnabled()) {
                logger.debug("Found XML entity [" + systemId + "]: " + resource);
            }
        }
    }
    return source;
}




```


### 2.2 DelegatingEntityResolver.resolveEntity(String publicId, String systemId)方法

```

public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    if (systemId != null) {
        if (systemId.endsWith(DTD_SUFFIX)) {
            // 若是dtd，交给BeansDtdResolver
            // 测试1、3走这个
            return this.dtdResolver.resolveEntity(publicId, systemId);
        }
        else if (systemId.endsWith(XSD_SUFFIX)) {
            // 测试2、4走这个
            // 若是xsd，交给PluggableSchemaResolver
            return this.schemaResolver.resolveEntity(publicId, systemId);
        }
    }
    return null;
}


```


### 2.3 BeansDtdResolver.resolveEntity(String publicId, String systemId)方法

```

@Override
public InputSource resolveEntity(String publicId, String systemId) throws IOException {
    if (logger.isTraceEnabled()) {
        logger.trace("Trying to resolve XML entity with public ID [" + publicId +
                "] and system ID [" + systemId + "]");
    }
    // 
    if (systemId != null && systemId.endsWith(DTD_EXTENSION)) {
        int lastPathSeparator = systemId.lastIndexOf("/");
        for (String DTD_NAME : DTD_NAMES) {
            int dtdNameStart = systemId.indexOf(DTD_NAME);
            if (dtdNameStart > lastPathSeparator) {
                // dtdFile=spring-beans-2.0.dtd
                String dtdFile = systemId.substring(dtdNameStart);
                if (logger.isTraceEnabled()) {
                    logger.trace("Trying to locate [" + dtdFile + "] in Spring jar");
                }
                try {
                    
                    Resource resource = new ClassPathResource(dtdFile, getClass());
                    // 断点到这里的时候，可以看下InputSource的byteInput的来源路径
                    // 测试1和3都会是从org/springframework/beans/xml拿到的spring-beans-2.0.dtd
                    InputSource source = new InputSource(resource.getInputStream());
                    source.setPublicId(publicId);
                    source.setSystemId(systemId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
                    }
                    return source;
                }
                catch (IOException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in class path", ex);
                    }
                }

            }
        }
    }

    // Use the default behavior -> download from website or wherever.
    return null;
}

```


### 2.4 PluggableSchemaResolver.resolveEntity(String publicId, String systemId)方法

```


public InputSource resolveEntity(String publicId, String systemId) throws IOException {
    if (logger.isTraceEnabled()) {
        logger.trace("Trying to resolve XML entity with public id [" + publicId +
                "] and system id [" + systemId + "]");
    }
    // 测试2走到这里的时候去org/springframework/beans/xml拿到的spring-beans-2.0.xsd
    // 测试4走到这时候拿不到getSchemaMappings()里没有我们自己写的路径映射，所以这里返或的InputSource是null
    if (systemId != null) {
        // getSchemaMappings()，这个方法就是上图中META-INF/spring.schemas中解析的映射
        // 也就是Spring提供的映射路径，可自行断点查看
        String resourceLocation = getSchemaMappings().get(systemId);
        if (resourceLocation != null) {
            Resource resource = new ClassPathResource(resourceLocation, this.classLoader);
            try {
                InputSource source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
                }
                return source;
            }
            catch (FileNotFoundException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Couldn't find XML schema [" + systemId + "]: " + resource, ex);
                }
            }
        }
    }
    return null;
}

/**
 * Load the specified schema mappings lazily.
 */
private Map<String, String> getSchemaMappings() {
    if (this.schemaMappings == null) {
        synchronized (this) {
            if (this.schemaMappings == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
                }
                try {
                    Properties mappings =
                            PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loaded schema mappings: " + mappings);
                    }
                    Map<String, String> schemaMappings = new ConcurrentHashMap<String, String>(mappings.size());
                    CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
                    this.schemaMappings = schemaMappings;
                }
                catch (IOException ex) {
                    throw new IllegalStateException(
                            "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
                }
            }
        }
    }
    return this.schemaMappings;
}



```

## 3 总结

> EntityResolver:用于提供自定义寻找约本地束文件的(寻找约束文件的目的，是要校验xml文件内的标签的合法性)，自己实现寻找的方法resolveEntity
  Spring是支持自定义标签的，自定义标签需要约束文件，那么这个约束文件的获取，用户就可以自己重写EntityResolver实现，也可以借助PluggableSchemaResolver
  这个以实现的套路，用户需要在ClassPath下，创建META-INF/spring.schemas,写上自己的映射，这里不举例子了
