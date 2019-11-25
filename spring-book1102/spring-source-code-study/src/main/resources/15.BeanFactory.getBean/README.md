BeanFactory.getBean()(bean工厂getBean方法)


```

@Test
public void testXmlBeanFactory(){
    XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("13.BeanFactory/beanFactoryTest.xml"));
    // 断点跟踪getBean方法
    Object demo = beanFactory.getBean("demo");
    // class com.spring.source.code.study.BeanFactory.Demo
    System.out.println(demo.getClass());
}


```


## 1 AbstractBeanFactory.getBean(String name)方法跟踪

```
// 有好几个getBean重载的方法，最终调用的都是doGetBean方法
public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}

// name：bean名称
// requiredType:接口或者类的类型
// args: 为获得bean赋值的参数
// typeCheckOnly：是否检查类型
protected <T> T doGetBean(
        final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
        throws BeansException {
    // 这个方法解析传入的name：name可能值
    // 1.就是bean的名称
    // 2.bean的别名,需要根据别名要拿到beanName
    // 3.这个bean不是原生的bean，是FactoryBean，FactoryBean是对bean的包装：获取的方式"&beanName"
    // 这个方法就是针对上述的3中情况进行的处理，其中1情况是正常情况，其他两种是以外需要特殊处理
    final String beanName = transformedBeanName(name);
    Object bean;

    // Eagerly check singleton cache for manually registered singletons.
    // 对于手动注册的单例,提前检查单例缓存
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isDebugEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.debug("Returning eagerly cached instance of singleton bean '" + beanName +
                        "' that is not fully initialized yet - a consequence of a circular reference");
            }
            else {
                logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
            }
        }
        // 主要处理FactoryBean的问题，根据用户是取bean还是取FactoryBean进行相关的处理
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }

    else {
        // Fail if we're already creating this bean instance:
        // We're assumably within a circular reference.
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }

        // Check if bean definition exists in this factory.
        // 尝试从父工厂中获取
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // Not found -> check parent.
            String nameToLookup = originalBeanName(name);
            if (args != null) {
                // Delegation to parent with explicit args.
                return (T) parentBeanFactory.getBean(nameToLookup, args);
            }
            else {
                // No args -> delegate to standard getBean method.
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
        }

        if (!typeCheckOnly) {
            // 这个标记bean已经创建过
            // 这个在BeacFactoryPostProcessor那里就有涉及
            // 其实是在，Set集合中添加这beanName
            markBeanAsCreated(beanName);
        }

        try {
            // 根据beanName获得BeanDefinition
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            // Guarantee initialization of beans that the current bean depends on.
            // 处理依赖的问题
            String[] dependsOn = mbd.getDependsOn();
            if (dependsOn != null) {
                for (String dependsOnBean : dependsOn) {
                    if (isDependent(beanName, dependsOnBean)) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "Circular depends-on relationship between '" + beanName + "' and '" + dependsOnBean + "'");
                    }
                    // 将依赖的beanName注册到依赖管理的容器中
                    registerDependentBean(dependsOnBean, beanName);
                    // 递归调用，获得依赖的bean，目的是创建依赖的bean
                    getBean(dependsOnBean);
                }
            }

            // Create bean instance.
            // 这里是才是实例化bean的地方
            if (mbd.isSingleton()) {
                // singleton的创建
                // 又一个getSingleton();只是这个方法是上一个同名方法的重载，这个传入一个ObjectFactory对象
                sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        try {
                            // 创建bean
                            return createBean(beanName, mbd, args);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            destroySingleton(beanName);
                            throw ex;
                        }
                    }
                });
                // 处理FactoryBean
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }

            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                // prototype创建新实例
                Object prototypeInstance = null;
                try {
                    beforePrototypeCreation(beanName);
                    prototypeInstance = createBean(beanName, mbd, args);
                }
                finally {
                    afterPrototypeCreation(beanName);
                }
                // 处理FactoryBean
                bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
            }

            else {
                String scopeName = mbd.getScope();
                final Scope scope = this.scopes.get(scopeName);
                if (scope == null) {
                    throw new IllegalStateException("No Scope registered for scope '" + scopeName + "'");
                }
                try {
                    Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
                        @Override
                        public Object getObject() throws BeansException {
                            beforePrototypeCreation(beanName);
                            try {
                                return createBean(beanName, mbd, args);
                            }
                            finally {
                                afterPrototypeCreation(beanName);
                            }
                        }
                    });
                    bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                }
                catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName,
                            "Scope '" + scopeName + "' is not active for the current thread; " +
                            "consider defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                            ex);
                }
            }
        }
        catch (BeansException ex) {
            cleanupAfterBeanCreationFailure(beanName);
            throw ex;
        }
    }

    // Check if required type matches the type of the actual bean instance.
    if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
        try {
            return getTypeConverter().convertIfNecessary(bean, requiredType);
        }
        catch (TypeMismatchException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to convert bean '" + name + "' to required type [" +
                        ClassUtils.getQualifiedName(requiredType) + "]", ex);
            }
            throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
        }
    }
    return (T) bean;
}


```



## 1.1 AbstractBeanFactory.transformedBeanName(name)方法跟踪


```

protected String transformedBeanName(String name) {
    return canonicalName(BeanFactoryUtils.transformedBeanName(name));
}


// BeanFactoryUtils.transformedBeanName(String name)
// 处理factoryBean的情况
public static String transformedBeanName(String name) {
    Assert.notNull(name, "'name' must not be null");
    String beanName = name;
    // beanName开头是"&"
    while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
        beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
    }
    return beanName;
}



// SimpleAliasRegistry.canonicalName(String name)
// SimpleAliasRegistry这个之前介绍过，就是用于存bean的别名的
// AbstractBeanFactory间接继承SimpleAliasRegistry
public String canonicalName(String name) {
    String canonicalName = name;
    // Handle aliasing...
    String resolvedName;    
    do {
        // 循环拿到beanName
        resolvedName = this.aliasMap.get(canonicalName);
        if (resolvedName != null) {
            canonicalName = resolvedName;
        }
    }
    while (resolvedName != null);
    return canonicalName;
}


```

**FactoryBean demo**


```

@Test
public void testFactoryBean() {
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("13.BeanFactory/beanFactoryTest.xml");
    // 这个获得的就是正常的bean
    Object studentFactoryBean = app.getBean("studentFactoryBean");
    // class com.spring.source.code.study.BeanFactory.Student
    System.out.println(studentFactoryBean.getClass());
    // "&"获得的是StudentFactoryBean
    Object student = app.getBean("&studentFactoryBean");
    //class com.spring.source.code.study.BeanFactory.StudentFactoryBean
    System.out.println(student.getClass());
}


```


**GetBeanByAlisaName demo**

```
@Test
public void testGetBeanByAlisaName() {

    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("13.BeanFactory/beanFactoryTest.xml");
    // 这个获得的就是正常的bean
    Object demo1 = app.getBean("demo1");
    Object demo2 = app.getBean("demo2");
    // 这个demo3的获取，就是do.. while循环的作用
    // demo3拿到demo1，然后demo1再拿到demo
    Object demo3 = app.getBean("demo3");
    Object demo = app.getBean("demo");
    System.out.println(demo1.getClass());
    System.out.println(demo2.getClass());
    System.out.println(demo3.getClass());
    System.out.println(demo.getClass());
}

```





## 1.2 DefaultSingletonBeanRegistry.getSingleton(String beanName)

```
// DefaultSingletonBeanRegistry后面再详细介绍
// 获得单例实例的bean
public Object getSingleton(String beanName) {
    return getSingleton(beanName, true);
}

/*
 * DefaultSingletonBeanRegistry类中的map缓存的作用
 * private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
 * singletonObjects就是存放单例bean的容器
 * private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);
 * singletonFactories这个是存放ObjectFactory的对象的容器，这个ObjectFactory其实是对单例bean的包装，延迟实例化的作用
 * private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);
 * earlySingletonObjects，提前暴露的单例bean的容器，作用是用于解决循环依赖的问题
 * 
 * 这三者的关系，singletonObjects若是他中没有值，会去earlySingletonObjects中取值，二者都没值说明值可能在ObjectFactory中，
 * 于是去singletonFactories中取到ObjectFactory，需要调用ObjectFactory.getObject()方法区实例化，
 * 这个实例化的后获得的bean会放在earlySingletonObjects中，同时，会将singletonFactories中的ObjectFactory对象去除掉，
 * 也就是说涉及到循环循环引用的单例bean一般存放在earlySingletonObjects中，没有循环依赖的就放在了singletonObjects中
 *
 */
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    Object singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        synchronized (this.singletonObjects) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    return (singletonObject != NULL_OBJECT ? singletonObject : null);
}

```


## 1.3 AbstractBeanFactory.getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)


```
// beanInstance：已经获得到的bean实例
// name：可能是"&beanName"
// beanName：bean名称
// mbd: RootBeanDefinition
protected Object getObjectForBeanInstance(
        Object beanInstance, String name, String beanName, RootBeanDefinition mbd) {

    // Don't let calling code try to dereference the factory if the bean isn't a factory.
    if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
        throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
    }

    // Now we have the bean instance, which may be a normal bean or a FactoryBean.
    // If it's a FactoryBean, we use it to create a bean instance, unless the
    // caller actually wants a reference to the factory.
    // 判断beanInstance不是FactoryBean，并且name不是"&beanNane",说明不是取FactoryBean，则直接返回
    if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
        return beanInstance;
    }
    
    // 走到这里，说明就是FactoryBean了
    Object object = null;
    if (mbd == null) {
        // 从FactoryBean缓存中拿到bean实例
        object = getCachedObjectForFactoryBean(beanName);
    }
    if (object == null) {
        // Return bean instance from factory.
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        // Caches object obtained from FactoryBean if it is a singleton.
        if (mbd == null && containsBeanDefinition(beanName)) {
            mbd = getMergedLocalBeanDefinition(beanName);
        }
        // 从FactoryBean中拿bean实例
        boolean synthetic = (mbd != null && mbd.isSynthetic());
        object = getObjectFromFactoryBean(factory, beanName, !synthetic);
    }
    return object;
}


// FactoryBeanRegistrySupport.getCachedObjectForFactoryBean(String beanName)
// FactoryBeanRegistrySupport是AbstractBeanFactory的父类
protected Object getCachedObjectForFactoryBean(String beanName) {
    // 缓存bean的FactoryBean容器
    // beanName->bean实例，键值对
    Object object = this.factoryBeanObjectCache.get(beanName);
    return (object != NULL_OBJECT ? object : null);
}


// FactoryBeanRegistrySupport.getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess)
// FactoryBeanRegistrySupport是AbstractBeanFactory的父类
protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
    if (factory.isSingleton() && containsSingleton(beanName)) {
        synchronized (getSingletonMutex()) {
            // 再次从factoryBeanObjectCache缓存中获得bean
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                // 这个是FactoryBean.getObject()返回的bean
                object = doGetObjectFromFactoryBean(factory, beanName);
                // Only post-process and store if not put there already during getObject() call above
                // (e.g. because of circular reference processing triggered by custom getBean calls)
                // 例如：循环引用调用的引起的问题，就是说，循环依赖调用doGetObjectFromFactoryBean和其他方式getBean代码走到这里的时候
                // 为了避免FactoryBean.getObject()不会创建两个的问题，还是单例的问题，所以就再次从factoryBeanObjectCache中拿去
                // 有点double-check的作用，目的就是保证是单例的，这里加了同步，按理说不存在多线程的问题，那么问题就是，同一个线程，
                // 在循环依赖的过程中会出现多次创建的风险，具体场景怎样，笔者也不明朗，于是就有了这个再次获取的操作
                Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
                if (alreadyThere != null) {
                    object = alreadyThere;
                }
                else {
                    if (object != null && shouldPostProcess) {
                        try {
                            // bean的自定义处理等，前面讲过BeanFactoryProcessor，这里应该是BeanProcessor的处理
                            // 默认没有处理，这个方法，那么可能需要用户自定义
                            object = postProcessObjectFromFactoryBean(object, beanName);
                        }
                        catch (Throwable ex) {
                            throw new BeanCreationException(beanName,
                                    "Post-processing of FactoryBean's singleton object failed", ex);
                        }
                    }
                    this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
                }
            }
            return (object != NULL_OBJECT ? object : null);
        }
    }
    else {
        Object object = doGetObjectFromFactoryBean(factory, beanName);
        if (object != null && shouldPostProcess) {
            try {
                object = postProcessObjectFromFactoryBean(object, beanName);
            }
            catch (Throwable ex) {
                throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
            }
        }
        return object;
    }
}





private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName)
			throws BeanCreationException {

    Object object;
    try {
        if (System.getSecurityManager() != null) {
            // 这里系统安全检验的逻辑，不展开
            // 校验通过就执行里面的代码，
            AccessControlContext acc = getAccessControlContext();
            try {
                object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                    @Override
                    public Object run() throws Exception {
                            return factory.getObject();
                        }
                    }, acc);
            }
            catch (PrivilegedActionException pae) {
                throw pae.getException();
            }
        }
        else {
            // 直接调用getObject()方法，返回目标bean，StudentFactoryBean中getObject()方法返回的Student
            object = factory.getObject();
        }
    }
    catch (FactoryBeanNotInitializedException ex) {
        throw new BeanCurrentlyInCreationException(beanName, ex.toString());
    }
    catch (Throwable ex) {
        throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
    }

    // Do not accept a null value for a FactoryBean that's not fully
    // initialized yet: Many FactoryBeans just return null then.
    if (object == null && isSingletonCurrentlyInCreation(beanName)) {
        throw new BeanCurrentlyInCreationException(
                beanName, "FactoryBean which is currently in creation returned null from getObject");
    }
    return object;
}

```



## 1.4 DefaultSingletonBeanRegistry.getObjectForBeanInstance(Object beanInstance, String name, String beanName, RootBeanDefinition mbd)


```

/**
 * Register a dependent bean for the given bean,
 * to be destroyed before the given bean is destroyed.
 * @param beanName the name of the bean
 * @param dependentBeanName the name of the dependent bean
 */
public void registerDependentBean(String beanName, String dependentBeanName) {
    // A quick check for an existing entry upfront, avoiding synchronization...
    String canonicalName = canonicalName(beanName);
    // 直接从依赖的容器中尝试获取依赖的bean set集合
    Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
    if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
        return;
    }

    // No entry yet -> fully synchronized manipulation of the dependentBeans Set
    // 没有的话就，将其dependentBeanName加入到集合中
    // 一个bean依赖了哪些bean
    synchronized (this.dependentBeanMap) {
        dependentBeans = this.dependentBeanMap.get(canonicalName);
        if (dependentBeans == null) {
            dependentBeans = new LinkedHashSet<String>(8);
            this.dependentBeanMap.put(canonicalName, dependentBeans);
        }
        dependentBeans.add(dependentBeanName);
    }
    // 这里是处理被依赖的bean和bean之间反过来映射关系维护，正好和dependentBeanMap相反
    // 作用，某一个bean被哪些bean所引用
    synchronized (this.dependenciesForBeanMap) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
        if (dependenciesForBean == null) {
            dependenciesForBean = new LinkedHashSet<String>(8);
            this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
        }
        dependenciesForBean.add(canonicalName);
    }
}



```



## 1.5 AbstractAutowireCapableBeanFactory.createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)

```

protected Object createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
        throws BeanCreationException {

    if (logger.isDebugEnabled()) {
        logger.debug("Creating instance of bean '" + beanName + "'");
    }
    // Make sure bean class is actually resolved at this point.
    // 确保在此刻beanClass已经解析过了，不在展开，主要是校验的作用
    resolveBeanClass(mbd, beanName);

    // Prepare method overrides.
    try {
        // 准备覆写的方法
        mbd.prepareMethodOverrides();
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanDefinitionStoreException(mbd.getResourceDescription(),
                beanName, "Validation of method overrides failed", ex);
    }

    try {
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        // 调用bean的BeanPostProcessor(后置处理器方法)，这个后置处理器和bean工厂的BeanFactoryPostProcessor一样的套路
        Object bean = resolveBeforeInstantiation(beanName, mbd);
        if (bean != null) {
            return bean;
        }
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                "BeanPostProcessor before instantiation of bean failed", ex);
    }

    Object beanInstance = doCreateBean(beanName, mbd, args);
    if (logger.isDebugEnabled()) {
        logger.debug("Finished creating instance of bean '" + beanName + "'");
    }
    return beanInstance;
}



protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
    Object bean = null;
    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
        // Make sure bean class is actually resolved at this point.
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            // 确定给定bean定义的目标类型
            Class<?> targetType = determineTargetType(beanName, mbd);
            if (targetType != null) {
                // 调用bean的后置处理器InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation方法
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    // 
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
        }
        mbd.beforeInstantiationResolved = (bean != null);
    }
    return bean;
}


// 调用bean的后置处理器InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation方法
protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName)
			throws BeansException {

    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
            if (result != null) {
                return result;
            }
        }
    }
    return null;
}

// 调用所有的BeanPostProcessor的postProcessAfterInitialization方法
public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
        throws BeansException {

    Object result = existingBean;
    for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
        result = beanProcessor.postProcessAfterInitialization(result, beanName);
        if (result == null) {
            return result;
        }
    }
    return result;
}


```


## 1.6 AbstractAutowireCapableBeanFactory.doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)

```

protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args) {
    // Instantiate the bean.
    // BeanWrapper是对bean的包装类
    BeanWrapper instanceWrapper = null;
    if (mbd.isSingleton()) {
        instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
    }
    if (instanceWrapper == null) {
        // 创建bean
        instanceWrapper = createBeanInstance(beanName, mbd, args);
    }
    final Object bean = (instanceWrapper != null ? instanceWrapper.getWrappedInstance() : null);
    Class<?> beanType = (instanceWrapper != null ? instanceWrapper.getWrappedClass() : null);

    // Allow post-processors to modify the merged bean definition.
    synchronized (mbd.postProcessingLock) {
        if (!mbd.postProcessed) {
            // 调用MergedBeanDefinitionPostProcessor后置处理的方法，对beanDefinition信息进行合并操作
            applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
            mbd.postProcessed = true;
        }
    }

    // Eagerly cache singletons to be able to resolve circular references
    // even when triggered by lifecycle interfaces like BeanFactoryAware.
    // 提前缓存单例和ObjectFactory，解决循环依赖的问题
    boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
            isSingletonCurrentlyInCreation(beanName));
    if (earlySingletonExposure) {
        if (logger.isDebugEnabled()) {
            logger.debug("Eagerly caching bean '" + beanName +
                    "' to allow for resolving potential circular references");
        }
        addSingletonFactory(beanName, new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                // 这个方法后续分析，这章内容太多
                return getEarlyBeanReference(beanName, mbd, bean);
            }
        });
    }

    // Initialize the bean instance.
    // 初始化bean的实例
    Object exposedObject = bean;
    try {
        // 为bean赋值
        populateBean(beanName, mbd, instanceWrapper);
        if (exposedObject != null) {
            exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
    }
    catch (Throwable ex) {
        if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
            throw (BeanCreationException) ex;
        }
        else {
            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        }
    }

    if (earlySingletonExposure) {
        Object earlySingletonReference = getSingleton(beanName, false);
        if (earlySingletonReference != null) {
            if (exposedObject == bean) {
                exposedObject = earlySingletonReference;
            }
            else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                String[] dependentBeans = getDependentBeans(beanName);
                Set<String> actualDependentBeans = new LinkedHashSet<String>(dependentBeans.length);
                for (String dependentBean : dependentBeans) {
                    if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
                        actualDependentBeans.add(dependentBean);
                    }
                }
                if (!actualDependentBeans.isEmpty()) {
                    throw new BeanCurrentlyInCreationException(beanName,
                            "Bean with name '" + beanName + "' has been injected into other beans [" +
                            StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
                            "] in its raw version as part of a circular reference, but has eventually been " +
                            "wrapped. This means that said other beans do not use the final version of the " +
                            "bean. This is often the result of over-eager type matching - consider using " +
                            "'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
                }
            }
        }
    }

    // Register bean as disposable.
    try {
        registerDisposableBeanIfNecessary(beanName, bean, mbd);
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
    }

    return exposedObject;
}



// 整个方法是用不同的方式来创建bean
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) {
		// Make sure bean class is actually resolved at this point.
    Class<?> beanClass = resolveBeanClass(mbd, beanName);

    if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                "Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
    }

    if (mbd.getFactoryMethodName() != null)  {
        使用FactoryMethod方式
        return instantiateUsingFactoryMethod(beanName, mbd, args);
    }

    // Shortcut when re-creating the same bean...
    boolean resolved = false;
    boolean autowireNecessary = false;
    if (args == null) {
        synchronized (mbd.constructorArgumentLock) {
            if (mbd.resolvedConstructorOrFactoryMethod != null) {
                resolved = true;
                // 构造方法已经解析过
                autowireNecessary = mbd.constructorArgumentsResolved;
            }
        }
    }
    if (resolved) {
        if (autowireNecessary) {
            // 使用构造器自动注入方式
            return autowireConstructor(beanName, mbd, null, null);
        }
        else {
            // 默认的初始化方法
            return instantiateBean(beanName, mbd);
        }
    }

    // Need to determine the constructor...
    // 解析构造方法，决定用哪一个构造方法
    Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
    if (ctors != null ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
            mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
        return autowireConstructor(beanName, mbd, ctors, args);
    }

    // No special handling: simply use no-arg constructor.
    // 无特殊处理，用默认的构造方法
    return instantiateBean(beanName, mbd);
}


protected BeanWrapper instantiateUsingFactoryMethod(
			String beanName, RootBeanDefinition mbd, Object[] explicitArgs) {
    return new ConstructorResolver(this).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
}


protected BeanWrapper autowireConstructor(
        String beanName, RootBeanDefinition mbd, Constructor<?>[] ctors, Object[] explicitArgs) {
    return new ConstructorResolver(this).autowireConstructor(beanName, mbd, ctors, explicitArgs);
}


// 初始化bean，这里不再深入
protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
    try {
        Object beanInstance;
        final BeanFactory parent = this;
        if (System.getSecurityManager() != null) {
            beanInstance = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    return getInstantiationStrategy().instantiate(mbd, beanName, parent);
                }
            }, getAccessControlContext());
        }
        else {
            beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
        }
        BeanWrapper bw = new BeanWrapperImpl(beanInstance);
        initBeanWrapper(bw);
        return bw;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Instantiation of bean failed", ex);
    }
}



DefaultSingletonBeanRegistry.addSingletonFactory

protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(singletonFactory, "Singleton factory must not be null");
    synchronized (this.singletonObjects) {
        if (!this.singletonObjects.containsKey(beanName)) {
            // 将singletonFactory加入到singletonFactories中
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
            // 已注册的单例也添加beanName
            this.registeredSingletons.add(beanName);
        }
    }
}


```
## 1.7 AbstractAutowireCapableBeanFactory.populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw)

```

// 主要是为bean的属性赋值
protected void populateBean(String beanName, RootBeanDefinition mbd, BeanWrapper bw) {
    PropertyValues pvs = mbd.getPropertyValues();

    if (bw == null) {
        if (!pvs.isEmpty()) {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
        }
        else {
            // Skip property population phase for null instance.
            return;
        }
    }

    // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
    // state of the bean before properties are set. This can be used, for example,
    // to support styles of field injection.
    boolean continueWithPropertyPopulation = true;

    if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
    }

    if (!continueWithPropertyPopulation) {
        return;
    }

    if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
        MutablePropertyValues newPvs = new MutablePropertyValues(pvs);

        // Add property values based on autowire by name if applicable.
        if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_NAME) {
            // 通过名称自动注入的
            autowireByName(beanName, mbd, bw, newPvs);
        }

        // Add property values based on autowire by type if applicable.
        if (mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_BY_TYPE) {
            // 通过类型自动注入的
            autowireByType(beanName, mbd, bw, newPvs);
        }

        pvs = newPvs;
    }

    boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
    boolean needsDepCheck = (mbd.getDependencyCheck() != RootBeanDefinition.DEPENDENCY_CHECK_NONE);

    if (hasInstAwareBpps || needsDepCheck) {
        PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
        if (hasInstAwareBpps) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                    if (pvs == null) {
                        return;
                    }
                }
            }
        }
        if (needsDepCheck) {
            checkDependencies(beanName, mbd, filteredPds, pvs);
        }
    }
    // 其他方式的
    applyPropertyValues(beanName, mbd, bw, pvs);
}


```
