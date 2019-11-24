BeanFactoryPostProcessor

## 1 介绍

### 1.1 认识

> BeanFactoryPostProcessor接口主要是对BeanFactory做一些自定义的处理，若是需要对BeanFactory做些调整，
  可以实现BeanFactoryPostProcessor进行实现
  
```
public interface BeanFactoryPostProcessor {
    
    // 主要实现该方法，参数就是beanFactory，可以对beanFactory做调整
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}

```

## 2 AbstractApplicationContext.invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)

```


package com.spring.source.code.study.BeanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Iterator;

public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("调用了自定义的CustomBeanFactoryPostProcessor " + beanFactory);
        Iterator<String> it = beanFactory.getBeanNamesIterator();

        String[] names = beanFactory.getBeanDefinitionNames();
        // 获取了所有的bean名称列表

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
            System.out.println(name + " bean properties: " + bd.getPropertyValues().toString());

        }

    }
}


@Test
public void testCustomBeanFactoryPostProcessor(){
    // 运行后输出
    // 调用了自定义的CustomBeanFactoryPostProcessor org.springframework.beans.factory.support.DefaultListableBeanFactory@419c5f1a: defining beans [customBeanFactoryPostProcessor]; root of factory hierarchy
    // customBeanFactoryPostProcessor bean properties: PropertyValues: length=0
    // 可以断点进行跟踪
    ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("14.BeanFactoryPostProcessor/beanFactoryProcessorTest.xml");
}


// 该方法是从ClassPathXmlApplicationContext.refresh()的 invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)进入

protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    // 后置处理器的注册代理类
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
}

```


### 2.1 AbstractApplicationContext.invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)


```
public static void invokeBeanFactoryPostProcessors(
        ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

    // Invoke BeanDefinitionRegistryPostProcessors first, if any.
    Set<String> processedBeans = new HashSet<String>();

    if (beanFactory instanceof BeanDefinitionRegistry) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
        List<BeanDefinitionRegistryPostProcessor> registryPostProcessors =
                new LinkedList<BeanDefinitionRegistryPostProcessor>();

        for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
            if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                BeanDefinitionRegistryPostProcessor registryPostProcessor =
                        (BeanDefinitionRegistryPostProcessor) postProcessor;
                registryPostProcessor.postProcessBeanDefinitionRegistry(registry);
                registryPostProcessors.add(registryPostProcessor);
            }
            else {
                regularPostProcessors.add(postProcessor);
            }
        }

        // Do not initialize FactoryBeans here: We need to leave all regular beans
        // uninitialized to let the bean factory post-processors apply to them!
        // Separate between BeanDefinitionRegistryPostProcessors that implement
        // PriorityOrdered, Ordered, and the rest.
        // 这里拿到实现BeanDefinitionRegistryPostProcessor的beanFactory中的bean
        // 后面跟踪beanFactory.getBeanNamesForType方法
        String[] postProcessorNames =
                beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);

        // First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
        List<BeanDefinitionRegistryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();
        for (String ppName : postProcessorNames) {
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                processedBeans.add(ppName);
            }
        }
        OrderComparator.sort(priorityOrderedPostProcessors);
        registryPostProcessors.addAll(priorityOrderedPostProcessors);
        invokeBeanDefinitionRegistryPostProcessors(priorityOrderedPostProcessors, registry);

        // Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
        // 
        postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
        List<BeanDefinitionRegistryPostProcessor> orderedPostProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();
        for (String ppName : postProcessorNames) {
            if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
                orderedPostProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                processedBeans.add(ppName);
            }
        }
        OrderComparator.sort(orderedPostProcessors);
        registryPostProcessors.addAll(orderedPostProcessors);
        invokeBeanDefinitionRegistryPostProcessors(orderedPostProcessors, registry);

        // Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
        // 这里是调用 BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry方法
        boolean reiterate = true;
        while (reiterate) {
            reiterate = false;
            postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            for (String ppName : postProcessorNames) {
                if (!processedBeans.contains(ppName)) {
                    BeanDefinitionRegistryPostProcessor pp = beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class);
                    registryPostProcessors.add(pp);
                    processedBeans.add(ppName);
                    pp.postProcessBeanDefinitionRegistry(registry);
                    reiterate = true;
                }
            }
        }

        // Now, invoke the postProcessBeanFactory callback of all processors handled so far.
        invokeBeanFactoryPostProcessors(registryPostProcessors, beanFactory);
        invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
    }

    else {
        // Invoke factory processors registered with the context instance.
        invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
    }

    // Do not initialize FactoryBeans here: We need to leave all regular beans
    // uninitialized to let the bean factory post-processors apply to them!
    // 这里拿到BeanFactoryPostProcessor类型的处理器的beanName，自定义的customBeanFactoryPostProcessor就是这里拿到的
    String[] postProcessorNames =
            beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

    // Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
    // Ordered, and the rest.
    List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    List<String> orderedPostProcessorNames = new ArrayList<String>();
    List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
    for (String ppName : postProcessorNames) {
        if (processedBeans.contains(ppName)) {
            // skip - already processed in first phase above
        }
        else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
            priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
        }
        else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
            orderedPostProcessorNames.add(ppName);
        }
        else {
            nonOrderedPostProcessorNames.add(ppName);
        }
    }

    // First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
    OrderComparator.sort(priorityOrderedPostProcessors);
    invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

    // Next, invoke the BeanFactoryPostProcessors that implement Ordered.
    List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    for (String postProcessorName : orderedPostProcessorNames) {
        orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
    }
    OrderComparator.sort(orderedPostProcessors);
    invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

    // Finally, invoke all other BeanFactoryPostProcessors.
    List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
    for (String postProcessorName : nonOrderedPostProcessorNames) {
        // 这里会去beanFactory中先拿到customBeanFactoryPostProcessor的bean处理
        // beanFactory.getBean方法：这个方法会最终实例化bean，后面章节分析
        nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
    }
    // 最终都会调用这里的处理方法，
    // 在beanFactoryProcessorTest.xml中定义的customBeanFactoryPostProcessor的bean，会在这里进行被调用
    invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
}


// 这个是调用后置处理器的方法
private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

    for (BeanFactoryPostProcessor postProcessor : postProcessors) {
        // 自定义的处理器方法就在这里调用
        postProcessor.postProcessBeanFactory(beanFactory);
    }
}

```


### 2.2 AbstractApplicationContext.getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)

```
// 参数type：类或接口的类型
// includeNonSingletons:是否包含非单例的bean
// allowEagerInit:是否允许提前初始化
public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
    // isConfigurationFrozen()是指bean的元数据是否被缓存，默认情况下是false,这个是针对全局所有的bean的，所以默认是false
    if (!isConfigurationFrozen() || type == null || !allowEagerInit) {
        // 本例中是走的这里
        return doGetBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    }
    Map<Class<?>, String[]> cache =
            (includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType);
    String[] resolvedBeanNames = cache.get(type);
    if (resolvedBeanNames != null) {
        return resolvedBeanNames;
    }
    resolvedBeanNames = doGetBeanNamesForType(type, includeNonSingletons, allowEagerInit);
    if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
        cache.put(type, resolvedBeanNames);
    }
    return resolvedBeanNames;
}



// 
private String[] doGetBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
    List<String> result = new ArrayList<String>();

    // Check all bean definitions.
    // 检查所有的beanDefinition
    for (String beanName : this.beanDefinitionNames) {
        // Only consider bean as eligible if the bean name
        // is not defined as alias for some other bean.
        // 检查是否是根据别名来获取bean
        if (!isAlias(beanName)) {
            try {
                // 从
                RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                // Only check bean definition if it is complete.
                if (!mbd.isAbstract() && (allowEagerInit ||
                        ((mbd.hasBeanClass() || !mbd.isLazyInit() || isAllowEagerClassLoading())) &&
                                !requiresEagerInitForType(mbd.getFactoryBeanName()))) {
                    // In case of FactoryBean, match object created by FactoryBean.
                    // 针对factoryBean做些处理，之后再将FactoryBean
                    boolean isFactoryBean = isFactoryBean(beanName, mbd);
                    boolean matchFound = (allowEagerInit || !isFactoryBean || containsSingleton(beanName)) &&
                            (includeNonSingletons || isSingleton(beanName)) && isTypeMatch(beanName, type);
                    if (!matchFound && isFactoryBean) {
                        // In case of FactoryBean, try to match FactoryBean instance itself next.
                        beanName = FACTORY_BEAN_PREFIX + beanName;
                        matchFound = (includeNonSingletons || mbd.isSingleton()) && isTypeMatch(beanName, type);
                    }
                    if (matchFound) {
                        result.add(beanName);
                    }
                }
            }
            catch (CannotLoadBeanClassException ex) {
                if (allowEagerInit) {
                    throw ex;
                }
                // Probably contains a placeholder: let's ignore it for type matching purposes.
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Ignoring bean class loading failure for bean '" + beanName + "'", ex);
                }
                onSuppressedException(ex);
            }
            catch (BeanDefinitionStoreException ex) {
                if (allowEagerInit) {
                    throw ex;
                }
                // Probably contains a placeholder: let's ignore it for type matching purposes.
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Ignoring unresolvable metadata in bean definition '" + beanName + "'", ex);
                }
                onSuppressedException(ex);
            }
        }
    }

    // Check manually registered singletons too.
    // 检查手动注册的单例
    for (String beanName : this.manualSingletonNames) {
        try {
            // In case of FactoryBean, match object created by FactoryBean.
            if (isFactoryBean(beanName)) {
                if ((includeNonSingletons || isSingleton(beanName)) && isTypeMatch(beanName, type)) {
                    result.add(beanName);
                    // Match found for this bean: do not match FactoryBean itself anymore.
                    continue;
                }
                // In case of FactoryBean, try to match FactoryBean itself next.
                beanName = FACTORY_BEAN_PREFIX + beanName;
            }
            // Match raw bean instance (might be raw FactoryBean).
            if (isTypeMatch(beanName, type)) {
                result.add(beanName);
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Shouldn't happen - probably a result of circular reference resolution...
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to check manually registered singleton with name '" + beanName + "'", ex);
            }
        }
    }

    return StringUtils.toStringArray(result);
}


// 为了拿到RootBeanDefinition
protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
    // Quick check on the concurrent map first, with minimal locking.
    // 从mergedBeanDefinitions的map中获取bean
    // 
    RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
    if (mbd != null) {
        return mbd;
    }
    // 这里的getBeanDefinition(beanName)就是从beanDefinitionMap的容器中获得
    return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
}


protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
			throws BeanDefinitionStoreException {

    return getMergedBeanDefinition(beanName, bd, null);
}


protected RootBeanDefinition getMergedBeanDefinition(
			String beanName, BeanDefinition bd, BeanDefinition containingBd)
			throws BeanDefinitionStoreException {

    synchronized (this.mergedBeanDefinitions) {
        RootBeanDefinition mbd = null;

        // Check with full lock now in order to enforce the same merged instance.
        // 这里mergedBeanDefinitions中存放的是已经创建，且允许缓存的beanName
        // 本例中的customBeanFactoryPostProcessor是还没有创建过
        if (containingBd == null) {
            // 故这里mbd仍然是null
            mbd = this.mergedBeanDefinitions.get(beanName);
        }

        if (mbd == null) {
            if (bd.getParentName() == null) {
                // Use copy of given root bean definition.
                // 一般解析的beanDefinition都是GenericBeanDefinition
                if (bd instanceof RootBeanDefinition) {
                    mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
                }
                else {
                    // 对于不是RootBeanDefinition的将其copy转变为RootBeanDefinition
                    mbd = new RootBeanDefinition(bd);
                }
            }
            else {
                // Child bean definition: needs to be merged with parent.
                BeanDefinition pbd;
                try {
                    String parentBeanName = transformedBeanName(bd.getParentName());
                    if (!beanName.equals(parentBeanName)) {
                        pbd = getMergedBeanDefinition(parentBeanName);
                    }
                    else {
                        if (getParentBeanFactory() instanceof ConfigurableBeanFactory) {
                            pbd = ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(parentBeanName);
                        }
                        else {
                            throw new NoSuchBeanDefinitionException(bd.getParentName(),
                                    "Parent name '" + bd.getParentName() + "' is equal to bean name '" + beanName +
                                    "': cannot be resolved without an AbstractBeanFactory parent");
                        }
                    }
                }
                catch (NoSuchBeanDefinitionException ex) {
                    throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName,
                            "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
                }
                // Deep copy with overridden values.
                mbd = new RootBeanDefinition(pbd);
                mbd.overrideFrom(bd);
            }

            // Set default singleton scope, if not configured before.
            if (!StringUtils.hasLength(mbd.getScope())) {
                mbd.setScope(RootBeanDefinition.SCOPE_SINGLETON);
            }

            // A bean contained in a non-singleton bean cannot be a singleton itself.
            // Let's correct this on the fly here, since this might be the result of
            // parent-child merging for the outer bean, in which case the original inner bean
            // definition will not have inherited the merged outer bean's singleton status.
            if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
                mbd.setScope(containingBd.getScope());
            }

            // Only cache the merged bean definition if we're already about to create an
            // instance of the bean, or at least have already created an instance before.
            if (containingBd == null && isCacheBeanMetadata() && isBeanEligibleForMetadataCaching(beanName)) {
                // 这里对创建的bean进行缓存，由于customBeanFactoryPostProcessor的bean都没有进行创建过，所以这里是不会缓存的
                // 具体可以看看isBeanEligibleForMetadataCaching(beanName)的方法：
                this.mergedBeanDefinitions.put(beanName, mbd);
            }
        }

        return mbd;
    }
}


protected boolean isBeanEligibleForMetadataCaching(String beanName) {
    return (this.configurationFrozen || super.isBeanEligibleForMetadataCaching(beanName));
}

// 
protected boolean isBeanEligibleForMetadataCaching(String beanName) {
    // 最终的判断是alreadyCreated的集合中是否存在已经创建的
    // alreadyCreated创建bean的时候，会将beanName放入这个集合中
    return this.alreadyCreated.contains(beanName);
}



```


## 3 结语

> 简单跟踪BeanFactoryProcessor，大致可以了解，其实在启动过程中，就开始执行的代码，且这个过程中不会创建
  BeanFactoryProcessor类型的bean，只是利用beanFactory.getBeanNamesForType随取随用的临时取用一下。
  而beanFactory.getBean方法才会实例化bean
