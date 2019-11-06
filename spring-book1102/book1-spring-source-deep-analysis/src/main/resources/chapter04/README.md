4 自定义标签的解析



```
#DefaultBeanDefinitionDocumentReader.java 默认标签的解析，分别对(import,alias,bean和beans)做了不同处理
protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
    if (delegate.isDefaultNamespace(root)) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (delegate.isDefaultNamespace(ele)) {
                    parseDefaultElement(ele, delegate);
                }
                else {
                    // 解析自定义标签
                    delegate.parseCustomElement(ele);
                }
            }
        }
    }
    else {
        // 解析自定义标签
        delegate.parseCustomElement(root);
    }
}

```

## 4.1 自定义标签使用

> 扩展Spring自定义标签配置大致需要以下几个步骤:
  1.创建一个需要扩展的组件
  2.定义一个XSD文件描述组件内容。
  3.创建一个文件，实现BeanDefinitionParser接口，用来解析XSD文件中的定义和组件定义。
  4.创建一个Handler文件，扩展自NamespaceHandlerSupport,目的是将组件注册到Spring容器。
  5.编写Spring.handlers和Spring.schemas文件
  
  
```

1.在com.spring.book.study.chapter04下创建User类
package com.spring.book.study.chapter04;

public class User {

    private String userName;

    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

2.在src/main/resources下创建META-INF文件，且创建Spring-test.xsd

<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://www.w3.org/2001/XMLSchema"
        targetNamespace="http://www.lexueba.com/schema/user"
        xmlns:tns="http://www.lexueba.com/schema/user"
        elementFormDefault="qualified">
    <element name="user">
        <complexType>
            <attribute name="id" type="string"/>
            <attribute name="userName" type="string"/>
            <attribute name="email" type="string"/>
        </complexType>
    </element>

</schema>

3.在com.spring.book.study.chapter04创建BeanDefinitionParser解析类
package com.spring.book.study.chapter04;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return User.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        String userName = element.getAttribute("userName");
        String email = element.getAttribute("email");

        if (StringUtils.hasText(userName)) {
            bean.addPropertyValue("userName", userName);
        }
        if (StringUtils.hasText(email)) {
            bean.addPropertyValue("email", email);
        }


    }
}

4.在com.spring.book.study.chapter04创建MyNamespaceHandler,将组件注册到Spring容器中

package com.spring.book.study.chapter04;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        /**
         * 当遇到自定义标签<user:aaa>这样类似以user开头的元素，就会把这个元素扔给UserBeanDefinitionParser处理
         */
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}

5.在META-INF中创建Spring.handlers和Spring.schemas文件
Spring.handlers内容：http\://www.lexueba.com/schema/user=com.spring.book.study.chapter04.MyNamespaceHandler
Spring.schemas内容：http\://www.lexueba.com/schema/user.xsd=META-INF/Spring-test.xsd

6.在src/main/resources/下创建customTagTest.xml,注意添加约束文件路径xsd
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:myname="http://www.lexueba.com/schema/user"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.lexueba.com/schema/user http://www.lexueba.com/schema/user.xsd">

    <myname:user id="testbean" userName="aaa" email="bbb"/>

</beans>

7.创建Main测试类
package com.spring.book.study.chapter04;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ap = new ClassPathXmlApplicationContext("customTagTest.xml");
        User user = (User) ap.getBean("testbean");
        // User{userName='aaa', email='bbb'}
        System.out.println(user);

    }

}

```

## 4.2 自定义标签解析


```
# BeanDefinitionParserDelegate.java 解析自定义标签
public BeanDefinition parseCustomElement(Element ele) {
    return parseCustomElement(ele, null);
}

public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
    // 获取对应的命名空间
    String namespaceUri = getNamespaceURI(ele);
    // 根据命名空间找到对应的NamespaceHandler
    NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
    if (handler == null) {
        error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
        return null;
    }
    // 调用自定义的NamespaceHandler解析
    return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
}

```

### 4.2.1 获取标签的命名空间

```
标签的解析是从命名孔家的提起开始的，无论是区分Spring中默认标签和自定义标签还是区分自定义标签中不同标签的
处理器都是以标签锁提供的命名空间为基础的，而至于如何提取对应元素的命名空间其实并不需要我们亲自去实现，下面
org.w3c.dom.Node中已经提供了方法供我们直接调用
public String getNamespaceURI(Node node) {
    return node.getNamespaceURI();
}
```

### 4.2.2 提取自定义标签处理器

```

NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
在readerContext初始化的时候其属性namespaceHandlerResolver已经被初始化为了DefaultNamespaceHandlerResolver的实例。

public NamespaceHandler resolve(String namespaceUri) {
    // 获取所有已经配置的handler映射
    Map<String, Object> handlerMappings = getHandlerMappings();
    // 根据命名空间找到对应的信息
    Object handlerOrClassName = handlerMappings.get(namespaceUri);
    if (handlerOrClassName == null) {
        return null;
    }
    else if (handlerOrClassName instanceof NamespaceHandler) {
        // 已经做过解析的情况，直接从缓存读取
        return (NamespaceHandler) handlerOrClassName;
    }
    else {
        // 没有做过解析，则返回的是类路径
        String className = (String) handlerOrClassName;
        try {
            // 使用反射将类路劲转化为类
            Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
            if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
                throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri +
                        "] does not implement the [" + NamespaceHandler.class.getName() + "] interface");
            }
            // 初始化类
            NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils.instantiateClass(handlerClass);
            // 调用自定义的NamespaceHandler的初始化方法
            namespaceHandler.init();
            // 记录在缓存
            handlerMappings.put(namespaceUri, namespaceHandler);
            return namespaceHandler;
        }
        catch (ClassNotFoundException ex) {
            throw new FatalBeanException("NamespaceHandler class [" + className + "] for namespace [" +
                    namespaceUri + "] not found", ex);
        }
        catch (LinkageError err) {
            throw new FatalBeanException("Invalid NamespaceHandler class [" + className + "] for namespace [" +
                    namespaceUri + "]: problem with handler class file or dependent class", err);
        }
    }
}

#借助了工具类PropertiesLoaderUtils对属性handler
private Map<String, Object> getHandlerMappings() {
    // 如果没有缓存则开始进行缓存
    if (this.handlerMappings == null) {
        synchronized (this) {
            if (this.handlerMappings == null) {
                try {
                    // this.handlerMappingsLocation在构造函数中已经被初始化:META-INF/Spring.handlers
                    Properties mappings =
                            PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loaded NamespaceHandler mappings: " + mappings);
                    }
                    Map<String, Object> handlerMappings = new ConcurrentHashMap<String, Object>(mappings.size());
                    // 将Properties格式文件合并到Map格式的handlerMappings中
                    CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
                    this.handlerMappings = handlerMappings;
                }
                catch (IOException ex) {
                    throw new IllegalStateException(
                            "Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", ex);
                }
            }
        }
    }
    return this.handlerMappings;
}



```

### 4.2.3 标签解析

```
# 自定义的解析器，进行解析
handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));

# NamespaceHandlerSupport.java
public BeanDefinition parse(Element element, ParserContext parserContext) {
    return findParserForElement(element, parserContext).parse(element, parserContext);
}


private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
    // 获取元素名称，也就是<myname:user> 中的user，若实例中，此时localName为user
    String localName = parserContext.getDelegate().getLocalName(element);
    // 根据user找到对应的解析器，也就是在registerBeanDefinitionParser("user",new UserBeanDefinitionParser()); 注册的解析器
    BeanDefinitionParser parser = this.parsers.get(localName);
    if (parser == null) {
        parserContext.getReaderContext().fatal(
                "Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
    }
    return parser;
}

# AbstractBeanDefinitionParser.java
public final BeanDefinition parse(Element element, ParserContext parserContext) {
    AbstractBeanDefinition definition = parseInternal(element, parserContext);
    if (definition != null && !parserContext.isNested()) {
        try {
            String id = resolveId(element, definition, parserContext);
            if (!StringUtils.hasText(id)) {
                parserContext.getReaderContext().error(
                        "Id is required for element '" + parserContext.getDelegate().getLocalName(element)
                                + "' when used as a top-level tag", element);
            }
            String[] aliases = null;
            if (shouldParseNameAsAliases()) {
                String name = element.getAttribute(NAME_ATTRIBUTE);
                if (StringUtils.hasLength(name)) {
                    aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
                }
            }
            // 将AbstractBeanDefinition转换为BeanDefinitionHolder并注册
            BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, id, aliases);
            registerBeanDefinition(holder, parserContext.getRegistry());
            if (shouldFireEvents()) {
                // 需要通知监听器则进行处理
                BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
                postProcessComponentDefinition(componentDefinition);
                parserContext.registerComponent(componentDefinition);
            }
        }
        catch (BeanDefinitionStoreException ex) {
            parserContext.getReaderContext().error(ex.getMessage(), element);
            return null;
        }
    }
    return definition;
}



# AbstractSingleBeanDefinitionParser.java 
protected final AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
    
    
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
    String parentName = getParentName(element);
    if (parentName != null) {
        builder.getRawBeanDefinition().setParentName(parentName);
    }
    // 获取自定义标签中的class，此时会调用自定义解析器如UserBeanDefinitionParser中的getBeanClass方法
    Class<?> beanClass = getBeanClass(element);
    if (beanClass != null) {
        builder.getRawBeanDefinition().setBeanClass(beanClass);
    }
    else {
        // 若子类没有重写getBeanClass方法则尝试检查子类是否重写getBeanClassName方法
        String beanClassName = getBeanClassName(element);
        if (beanClassName != null) {
            builder.getRawBeanDefinition().setBeanClassName(beanClassName);
        }
    }
    builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
    if (parserContext.isNested()) {
        // 若存在父类则使用父类的scope属性
        // Inner bean definition must receive same scope as containing bean.
        builder.setScope(parserContext.getContainingBeanDefinition().getScope());
    }
    if (parserContext.isDefaultLazyInit()) {
        // Default-lazy-init applies to custom bean definitions as well.
        // 配置延迟加载
        builder.setLazyInit(true);
    }
    doParse(element, parserContext, builder);
    return builder.getBeanDefinition();
}


protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    doParse(element, builder);
}


```





