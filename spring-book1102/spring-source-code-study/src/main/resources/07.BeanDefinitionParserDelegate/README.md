BeanDefinitionParserDelegate(bean定义信息解析代理)

## 1 简介

> 这个类是标签的最终的解析类，主要针对标签上的属性进行解析，最终封装成一个BeanDefinitionHolder返回，
  用于注册到beanFactory。
  
  
## 2 DefaultBeanDefinitionDocumentReader.processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate)方法开始跟踪

```
protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
    // 这里是解析<bean>的入口，将解析的<bean>信息封装到BeanDefinitionHolder对象中
    // 解析
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

### 2.1 BeanefinitionParserDelegate.parseBeanDefinitionElement(Element ele)

```

public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
    // 重载
    return parseBeanDefinitionElement(ele, null);
}




public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean) {
    // 获得id属性
    String id = ele.getAttribute(ID_ATTRIBUTE);
    // 获得name属性
    String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

    List<String> aliases = new ArrayList<String>();
    // name可以同时起多个
    if (StringUtils.hasLength(nameAttr)) {
        String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
        aliases.addAll(Arrays.asList(nameArr));
    }

    String beanName = id;
    if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
        beanName = aliases.remove(0);
        if (logger.isDebugEnabled()) {
            logger.debug("No XML 'id' specified - using '" + beanName +
                    "' as bean name and " + aliases + " as aliases");
        }
    }

    if (containingBean == null) {
        // 校验这个bean是否唯一
        checkNameUniqueness(beanName, aliases, ele);
    }

    // 将bean标签解析为BeanDefinition
    // 这个是核心方法
    AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
    if (beanDefinition != null) {
        if (!StringUtils.hasText(beanName)) {
            try {
                if (containingBean != null) {
                    beanName = BeanDefinitionReaderUtils.generateBeanName(
                            beanDefinition, this.readerContext.getRegistry(), true);
                }
                else {
                    beanName = this.readerContext.generateBeanName(beanDefinition);
                    // Register an alias for the plain bean class name, if still possible,
                    // if the generator returned the class name plus a suffix.
                    // This is expected for Spring 1.2/2.0 backwards compatibility.
                    String beanClassName = beanDefinition.getBeanClassName();
                    if (beanClassName != null &&
                            beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() &&
                            !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
                        aliases.add(beanClassName);
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Neither XML 'id' nor 'name' specified - " +
                            "using generated bean name [" + beanName + "]");
                }
            }
            catch (Exception ex) {
                error(ex.getMessage(), ele);
                return null;
            }
        }
        String[] aliasesArray = StringUtils.toStringArray(aliases);
        // 将解析后的beanDefintion封装在BeanDefinitionHolder对象中
        return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
    }

    return null;
}


// 解析bean标签的最终方法
public AbstractBeanDefinition parseBeanDefinitionElement(
			Element ele, String beanName, BeanDefinition containingBean) {

    this.parseState.push(new BeanEntry(beanName));

    String className = null;
    // 解析class属性
    if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
        className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
    }

    try {
        String parent = null;
        // 解析parent属性
        if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
            parent = ele.getAttribute(PARENT_ATTRIBUTE);
        }
        // 创建一个AbstractBeanDefinition，用于封装bean标签的所有属性
        AbstractBeanDefinition bd = createBeanDefinition(className, parent);

        parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
        bd.setDescription(DomUtils.getChildElementValueByTagName(ele, DESCRIPTION_ELEMENT));
        
        // 解析元素据
        parseMetaElements(ele, bd);
        // 解析lookup-method属性
        parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
        // 解析replaced-method属性
        parseReplacedMethodSubElements(ele, bd.getMethodOverrides());

        // 解析构造函数参数
        parseConstructorArgElements(ele, bd);
        // 解析property子元素
        parsePropertyElements(ele, bd);
        // 解析qualifier子元素
        parseQualifierElements(ele, bd);
        

        bd.setResource(this.readerContext.getResource());
        bd.setSource(extractSource(ele));

        return bd;
    }
    catch (ClassNotFoundException ex) {
        error("Bean class [" + className + "] not found", ele, ex);
    }
    catch (NoClassDefFoundError err) {
        error("Class that bean class [" + className + "] depends on not found", ele, err);
    }
    catch (Throwable ex) {
        error("Unexpected failure during bean definition parsing", ele, ex);
    }
    finally {
        this.parseState.pop();
    }

    return null;
}

以上好多parse方法不一一展开叙述，都在当前类中，基本都是将xml中Node对象解析成出名称和值的一个过程，且都差不多

```


### 2.2 BeanefinitionParserDelegate.decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder)

```

// 主要用于对已存在的默认标签进行自定义或者 自定义标签进行解析
public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder) {
    return decorateBeanDefinitionIfRequired(ele, definitionHolder, null);
}


public BeanDefinitionHolder decorateBeanDefinitionIfRequired(
        Element ele, BeanDefinitionHolder definitionHolder, BeanDefinition containingBd) {

    BeanDefinitionHolder finalDefinition = definitionHolder;

    // Decorate based on custom attributes first.
    NamedNodeMap attributes = ele.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
        Node node = attributes.item(i);
        finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
    }

    // Decorate based on custom nested elements.
    NodeList children = ele.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
        Node node = children.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
        }
    }
    return finalDefinition;
}



public BeanDefinitionHolder decorateIfRequired(
        Node node, BeanDefinitionHolder originalDef, BeanDefinition containingBd) {

    // 解析器或装饰者的的命名空间
    String namespaceUri = getNamespaceURI(node);
    if (!isDefaultNamespace(namespaceUri)) {
        // 获得命名空间处理器
        NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
        if (handler != null) {
            // 进行自定义的属性或标签进行解析并赋值给BeanDefinitionHolder
            
            return handler.decorate(node, originalDef, new ParserContext(this.readerContext, this, containingBd));
        }
        else if (namespaceUri != null && namespaceUri.startsWith("http://www.springframework.org/")) {
            error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", node);
        }
        else {
            // A custom namespace, not to be handled by Spring - maybe "xml:...".
            if (logger.isDebugEnabled()) {
                logger.debug("No Spring NamespaceHandler found for XML schema namespace [" + namespaceUri + "]");
            }
        }
    }
    return originalDef;
}

```
    
> BeanDefinitionParserDelegate是解析标签的最终类，内容比较多。到这里解析的模块基本上就结束了。
