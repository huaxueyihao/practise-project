BeanPostProcessor(bean后置处理器)

## 1 简介

### 1.认识

> BeanPostProcessor和BeanFactoryPostProcessor是类似的用途，只是作用对象不一样，
  BeanPostProcessor作用于bean，BeanFactoryPostProcessor作用于beanFactory；
  也就是前者用于对bean做出自定义的调整，后者是对beanFactory做出自定义的调整。
  BeanFactoryPostProcessor里面就一个方法：
  void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
  
  
```

// 两个方法
public interface BeanPostProcessor {

	// 在初始化之前调用
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    // 在初始化之后调用
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}

```

### 1.2 类关系

![avatar](images/01_beanPostProcessor_level.jpg)

> 可以看到，Spring中该类的子类就不少，其中还有与切面相关的后置处理器


## 2 测试

> demo

```

// 自定义的processor
package com.spring.source.code.study.BeanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

public class MyBeanPostProcessor implements BeanPostProcessor {


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization before :" + beanName + "=" + bean.toString() + "   " + bean.getClass());

        if (bean instanceof Cat) {
            Cat cat = (Cat) bean;
            cat.setColor("white");
            cat.setKind("bigFlowerCat");
            bean = cat;
        }
        System.out.println("postProcessBeforeInitialization after :" + beanName + "=" + bean.toString() + "   " + bean.getClass());

        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization before :" + beanName + "=" + bean.toString() + "   " + bean.getClass());
        if (bean instanceof Cat) {
            Cat cat = (Cat) bean;
            cat.setColor("yellow");
            cat.setKind("orangeCat");
            bean = cat;
        }
        System.out.println("postProcessAfterInitialization after :" + beanName + "=" + bean.toString() + "   " + bean.getClass());

        return bean;
    }

}


// Cat类
package com.spring.source.code.study.BeanPostProcessor;

public class Cat {

    private String color;

    private String kind;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "color='" + color + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}

// xml配置文件

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">


    <bean id="cat" class="com.spring.source.code.study.BeanPostProcessor.Cat">
        <property name="color" value="red"></property>
        <property name="kind" value="orangeCat"></property>
    </bean>

    <bean id="myBeanPostProcessor" class="com.spring.source.code.study.BeanPostProcessor.MyBeanPostProcessor"></bean>


</beans>

// 测试类

 @Test
public void testBeanPostProcessor(){
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("16.BeanPostProcessor/beanPostProcessorTest.xml");

    /**
     * postProcessBeforeInitialization before :cat=Cat{color='red', kind='orangeCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
     * postProcessBeforeInitialization after :cat=Cat{color='white', kind='bigFlowerCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
     * postProcessAfterInitialization before :cat=Cat{color='white', kind='bigFlowerCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
     * postProcessAfterInitialization after :cat=Cat{color='yellow', kind='orangeCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
     * Cat{color='yellow', kind='orangeCat'}
     */
    System.out.println(app.getBean("cat"));

}



```

> 可以看到，两个方法都是容器启动过程中执行的过程。在postProcessBeforeInitialization这个方法之前，其实Cat已经实例化好了，
  且postProcessAfterInitialization在postProcessBeforeInitialization之后，那这两个方法有什么区别，都是在bean实例化后调用？
 
 
### 2.1 由AbstractApplicationContext.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)方法跟踪
  
```
// 注册beanPostProcessors到beanFactory
protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    // 这里的注册方法和BeanFactoryPostProcessor的注册是同一个类里的不通方法
    PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
}


```

#### 2.1.1 由PostProcessorRegistrationDelegate.registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) 方法跟踪



```

// 这个beanPostProcessor只是注册到beanFactory上，而BeanFactoryPostProcessor的获取之后，不是为了注册，而是直接调用方法。
// beanPostProcessor的调用还在AbstractApplicationContext.finishBeanFactoryInitialization(beanFactory)方法中

public static void registerBeanPostProcessors(
        ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
    
    // 这个方法在分析BeanFactoryPostProcessor，时已经分析过，目的获得bean的name
    String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

    // Register BeanPostProcessorChecker that logs an info message when
    // a bean is created during BeanPostProcessor instantiation, i.e. when
    // a bean is not eligible for getting processed by all BeanPostProcessors.
    // 用于校验处理器是个合格的后置处理器
    int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
    beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

    // Separate between BeanPostProcessors that implement PriorityOrdered,
    // Ordered, and the rest.
    // 优先有序的后置处理器
    List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
    // Spring内置的后置处理器
    List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
    // 有序的处理器类
    List<String> orderedPostProcessorNames = new ArrayList<String>();
    // 常规的处理器类，本例就是这类的处理器
    List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
    for (String ppName : postProcessorNames) {
        if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
            // 获得处理器的bean，目的初始化处后置理器类
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            priorityOrderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                // 内置的处理器
                internalPostProcessors.add(pp);
            }
        }
        else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
            orderedPostProcessorNames.add(ppName);
        }
        else {
            nonOrderedPostProcessorNames.add(ppName);
        }
    }

    // First, register the BeanPostProcessors that implement PriorityOrdered.
    // 进行排序
    OrderComparator.sort(priorityOrderedPostProcessors);
    registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

    // Next, register the BeanPostProcessors that implement Ordered.
    // 有序处理器获得
    List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
    for (String ppName : orderedPostProcessorNames) {
        BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
        orderedPostProcessors.add(pp);
        if (pp instanceof MergedBeanDefinitionPostProcessor) {
            internalPostProcessors.add(pp);
        }
    }
    OrderComparator.sort(orderedPostProcessors);
    // 注册到beanFactory上
    registerBeanPostProcessors(beanFactory, orderedPostProcessors);

    // Now, register all regular BeanPostProcessors.
    // 常规的后置处理器获得
    List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
    for (String ppName : nonOrderedPostProcessorNames) {
        BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
        nonOrderedPostProcessors.add(pp);
        if (pp instanceof MergedBeanDefinitionPostProcessor) {
            internalPostProcessors.add(pp);
        }
    }
    // 注册
    registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

    // Finally, re-register all internal BeanPostProcessors.
    OrderComparator.sort(internalPostProcessors);
    // 内置处理器注册
    registerBeanPostProcessors(beanFactory, internalPostProcessors);

    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
}




```


### 2.2 AbstractApplicationContext.finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
  
```

protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    // Initialize conversion service for this context.
    // 初始化ConversionService
    if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
            beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
        beanFactory.setConversionService(
                beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
    }

    // Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
    // 初始化LoadTimeWeaverAware的bean，这个是用于切面的，后面回头来讲
    String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
    for (String weaverAwareName : weaverAwareNames) {
        getBean(weaverAwareName);
    }

    // Stop using the temporary ClassLoader for type matching.
    beanFactory.setTempClassLoader(null);

    // Allow for caching all bean definition metadata, not expecting further changes.
    beanFactory.freezeConfiguration();

    // Instantiate all remaining (non-lazy-init) singletons.
    // 初始化非延迟初始化的bean，这里代码进行的后置处理器的应用
    beanFactory.preInstantiateSingletons();
}



```

#### 2.2.1 DefaultListableBeanFactory.preInstantiateSingletons()


```

public void preInstantiateSingletons() throws BeansException {
    if (this.logger.isDebugEnabled()) {
        this.logger.debug("Pre-instantiating singletons in " + this);
    }

    // Iterate over a copy to allow for init methods which in turn register new bean definitions.
    // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
    List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);

    // Trigger initialization of all non-lazy singleton beans...
    // 循环进行初始化
    for (String beanName : beanNames) {
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            // 是否是factoryBean
            if (isFactoryBean(beanName)) {
                final FactoryBean<?> factory = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
                boolean isEagerInit;
                if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                    isEagerInit = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                        @Override
                        public Boolean run() {
                            return ((SmartFactoryBean<?>) factory).isEagerInit();
                        }
                    }, getAccessControlContext());
                }
                else {
                    isEagerInit = (factory instanceof SmartFactoryBean &&
                            ((SmartFactoryBean<?>) factory).isEagerInit());
                }
                if (isEagerInit) {
                    getBean(beanName);
                }
            }
            else {
                // 本例显然走这里的,beanPostProcessor也是在这里调用的
                // 这里的方法在上一章节中已经叙述
                // 下面 直接跳到应用beanPostProcessor方法的地方，就不一一跟踪了
                // 可自行断点跟踪
                getBean(beanName);
            }
        }
    }

    // Trigger post-initialization callback for all applicable beans...
    for (String beanName : beanNames) {
        Object singletonInstance = getSingleton(beanName);
        if (singletonInstance instanceof SmartInitializingSingleton) {
            final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        smartSingleton.afterSingletonsInstantiated();
                        return null;
                    }
                }, getAccessControlContext());
            }
            else {
                smartSingleton.afterSingletonsInstantiated();
            }
        }
    }
}



```

#### 2.2.2 AbstractAutowireCapableBeanFactory.doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)

```
// 可以断点跟踪到这里

protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args) {
    // Instantiate the bean.
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
            // 合并后置处理器
            applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
            mbd.postProcessed = true;
        }
    }

    // Eagerly cache singletons to be able to resolve circular references
    // even when triggered by lifecycle interfaces like BeanFactoryAware.
    // 处理循环依赖的问题
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
                return getEarlyBeanReference(beanName, mbd, bean);
            }
        });
    }

    // Initialize the bean instance.
    Object exposedObject = bean;
    try {
        // 对bean字段进行赋值
        populateBean(beanName, mbd, instanceWrapper);
        if (exposedObject != null) {
            // 初始化操作，后置处理器就在这里面调用的，接着看这个方法
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




protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                invokeAwareMethods(beanName, bean);
                return null;
            }
        }, getAccessControlContext());
    }
    else {
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 这里就是BeanPostProcessor.postProcessBeforeInitialization调用
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        // 在BeanPostProcessor的两个方法前后调用了这个方法，这个方法将二者隔开
        // 若是没有这个方法，那么BeanPostProcessor其实就只需要一个方法
        // 两个方法就是前后两次修改调整的机会
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                (mbd != null ? mbd.getResourceDescription() : null),
                beanName, "Invocation of init method failed", ex);
    }

    if (mbd == null || !mbd.isSynthetic()) {
        // 这里就是BeanPostProcessor.postProcessAfterInitialization调用
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }
    return wrappedBean;
}


```

#### 2.2.3 AbstractAutowireCapableBeanFactory.invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)


```

protected void invokeInitMethods(String beanName, final Object bean, RootBeanDefinition mbd)
        throws Throwable {
    
    // 判断bean的类型是否是InitializingBean
    boolean isInitializingBean = (bean instanceof InitializingBean);
    if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
        if (logger.isDebugEnabled()) {
            logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
        }
        if (System.getSecurityManager() != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                    @Override
                    public Object run() throws Exception {
                        // 调用了InitializingBean.afterPropertiesSet()方法
                        // 这个方法名称字面意思：属性设置之后调用
                        ((InitializingBean) bean).afterPropertiesSet();
                        return null;
                    }
                }, getAccessControlContext());
            }
            catch (PrivilegedActionException pae) {
                throw pae.getException();
            }
        }
        else {
            // 调用了InitializingBean.afterPropertiesSet()方法
            // 这个方法名称字面意思：属性设置之后调用
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    if (mbd != null) {
        String initMethodName = mbd.getInitMethodName();
        if (initMethodName != null && !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
                !mbd.isExternallyManagedInitMethod(initMethodName)) {
            // 自定义的initMethod(初始化方法)
            // 这个是<bean init-method="custonInitMethod">方法
            // 下面用demo来看看afterPropertiesSet和自定义的initMethod的区别
            invokeCustomInitMethod(beanName, bean, mbd);
        }
    }
}

// 整个过程是通过反射来调用自定义的initMethod
protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd) throws Throwable {
    String initMethodName = mbd.getInitMethodName();
    final Method initMethod = (mbd.isNonPublicAccessAllowed() ?
            BeanUtils.findMethod(bean.getClass(), initMethodName) :
            ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName));
    if (initMethod == null) {
        if (mbd.isEnforceInitMethod()) {
            throw new BeanDefinitionValidationException("Couldn't find an init method named '" +
                    initMethodName + "' on bean with name '" + beanName + "'");
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("No default init method named '" + initMethodName +
                        "' found on bean with name '" + beanName + "'");
            }
            // Ignore non-existent default lifecycle methods.
            return;
        }
    }

    if (logger.isDebugEnabled()) {
        logger.debug("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
    }

    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            @Override
            public Object run() throws Exception {
                ReflectionUtils.makeAccessible(initMethod);
                return null;
            }
        });
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                @Override
                public Object run() throws Exception {
                    initMethod.invoke(bean);
                    return null;
                }
            }, getAccessControlContext());
        }
        catch (PrivilegedActionException pae) {
            InvocationTargetException ex = (InvocationTargetException) pae.getException();
            throw ex.getTargetException();
        }
    }
    else {
        try {
            ReflectionUtils.makeAccessible(initMethod);
            initMethod.invoke(bean);
        }
        catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
}


```

**afterPropertiesSet和自定义的initMethod的区别**


```

// 将Cat类继承InitializingBean实现afterPropertiesSet()
// 自定义方法customInitMethod

package com.spring.source.code.study.BeanPostProcessor;

import org.springframework.beans.factory.InitializingBean;

public class Cat implements InitializingBean {

    private String color;

    private String kind;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "color='" + color + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean.afterPropertiesSet before=" + toString());
        this.kind = "bigFaceCat";
        this.color = "black";
        System.out.println("InitializingBean.afterPropertiesSet after=" + toString());
    }

    public void customInitMethod(){
        System.out.println("bean element customInitMethod before=" + toString());
        this.kind = "littleCatCat";
        this.color = "green";
        System.out.println("bean element customInitMethod after=" + toString());
    }

}

// xml配置文件调整一下，加个init-method属性
<bean id="cat" class="com.spring.source.code.study.BeanPostProcessor.Cat" init-method="customInitMethod">
    <property name="color" value="red"></property>
    <property name="kind" value="orangeCat"></property>
</bean>


// 测试结果

postProcessBeforeInitialization before :cat=Cat{color='red', kind='orangeCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
postProcessBeforeInitialization after :cat=Cat{color='white', kind='bigFlowerCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
InitializingBean.afterPropertiesSet before=Cat{color='white', kind='bigFlowerCat'}
InitializingBean.afterPropertiesSet after=Cat{color='black', kind='bigFaceCat'}
bean element customInitMethod before=Cat{color='black', kind='bigFaceCat'}
bean element customInitMethod after=Cat{color='green', kind='littleCatCat'}
postProcessAfterInitialization before :cat=Cat{color='green', kind='littleCatCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
postProcessAfterInitialization after :cat=Cat{color='yellow', kind='orangeCat'}   class com.spring.source.code.study.BeanPostProcessor.Cat
Cat{color='yellow', kind='orangeCat'}

// 从上述结果中可以看到，对bean在初始化过程中4次的调整机会的先后顺序：beanPostProcessor的前置调用，InitializingBean的afterPropertiesSet，bean标签自定义的init-method方法，beanPostProcessor的后置调用

```


## 3 结语

> 从上述过程中，我们解答了开始的疑问，关于beanPostProcessor的两个方法的区别。并且引出了InitializingBean接口和标签上的init-method的属性的区别，
  这几种方式的优劣，就不晓得了。
