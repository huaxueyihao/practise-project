7 AOP

## 7.1 动态AOP使用示例

```

package com.spring.book.study.chapter07;

public class TestBean {

    private String testStr = "testStr";

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public void test() {
        System.out.println("test");
    }
}




package com.spring.book.study.chapter07;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AspectJTest {

    @Pointcut("execution(public * com.spring.book.study.chapter07.*.*(..))")
    public void test() {

    }

    @Before("test()")
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @After("test()")
    public void afterTest(){
        System.out.println("afterTest");
    }

    @Around("test()")
    public Object aroundTest(ProceedingJoinPoint p) {
        System.out.println("before1");
        Object o = null;
        try {
            o = p.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("after1");
        return o;
    }

}


<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:util="http://www.springframework.org/schema/util"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <aop:aspectj-autoproxy/>

    <bean id="testBean" class="com.spring.book.study.chapter07.TestBean" />
    <bean class="com.spring.book.study.chapter07.AspectJTest"/>
</beans>


package com.spring.book.study.chapter07;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        testAspectJ();
    }

    private static void testAspectJ() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("apectjTest.xml");
        TestBean testBean = (TestBean) bf.getBean("testBean");
        testBean.test();
    }

}



```

## 7.2 动态AOP自定义标签

```


public class AopNamespaceHandler extends NamespaceHandlerSupport {

	/**
	 * Register the {@link BeanDefinitionParser BeanDefinitionParsers} for the
	 * '{@code config}', '{@code spring-configured}', '{@code aspectj-autoproxy}'
	 * and '{@code scoped-proxy}' tags.
	 */
	@Override
	public void init() {
		// In 2.0 XSD as well as in 2.1 XSD.
		registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());
		registerBeanDefinitionParser("aspectj-autoproxy", new AspectJAutoProxyBeanDefinitionParser());
		registerBeanDefinitionDecorator("scoped-proxy", new ScopedProxyBeanDefinitionDecorator());

		// Only in 2.0 XSD: moved to context namespace as of 2.1
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
	}

}

```

### 7.2.1 注册AnnotationAwareAspectJProxyCreator


```
所有解析器都是对BeanDefinitionParser接口的统一实现：
AspectJAutoProxyBeanDefinitionParser

public BeanDefinition parse(Element element, ParserContext parserContext) {
    // 注册AnnotationAwareAspectJAutoProxyCreator
    AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
    // 对于注解中紫烈的处理
    extendBeanDefinition(element, parserContext);
    return null;
}


public static void registerAspectJAnnotationAutoProxyCreatorIfNecessary(
        ParserContext parserContext, Element sourceElement) {
    
    // 注册或升级AutoProxyCreator定义beanName为org.springframework.aop.config.internalAutoProxyCreator的BeanDefinition
    BeanDefinition beanDefinition = AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(
            parserContext.getRegistry(), parserContext.extractSource(sourceElement));
    // 对于proxy-target-class以及expose-proxy属性的处理
    useClassProxyingIfNecessary(parserContext.getRegistry(), sourceElement);
    // 注册主键并通知，便于监听器做进一步处理
    // 其中beanDefinition的className为AnnotationAwareAspectJAutoProxyCreator
    registerComponentIfNecessary(beanDefinition, parserContext);
}


```

> registerAspectJAnnotationAutoProxyCreatorIfNecessary主要完成了3件事

**1.注册或者升级AnnotationAwareAspectJAutoProxyCreator**

```
public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, Object source) {
    return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
}



private static BeanDefinition registerOrEscalateApcAsRequired(Class<?> cls, BeanDefinitionRegistry registry, Object source) {
    Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
    // 如果已经存在了自动代理创建器且存在的自动代理创建器与现在的不一致，那么需要根据优先级来判断到底需要使用哪
    if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
        BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
        if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
            int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
            int requiredPriority = findPriorityForClass(cls);
            if (currentPriority < requiredPriority) {
                // 改变bean最重要的就是改变bean所对应的className属性
                apcDefinition.setBeanClassName(cls.getName());
            }
        }
        // 如果已经存在自动代理创建器并且与将要创建的一致，那么无须再次创建
        return null;
    }
    RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
    beanDefinition.setSource(source);
    beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
    beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    return beanDefinition;
}

```

**2.处理proxy-target-class以及expose-proxy属性**

```
useClassProxyingIfNecessary实现了proxy-target-class属性以及expose-proxy属性的处理


private static void useClassProxyingIfNecessary(BeanDefinitionRegistry registry, Element sourceElement) {
    if (sourceElement != null) {
        // 对于proxy-target-class属性处理
        boolean proxyTargetClass = Boolean.valueOf(sourceElement.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE));
        if (proxyTargetClass) {
            AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
        }
        // 对于expose-proxy属性处理
        boolean exposeProxy = Boolean.valueOf(sourceElement.getAttribute(EXPOSE_PROXY_ATTRIBUTE));
        if (exposeProxy) {
            AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
        }
    }
}


// 强制使用的过程其实也是一个属性设置的过程
public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
    if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
        BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
        definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
    }
}

static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
    if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
        BeanDefinition definition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
        definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
    }
}

proxy-target-class: Spring AOP部分使用JDK动态代理或者CGLIB来为目标对象创建代理(建议尽量使用JDK的动态代理)。如果被代理的目标对象实现了至少一个接口，
则会使用JDK动态代理。所有该目标类型实现的接口都将被代理。若该目标对象没有实现任何接口，则创建爱你一个CGLIB代理。如果你希望强制使用CGLIB代理，那也可以。
但是会产生两个问题
1.无法通知(advise)Final方法，因为他们不能被覆盖。
2.需要将CGLIB二进制发型包放在classpath下面。

强制使用CGLIB代理需要将<aop.config>的proxy-target-class属性设为true；
<aop:config proxy-target-class="true">...</aop:config>

当需要使用CGLIB代理和@AspectJ自动代理支持，可以按照以下方式设置<aop:aspectj-autoproxy>的proxy-target-class属性：
<aop:aspectj-autoproxy proxy-target-class="true"/>
使用过程中才会发现细节问题差别：
1.JDK动态代理：其代理对象必须是某个接口的实现，它是通过在运行期间创建一个接口的实现类来完成对目标对象的代理。
2.CGLIB代理：实现原理类似与JDK动态代理，智慧树它在运行期间生成的代理对象是针对目标类扩展的紫烈。CGLIB是高效的代码生成包，底层是依靠ASM
  (开源的java字节码编辑类库)操作字节码实现的，性能比JDK强。
3.expose-proxy：有时候目标对象内部的自我调用将无法实施切面中的增强，如下例：

public interface AService{
    public void a();
    
    public void b();
}


@Service
public class AServiceImpl1 implements AService{
    
    @Transactional(propagation=Propagation.REQUIRED)
    public void a(){
        this.b();
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void b(){
    
    }
}

此处的this指向目标对象，英雌调用this.b()将不会执行b事务切面，即不会执行事务增强，英雌
b方法的事务定义"@Transactional(propagation=Propagation.REQUIRES_NEW)"将不会实施，
解决这个问题：
<aop:aspectj-autoproxy expose-proxy="true"/>

让后将以上代码中的"this.b()"修改为"((AService)AopContxt.currentProxy()).b();"即可

```

## 7.3 创建AOP代理

> 在类的层级中，我们看到AnnotationAwareAspectJAutoProxyCreator实现了BeanPostProcessor接口，
  而实现BeanPostProcessor后，当Spring加载这个Bean时会在实例化前调用其postProcessorAfterInitialization
  方法。
  
```
AbstractAutoProxyCreator.java

public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean != null) {
        // 根据给定的bean的class和name构建出一个key，格式：beanClassName_beanName
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (!this.earlyProxyReferences.contains(cacheKey)) {
            // 如果它适合被代理，则需要封装指定bean
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}


protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    // 如果已经处理过
    if (beanName != null && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    // 给定的bean类是否代表一个基础设施类，基础设施类不应代理，或者配置了指定bean不需要自动代理
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // Create proxy if we have advice.
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    // 如果过获取到了增强需要针对增强创建代理
    if (specificInterceptors != DO_NOT_PROXY) {
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        // 创建代理
        Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}

真正创建代理的代码是从getAdcicesAndAdvisorsForBean开始。
创建代理主要包含了两个步骤：
1.获取增强方法或者增强器。
2.根据获取的增强进行代理



AbstractAdvisorAutoProxyCreator.java

protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
    List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
    if (advisors.isEmpty()) {
        return DO_NOT_PROXY;
    }
    return advisors.toArray();
}

protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
    List<Advisor> candidateAdvisors = findCandidateAdvisors();
    List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
    extendAdvisors(eligibleAdvisors);
    if (!eligibleAdvisors.isEmpty()) {
        eligibleAdvisors = sortAdvisors(eligibleAdvisors);
    }
    return eligibleAdvisors;
}

对于指定bean的增强方法的获取一定是包含两个步骤的，获取所有的增强以及寻找所有增强中适用于
bean的增强并应用，那么findCandidateAdvisors与findAdvisorsThatCanApply便是做了这两件事情。
当然，如果无法找到对应的增强器便返回DO_NOT_PROXY=null.



```

![avatar](images/01_create_proxy_advice.jpg)


### 7.3.1 获取增强器

> 对于findCandidateAdvisors的实现其实是由AnnotationAwareAspectJAutoProxyCreator类完成的。

```

AnnotationAwareAspectJAutoProxyCreator.java

protected List<Advisor> findCandidateAdvisors() {
    // Add all the Spring advisors found according to superclass rules.
    List<Advisor> advisors = super.findCandidateAdvisors();
    // Build Advisors for all AspectJ aspects in the bean factory.
    advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
    return advisors;
}

AnnotationAwareAspectJAutoProxyCreator间接继承了AbstractAdvisorAutoProxyCreator

1.获取所有beanName，这一步骤中所有在beanFactory中注册的bean都会被提取出来。
2.遍历所有beanName，并找出声明AspectJ注解的类，进行进一步的处理。
3.对标记为AspectJ注解的类进行增强器的提取
4.将提取结果加入缓存




public List<Advisor> buildAspectJAdvisors() {
    List<String> aspectNames = null;

    synchronized (this) {
        aspectNames = this.aspectBeanNames;
        if (aspectNames == null) {
            List<Advisor> advisors = new LinkedList<Advisor>();
            aspectNames = new LinkedList<String>();
            // 获取所有的beanName
            String[] beanNames =
                    BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Object.class, true, false);
            // 循环所有的beanName找出对应的增强方法
            for (String beanName : beanNames) {
                // 不合法的bean则略过，由子类定义规则，默认返回true
                if (!isEligibleBean(beanName)) {
                    continue;
                }
                // We must be careful not to instantiate beans eagerly as in this
                // case they would be cached by the Spring container but would not
                // have been weaved
                // 获取对应的bean类型
                Class<?> beanType = this.beanFactory.getType(beanName);
                if (beanType == null) {
                    continue;
                }
                // 如果存在Aspect注解
                if (this.advisorFactory.isAspect(beanType)) {
                    aspectNames.add(beanName);
                    AspectMetadata amd = new AspectMetadata(beanType, beanName);
                    if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
                        MetadataAwareAspectInstanceFactory factory =
                                new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
                        // 解析标记AspectJ注解中的增强方法
                        List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
                        if (this.beanFactory.isSingleton(beanName)) {
                            this.advisorsCache.put(beanName, classAdvisors);
                        }
                        else {
                            this.aspectFactoryCache.put(beanName, factory);
                        }
                        advisors.addAll(classAdvisors);
                    }
                    else {
                        // Per target or per this.
                        if (this.beanFactory.isSingleton(beanName)) {
                            throw new IllegalArgumentException("Bean with name '" + beanName +
                                    "' is a singleton, but aspect instantiation model is not singleton");
                        }
                        MetadataAwareAspectInstanceFactory factory =
                                new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
                        this.aspectFactoryCache.put(beanName, factory);
                        advisors.addAll(this.advisorFactory.getAdvisors(factory));
                    }
                }
            }
            this.aspectBeanNames = aspectNames;
            return advisors;
        }
    }

    if (aspectNames.isEmpty()) {
        return Collections.emptyList();
    }
    // 记录在缓存中
    List<Advisor> advisors = new LinkedList<Advisor>();
    for (String aspectName : aspectNames) {
        List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
        if (cachedAdvisors != null) {
            advisors.addAll(cachedAdvisors);
        }
        else {
            MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
            advisors.addAll(this.advisorFactory.getAdvisors(factory));
        }
    }
    return advisors;
}

// 我们已经完成了Advisor的提取，在上面的步骤中最重要的也是最为繁杂的就是增强器的获取。而这一功能委托给了
// getAdvisors方法实现(this.advisorFactory.getAdvisors(factory))


/*
 * 函数中首先完成了对增强器的获取，包括获取注解以及根据注解生成增强的步骤
 * 配置中可能会将增强配置成延迟初始化，需要在首位加入同步实例化增强器保证增强使用之前的实例化，
 * 最后是对DeclareParents注解的获取。
 ***/
public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory maaif) {
    // 获取标记为AspectJ的类
    final Class<?> aspectClass = maaif.getAspectMetadata().getAspectClass();
    // 获取标记为AspectJ的name
    final String aspectName = maaif.getAspectMetadata().getAspectName();
    // 验证
    validate(aspectClass);

    // We need to wrap the MetadataAwareAspectInstanceFactory with a decorator
    // so that it will only instantiate once.
    final MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory =
            new LazySingletonAspectInstanceFactoryDecorator(maaif);

    final List<Advisor> advisors = new LinkedList<Advisor>();
    for (Method method : getAdvisorMethods(aspectClass)) {
        
        Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
        if (advisor != null) {
            advisors.add(advisor);
        }
    }

    // If it's a per target aspect, emit the dummy instantiating aspect.
    if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
        // 如果寻找的增强器不为空而且又配置了增强延迟初始化，那么需要在首位增加同步实例化增强器
        Advisor instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
        advisors.add(0, instantiationAdvisor);
    }

    // Find introduction fields.
    // 获取DeclareParents注解
    for (Field field : aspectClass.getDeclaredFields()) {
        Advisor advisor = getDeclareParentsAdvisor(field);
        if (advisor != null) {
            advisors.add(advisor);
        }
    }

    return advisors;
}



```

**1.普通增强器的获取**

```
// 普通增强器的获取逻辑通过getAdvisor方法实现，实现步骤包括对切点的注解的获取以及根据注解信息生成增强
public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aif,
        int declarationOrderInAspect, String aspectName) {

    validate(aif.getAspectMetadata().getAspectClass());

    // 切点信息的获取
    AspectJExpressionPointcut ajexp =
            getPointcut(candidateAdviceMethod, aif.getAspectMetadata().getAspectClass());
    if (ajexp == null) {
        return null;
    }
    // 根据切点信息生成增强器
    return new InstantiationModelAwarePointcutAdvisorImpl(
            this, ajexp, aif, candidateAdviceMethod, declarationOrderInAspect, aspectName);
}


1.切点信息的获取，所谓获取切点信息就是制定注解的表达式信息的获取，如@Before("test()")
private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass) {
    // 获取方法上的注解
    AspectJAnnotation<?> aspectJAnnotation =
            AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
    if (aspectJAnnotation == null) {
        return null;
    }
    // 使用AspectJExpressionPointcut实例封装获取的信息
    AspectJExpressionPointcut ajexp =
            new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class<?>[0]);
    // 提取得到的注解中的表达式，如：
    // @Pointcut("execution(* *.*test*(..))")中的execution(* *.*test*(..))
    ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
    return ajexp;
}



protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method) {
    // 设置敏感的注解类
    Class<?>[] classesToLookFor = new Class<?>[] {
            Before.class, Around.class, After.class, AfterReturning.class, AfterThrowing.class, Pointcut.class};
    for (Class<?> c : classesToLookFor) {
        AspectJAnnotation<?> foundAnnotation = findAnnotation(method, (Class<Annotation>) c);
        if (foundAnnotation != null) {
            return foundAnnotation;
        }
    }
    return null;
}


// 获取指定方法上的注解并使用AspectJAnnotation封装
private static <A extends Annotation> AspectJAnnotation<A> findAnnotation(Method method, Class<A> toLookFor) {
    A result = AnnotationUtils.findAnnotation(method, toLookFor);
    if (result != null) {
        return new AspectJAnnotation<A>(result);
    }
    else {
        return null;
    }
}

2.根据切点信息生成增强。所有的增强都由Advisor的实现类InstantiationModelAwarePointcutAdvisorImpl统一封装的。
public InstantiationModelAwarePointcutAdvisorImpl(AspectJAdvisorFactory af, AspectJExpressionPointcut ajexp,
			MetadataAwareAspectInstanceFactory aif, Method method, int declarationOrderInAspect, String aspectName) {

    // test()
    this.declaredPointcut = ajexp;
    // public void test.AspectJTest.beforeTest()
    this.method = method;
    this.atAspectJAdvisorFactory = af;
    this.aspectInstanceFactory = aif;
    // 0
    this.declarationOrder = declarationOrderInAspect;
    // test.AspectJTest
    this.aspectName = aspectName;

    if (aif.getAspectMetadata().isLazilyInstantiated()) {
        // Static part of the pointcut is a lazy type.
        Pointcut preInstantiationPointcut =
                Pointcuts.union(aif.getAspectMetadata().getPerClausePointcut(), this.declaredPointcut);

        // Make it dynamic: must mutate from pre-instantiation to post-instantiation state.
        // If it's not a dynamic pointcut, it may be optimized out
        // by the Spring AOP infrastructure after the first evaluation.
        this.pointcut = new PerTargetInstantiationModelPointcut(this.declaredPointcut, preInstantiationPointcut, aif);
        this.lazy = true;
    }
    else {
        // A singleton aspect.
        this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
        this.pointcut = declaredPointcut;
        this.lazy = false;
    }
}

不同的增强所体验的逻辑是不同的，比如@Before("test()")与@After("test()")标签的不同就是增强器增强的位置不同，所以就需要不同的增强器
来完成不同的逻辑，而根据注解中的信息初始化对应的增强器就是在instantiateAdvice函数中实现的


private Advice instantiateAdvice(AspectJExpressionPointcut pcut) {
    return this.atAspectJAdvisorFactory.getAdvice(
            this.method, pcut, this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
}



public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut ajexp,
        MetadataAwareAspectInstanceFactory aif, int declarationOrderInAspect, String aspectName) {
    
    
    Class<?> candidateAspectClass = aif.getAspectMetadata().getAspectClass();
    validate(candidateAspectClass);

    AspectJAnnotation<?> aspectJAnnotation =
            AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
    if (aspectJAnnotation == null) {
        return null;
    }

    // If we get here, we know we have an AspectJ method.
    // Check that it's an AspectJ-annotated class
    if (!isAspect(candidateAspectClass)) {
        throw new AopConfigException("Advice must be declared inside an aspect type: " +
                "Offending method '" + candidateAdviceMethod + "' in class [" +
                candidateAspectClass.getName() + "]");
    }

    if (logger.isDebugEnabled()) {
        logger.debug("Found AspectJ method: " + candidateAdviceMethod);
    }

    AbstractAspectJAdvice springAdvice;
    
    // 根据不同的注解类型封装不同的增强器
    switch (aspectJAnnotation.getAnnotationType()) {
        case AtBefore:
            springAdvice = new AspectJMethodBeforeAdvice(candidateAdviceMethod, ajexp, aif);
            break;
        case AtAfter:
            springAdvice = new AspectJAfterAdvice(candidateAdviceMethod, ajexp, aif);
            break;
        case AtAfterReturning:
            springAdvice = new AspectJAfterReturningAdvice(candidateAdviceMethod, ajexp, aif);
            AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterReturningAnnotation.returning())) {
                springAdvice.setReturningName(afterReturningAnnotation.returning());
            }
            break;
        case AtAfterThrowing:
            springAdvice = new AspectJAfterThrowingAdvice(candidateAdviceMethod, ajexp, aif);
            AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
                springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
            }
            break;
        case AtAround:
            springAdvice = new AspectJAroundAdvice(candidateAdviceMethod, ajexp, aif);
            break;
        case AtPointcut:
            if (logger.isDebugEnabled()) {
                logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
            }
            return null;
        default:
            throw new UnsupportedOperationException(
                    "Unsupported advice type on method " + candidateAdviceMethod);
    }

    // Now to configure the advice...
    springAdvice.setAspectName(aspectName);
    springAdvice.setDeclarationOrder(declarationOrderInAspect);
    String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
    if (argNames != null) {
        springAdvice.setArgumentNamesFromStringArray(argNames);
    }
    springAdvice.calculateArgumentBindings();
    return springAdvice;
}

从上述函数中可以看到，Spring会根据不同的注解生成不同的增强器，例如AtBefore会对应AspectJMethodBeforeAdvice，
而在AspectJMethodBeforeAdvice中完成了增强方法的逻辑。尝试分析几个常用的增强器实现。

No.1 MethodBeforeAdviceInterceptor

public class MethodBeforeAdviceInterceptor implements MethodInterceptor, Serializable {

	private MethodBeforeAdvice advice;


	/**
	 * Create a new MethodBeforeAdviceInterceptor for the given advice.
	 * @param advice the MethodBeforeAdvice to wrap
	 */
	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		Assert.notNull(advice, "Advice must not be null");
		this.advice = advice;
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
		return mi.proceed();
	}

}

其中的属性MethodBeforeAdvice代表着前置增强的AspectJMethodBeforeAdvice，跟踪before方法：

public void before(Method method, Object[] args, Object target) throws Throwable {
    invokeAdviceMethod(getJoinPointMatch(), null, null);
}


protected Object invokeAdviceMethod(JoinPointMatch jpMatch, Object returnValue, Throwable ex) throws Throwable {
    return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
}

protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
    Object[] actualArgs = args;
    if (this.aspectJAdviceMethod.getParameterTypes().length == 0) {
        actualArgs = null;
    }
    try {
        ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
        // TODO AopUtils.invokeJoinpointUsingReflection
        // 激活增强方法
        return this.aspectJAdviceMethod.invoke(this.aspectInstanceFactory.getAspectInstance(), actualArgs);
    }
    catch (IllegalArgumentException ex) {
        throw new AopInvocationException("Mismatch on arguments to advice method [" +
                this.aspectJAdviceMethod + "]; pointcut expression [" +
                this.pointcut.getPointcutExpression() + "]", ex);
    }
    catch (InvocationTargetException ex) {
        throw ex.getTargetException();
    }
}

invokeAdviceMethodWithGivenArgs方法中aspectJAdviceMethod正是对于前置增强的方法，在这里实现了调用

No.2 AspectJAfterAdvice

后置增强与前置增强有稍许不一致的地方。大致的结构是在拦截器联中防止MethodBeforeAdviceInterceptor，
而在MethodBeforeAdviceInterceptor中又放置了AspectJMethodBeforeAdvice，并在调用invoke时首先串联调用。
但是在后置增强的时候却不一样，没有提供中间的类，而是直接在拦截器链中使用了中间的AspectJAfterAdvice。

public class AspectJAfterAdvice extends AbstractAspectJAdvice implements MethodInterceptor, AfterAdvice {

	public AspectJAfterAdvice(
			Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {

		super(aspectJBeforeAdviceMethod, pointcut, aif);
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		}
		finally {
		    // 激活增强方法
			invokeAdviceMethod(getJoinPointMatch(), null, null);
		}
	}

	@Override
	public boolean isBeforeAdvice() {
		return false;
	}

	@Override
	public boolean isAfterAdvice() {
		return true;
	}

}

```

**2. 增加同步实例化增强器**

```
如果寻找的增强器不为空而且又配置了增强延迟初始化，那么就需要在首位加入同步实例化增强器。
同步实例化增强器SyntheticInstantiationAdvisor如下：

ReflectiveAspectJAdvisorFactory内部类

protected static class SyntheticInstantiationAdvisor extends DefaultPointcutAdvisor {

    public SyntheticInstantiationAdvisor(final MetadataAwareAspectInstanceFactory aif) {
        super(aif.getAspectMetadata().getPerClausePointcut(), new MethodBeforeAdvice() {
            // 目标方法前调用，类似@Before
            @Override
            public void before(Method method, Object[] args, Object target) {
                // Simply instantiate the aspect
                // 简单初始化aspect
                aif.getAspectInstance();
            }
        });
    }
}

```

**3. 获取DeclareParents注解**

```
DeclareParents主要用于引介增强的注解形式的实现，而其实现方式与普通增强很类似，只不过使用DeclareParentsAdvisor
对功能进行封装

private Advisor getDeclareParentsAdvisor(Field introductionField) {
    DeclareParents declareParents = introductionField.getAnnotation(DeclareParents.class);
    if (declareParents == null) {
        // Not an introduction field
        return null;
    }

    if (DeclareParents.class.equals(declareParents.defaultImpl())) {
        // This is what comes back if it wasn't set. This seems bizarre...
        // TODO this restriction possibly should be relaxed
        throw new IllegalStateException("defaultImpl must be set on DeclareParents");
    }

    return new DeclareParentsAdvisor(
            introductionField.getType(), declareParents.value(), declareParents.defaultImpl());
}



```

### 7.3.2 寻找匹配的增强器



```
对于所有增强器来将，并不一定都使用于当前的Bean，还要挑取适合的增强器，也就是满足我们配置的统配符的增强器。具体实现
在findAdvisorsThatCanApply

protected List<Advisor> findAdvisorsThatCanApply(
        List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

    ProxyCreationContext.setCurrentProxiedBeanName(beanName);
    try {
        // 过滤已经得到的advisors
        return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
    }
    finally {
        ProxyCreationContext.setCurrentProxiedBeanName(null);
    }
}


// 
public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
    if (candidateAdvisors.isEmpty()) {
        return candidateAdvisors;
    }
    List<Advisor> eligibleAdvisors = new LinkedList<Advisor>();
    // 首先处理引介
    for (Advisor candidate : candidateAdvisors) {
        if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
            eligibleAdvisors.add(candidate);
        }
    }
    boolean hasIntroductions = !eligibleAdvisors.isEmpty();
    for (Advisor candidate : candidateAdvisors) {
        // 引介增强已经处理
        if (candidate instanceof IntroductionAdvisor) {
            // already processed
            continue;
        }
        // 对于普通bean的处理
        if (canApply(candidate, clazz, hasIntroductions)) {
            eligibleAdvisors.add(candidate);
        }
    }
    return eligibleAdvisors;
}


findAdvisorsThatCanApply函数的主要功能是寻找所有增强器中适用于当前class的增强器。引介增强与普通的增强处理是不一样的。

public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions) {
    if (advisor instanceof IntroductionAdvisor) {
        return ((IntroductionAdvisor) advisor).getClassFilter().matches(targetClass);
    }
    else if (advisor instanceof PointcutAdvisor) {
        PointcutAdvisor pca = (PointcutAdvisor) advisor;
        return canApply(pca.getPointcut(), targetClass, hasIntroductions);
    }
    else {
        // It doesn't have a pointcut so we assume it applies.
        return true;
    }
}



public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions) {
    Assert.notNull(pc, "Pointcut must not be null");
    if (!pc.getClassFilter().matches(targetClass)) {
        return false;
    }

    MethodMatcher methodMatcher = pc.getMethodMatcher();
    IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
    if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
        introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher) methodMatcher;
    }

    Set<Class<?>> classes = new LinkedHashSet<Class<?>>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
    classes.add(targetClass);
    for (Class<?> clazz : classes) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if ((introductionAwareMethodMatcher != null &&
                    introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions)) ||
                    methodMatcher.matches(method, targetClass)) {
                return true;
            }
        }
    }

    return false;
}


```

### 7.3.3 创建代理

```
在获取了所有对应bean的增强器后，便可以进行代理的创建了。

AbstractAutoProxyCreator.java

protected Object createProxy(
        Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) {

    ProxyFactory proxyFactory = new ProxyFactory();
    // 获取当前类中相关属性
    proxyFactory.copyFrom(this);

    
    if (!proxyFactory.isProxyTargetClass()) {
        // 决定对于给定的bean是否应该使用targetClass而不是它的接口代理
        // 检查proxyTargetClass设置以及preserveTargetClass属性
        if (shouldProxyTargetClass(beanClass, beanName)) {
            proxyFactory.setProxyTargetClass(true);
        }
        else {
            evaluateProxyInterfaces(beanClass, proxyFactory);
        }
    }

    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
    for (Advisor advisor : advisors) {
        // 加入增强器
        proxyFactory.addAdvisor(advisor);
    }
    
    // 设置要代理的类
    proxyFactory.setTargetSource(targetSource);
    // 定制代理
    customizeProxyFactory(proxyFactory);
    
    // 用来控制代理工厂被配置之后，是否还允许修改通知
    // 缺省值为false(即在代理被配置之后，不允许修改代理的配置)
    proxyFactory.setFrozen(this.freezeProxy);
    if (advisorsPreFiltered()) {
        proxyFactory.setPreFiltered(true);
    }

    return proxyFactory.getProxy(getProxyClassLoader());
}



protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory) {
    Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, getProxyClassLoader());
    boolean hasReasonableProxyInterface = false;
    for (Class<?> ifc : targetInterfaces) {
        if (!isConfigurationCallbackInterface(ifc) && !isInternalLanguageInterface(ifc) &&
                ifc.getMethods().length > 0) {
            hasReasonableProxyInterface = true;
            break;
        }
    }
    if (hasReasonableProxyInterface) {
        // Must allow for introductions; can't just set interfaces to the target's interfaces only.
        for (Class<?> ifc : targetInterfaces) {
            // 添加代理接口
            proxyFactory.addInterface(ifc);
        }
    }
    else {
        proxyFactory.setProxyTargetClass(true);
    }
}

对于代理类的创建及处理，Spring委托给了ProxyFactory去处理，而在此函数中主要对ProxyFactory的初始化操作，进而对真正的创建
代理做准备，这些初始化操作包括如下内容。
1.获取当前类中的属性。
2.添加代理接口。
3.封装Advisor并加入到ProxyFactory中。
4.设置要代理的类
5.当然在Spring中还未子类提供了定制的函数customizeProxyFactory，子类可以在此函数中进行对ProxyFactory的进一步封装。
6.进行获取代理操作

其中，封装Advisor并加入到ProxyFactory中以及创建代理是两个相对繁琐的过程，可以通过ProxyFactory提供的addAdvisor方法
直接将增强器置入代理创建工厂中，但是将拦截器封装为增强器还是需要一定的逻辑的

protected Advisor[] buildAdvisors(String beanName, Object[] specificInterceptors) {
    // Handle prototypes correctly...
    // 解析注册的所有interceptorName
    Advisor[] commonInterceptors = resolveInterceptorNames();

    List<Object> allInterceptors = new ArrayList<Object>();
    if (specificInterceptors != null) {
        // 加入拦截器
        allInterceptors.addAll(Arrays.asList(specificInterceptors));
        if (commonInterceptors != null) {
            if (this.applyCommonInterceptorsFirst) {
                allInterceptors.addAll(0, Arrays.asList(commonInterceptors));
            }
            else {
                allInterceptors.addAll(Arrays.asList(commonInterceptors));
            }
        }
    }
    if (logger.isDebugEnabled()) {
        int nrOfCommonInterceptors = (commonInterceptors != null ? commonInterceptors.length : 0);
        int nrOfSpecificInterceptors = (specificInterceptors != null ? specificInterceptors.length : 0);
        logger.debug("Creating implicit proxy for bean '" + beanName + "' with " + nrOfCommonInterceptors +
                " common interceptors and " + nrOfSpecificInterceptors + " specific interceptors");
    }

    Advisor[] advisors = new Advisor[allInterceptors.size()];
    for (int i = 0; i < allInterceptors.size(); i++) {
        // 拦截器进行封装转化为Advisor
        advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
    }
    return advisors;
}


public Advisor wrap(Object adviceObject) throws UnknownAdviceTypeException {
    // 如果要封装的对象本身就是Advisor类型的，那么无需在做过多处理
    if (adviceObject instanceof Advisor) {
        return (Advisor) adviceObject;
    }
    // 因为此封装方法支队Advisor与Advice两种类型的数据有效，如果不是将不能封装
    if (!(adviceObject instanceof Advice)) {
        throw new UnknownAdviceTypeException(adviceObject);
    }
    Advice advice = (Advice) adviceObject;
    if (advice instanceof MethodInterceptor) {
        // So well-known it doesn't even need an adapter.
        // 如果是MethodInterceptor类型则使用DefaultPointcutAdvisor封装
        return new DefaultPointcutAdvisor(advice);
    }
    // 如果存在Advisor的适配器那么也同样需要进行封装
    for (AdvisorAdapter adapter : this.adapters) {
        // Check that it is supported.
        if (adapter.supportsAdvice(advice)) {
            return new DefaultPointcutAdvisor(advice);
        }
    }
    throw new UnknownAdviceTypeException(advice);
}

由于Spring中设计过多的拉按揭器、增强器、增强方法等方式来对逻辑进行增强，所以非常有
必要统一封装成Advisor来进行代理的创建，完成了增强的封装过程，那么解析最重要的一步
就是代理的创建与获取了。

# ProxyFactory.java
public Object getProxy(ClassLoader classLoader) {
    return createAopProxy().getProxy(classLoader);
}


```

**1.创建代理**


```

protected final synchronized AopProxy createAopProxy() {
    if (!this.active) {
        activate();
    }
    // 创建代理
    return getAopProxyFactory().createAopProxy(this);
}

DefaultAopProxyFactory.java
public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
    if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
        Class<?> targetClass = config.getTargetClass();
        if (targetClass == null) {
            throw new AopConfigException("TargetSource cannot determine target class: " +
                    "Either an interface or a target is required for proxy creation.");
        }
        if (targetClass.isInterface()) {
            return new JdkDynamicAopProxy(config);
        }
        return new ObjenesisCglibAopProxy(config);
    }
    else {
        return new JdkDynamicAopProxy(config);
    }
}

Spring的代理中JDKProxy的实现和CglibProxy的实现。Spring会选择一个代理方式
从if中判断条件可以看到3个方面影响着Spring的判断：
1.optimize:用来控制通过CGLIB创建的代理是够使用激进的优化策略。除非完全了解AOP代理如何处理优化，否则不推荐用户使用这个设置。
目前这个属性仅用于CGLIB代理，对于JDK动态代理(默认代理)无效
2.proxyTargetClass：这个属性为true时，目标类本身被代理而不是目标类的接口。如果这个属性值被设为true，CGLIB代理将被创建，
设置方式为<aop:aspectj-autoproxy proxy-target-class="true">
3.hasNoUserSuppliedProxyInterfaces:是否存在代理接口

下面是对JDK与Cglib方式的总结。
如果目标对象实现了接口，默认情况下回采用JDK动态代理实现AOP
如果目标对象实现了接口，可以强制使用CGLIB实现AOP
如果目标对象没有实现接口，必须采用CGLIB库，Spring会自动在JDK动态代理和CGLIB之间转换

如何前置使用CGLIB实现AOP？
添加CGLIB库，Spring_HOME/cglib/*.jar
在Spring配置文件中加入<aop:aspectj-autoproxy proxy-target-class="true"/>

JDK动态代理和CGLIB字节码生成区别？
JDK动态代只能对实现了接口的类生成代理，而不能针对类。
CGLIB是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法，因为是继承，所以该类或方法最好不要声明成final。


```

**2.获取代理**

```

package com.spring.book.study.chapter07;

public interface UserService {

    /**
     * 目标方法
     */
    void add();

}



package com.spring.book.study.chapter07;

public class UserServiceImpl implements UserService {

    public void add() {
        System.out.println("--------------add------------");
    }

}



package com.spring.book.study.chapter07;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {

    private Object target;

    public MyInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("---------before---------");

        Object result = method.invoke(target, args);

        System.out.println("----------after---------");

        return result;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target.getClass().getInterfaces(), this);
    }
}



package com.spring.book.study.chapter07;

import org.junit.Test;

public class ProxyTest {

    @Test
    public void testProxy() throws Throwable {
        UserService userService = new UserServiceImpl();
        MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);
        UserService proxy = (UserService) invocationHandler.getProxy();
        proxy.add();
    }
}


以上是实现了简单的aop功能


继续分析JdkDynamicAopProxy的getProxy。


public Object getProxy(ClassLoader classLoader) {
    if (logger.isDebugEnabled()) {
        logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
    }
    Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised);
    findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
    return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
}

JdkDynamicAopProxy实现了InvocationHandler接口

public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodInvocation invocation;
    Object oldProxy = null;
    boolean setProxyContext = false;

    TargetSource targetSource = this.advised.targetSource;
    Class<?> targetClass = null;
    Object target = null;

    try {
        // equals方法的处理
        if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
            // The target does not implement the equals(Object) method itself.
            return equals(args[0]);
        }
        // hash方法的处理
        if (!this.hashCodeDefined && AopUtils.isHashCodeMethod(method)) {
            // The target does not implement the hashCode() method itself.
            return hashCode();
        }
        /*
         * Class类的isAssignableFrom(Class cls)方法：
         * 如果调用这个方法的class或接口与参数cls表示的类或接口相同
         * 或者是参数cls便是的类或接口的父类，则返回true。
         * 形象地：自身类.class.isAssignableFrom(自身类或子类.class) 返回true
         * 例如：
         *    System.out.println(ArrayList.class.isAssignableFrom(Object.class));
         *    System.out.println(Object.class.isAssignableFrom(ArrayList.class));
         *
         *
         *
         */
        if (!this.advised.opaque && method.getDeclaringClass().isInterface() &&
                method.getDeclaringClass().isAssignableFrom(Advised.class)) {
            // Service invocations on ProxyConfig with the proxy config...
            return AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
        }

        Object retVal;
        // 有时候目标对象内部的自我调用将无法实施切面中的增强则需要通过此属性暴露代理
        if (this.advised.exposeProxy) {
            // Make invocation available if necessary.
            oldProxy = AopContext.setCurrentProxy(proxy);
            setProxyContext = true;
        }

        // May be null. Get as late as possible to minimize the time we "own" the target,
        // in case it comes from a pool.
        target = targetSource.getTarget();
        if (target != null) {
            targetClass = target.getClass();
        }

        // Get the interception chain for this method.
        // 获取当前方法的拦截器链
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

        // Check whether we have any advice. If we don't, we can fallback on direct
        // reflective invocation of the target, and avoid creating a MethodInvocation.
        if (chain.isEmpty()) {
            // We can skip creating a MethodInvocation: just invoke the target directly
            // Note that the final invoker must be an InvokerInterceptor so we know it does
            // nothing but a reflective operation on the target, and no hot swapping or fancy proxying.
            // 如果没有发现任何拦截器那么直接调用切点方法
            retVal = AopUtils.invokeJoinpointUsingReflection(target, method, args);
        }
        else {
            // We need to create a method invocation...
            // 将拦截器封装在ReflectiveMethodInvocation
            // 一遍使用期proceed进行链接表用拦截器
            invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // Proceed to the joinpoint through the interceptor chain.
            // 执行拦截器链
            retVal = invocation.proceed();
        }

        // Massage return value if necessary.
        Class<?> returnType = method.getReturnType();
        if (retVal != null && retVal == target && returnType.isInstance(proxy) &&
                !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
            // Special case: it returned "this" and the return type of the method
            // is type-compatible. Note that we can't help if the target sets
            // a reference to itself in another returned object.
            retVal = proxy;
        }
        else if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopInvocationException(
                    "Null return value from advice does not match primitive return type for: " + method);
        }
        return retVal;
    }
    finally {
        if (target != null && !targetSource.isStatic()) {
            // Must have come from TargetSource.
            targetSource.releaseTarget(target);
        }
        if (setProxyContext) {
            // Restore old proxy.
            AopContext.setCurrentProxy(oldProxy);
        }
    }
}

上面函数主要是创建一个拦截器链，并使用ReflectiveMethodInvocation类进行链的封装，并使用
ReflectiveMethodInvocation类进行了链的封装，而在ReflectiveMethodInvocation类的proceed
方法中实现了拦截器的逐一调用。




public Object proceed() throws Throwable {
    //	We start with an index of -1 and increment early.
    // 执行完所有增强后执行切点方法
    if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
        return invokeJoinpoint();
    }

    // 获取下一个要执行的拦截器
    Object interceptorOrInterceptionAdvice =
            this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
    if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
        // Evaluate dynamic method matcher here: static part will already have
        // been evaluated and found to match.
        // 动态匹配
        InterceptorAndDynamicMethodMatcher dm =
                (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
        if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
            return dm.interceptor.invoke(this);
        }
        else {
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
            // 不匹配则不执行拦截器
            return proceed();
        }
    }
    else {
        // It's an interceptor, so we just invoke it: The pointcut will have
        // been evaluated statically before this object was constructed.
        /*
         * 普通拦截器，直接调用拦截器比如：
         * ExposeInvocationInterceptor、
         * DelegatePerTargetObjectIntroductionInterceptor、
         * MethodBeforeAdviceInterceptor、
         * AspectJAroundAdvice、
         * AspectJAfterAdvice
         *
         *
         *****/
         // 将this作为擦桉树传递以保证当前实例中调用链的执行
        return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
    }
}



```

**2.CGLIB使用示例**



```
package com.spring.book.study.chapter07;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EnhancerDemo {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerDemo.class);
        enhancer.setCallback(new MethodInterceptorImpl());

        EnhancerDemo demo = (EnhancerDemo) enhancer.create();
        demo.test();
        System.out.println(demo);

    }

    public void test() {
        System.out.println("EnhancerDemo test()");
    }


    private static class MethodInterceptorImpl implements MethodInterceptor {

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("Before invoke " + method);
            Object result = methodProxy.invokeSuper(o, objects);
            System.out.println("after invoke" + method);
            return result;
        }
    }

}

CglibAopProxy

public Object getProxy(ClassLoader classLoader) {
    if (logger.isDebugEnabled()) {
        logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
    }

    try {
        Class<?> rootClass = this.advised.getTargetClass();
        Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");

        Class<?> proxySuperClass = rootClass;
        if (ClassUtils.isCglibProxyClass(rootClass)) {
            proxySuperClass = rootClass.getSuperclass();
            Class<?>[] additionalInterfaces = rootClass.getInterfaces();
            for (Class<?> additionalInterface : additionalInterfaces) {
                this.advised.addInterface(additionalInterface);
            }
        }

        // Validate the class, writing log messages as necessary.
        // 验证Class
        validateClassIfNecessary(proxySuperClass, classLoader);

        // Configure CGLIB Enhancer...
        // 创建及配置Enhancer
        Enhancer enhancer = createEnhancer();
        if (classLoader != null) {
            enhancer.setClassLoader(classLoader);
            if (classLoader instanceof SmartClassLoader &&
                    ((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
                enhancer.setUseCache(false);
            }
        }
        enhancer.setSuperclass(proxySuperClass);
        enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setStrategy(new UndeclaredThrowableStrategy(UndeclaredThrowableException.class));

        // 设置拦截器
        Callback[] callbacks = getCallbacks(rootClass);
        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }
        // fixedInterceptorMap only populated at this point, after getCallbacks call above
        enhancer.setCallbackFilter(new ProxyCallbackFilter(
                this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
        enhancer.setCallbackTypes(types);

        // Generate the proxy class and create a proxy instance.
        // 生成代理类以及创建代理
        return createProxyClassAndInstance(enhancer, callbacks);
    }
    catch (CodeGenerationException ex) {
        throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                this.advised.getTargetClass() + "]: " +
                "Common causes of this problem include using a final class or a non-visible class",
                ex);
    }
    catch (IllegalArgumentException ex) {
        throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                this.advised.getTargetClass() + "]: " +
                "Common causes of this problem include using a final class or a non-visible class",
                ex);
    }
    catch (Exception ex) {
        // TargetSource.getTarget() failed
        throw new AopConfigException("Unexpected AOP exception", ex);
    }
}


protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
    enhancer.setInterceptDuringConstruction(false);
    enhancer.setCallbacks(callbacks);
    return (this.constructorArgs != null ?
            enhancer.create(this.constructorArgTypes, this.constructorArgs) :
            enhancer.create());
}

以上过程是一个创建Spring中的Enhancer的过程




private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
    // Parameters used for optimisation choices...
    // 对于expose-proxy属性的处理
    boolean exposeProxy = this.advised.isExposeProxy();
    boolean isFrozen = this.advised.isFrozen();
    boolean isStatic = this.advised.getTargetSource().isStatic();

    // Choose an "aop" interceptor (used for AOP calls).
    // 将拦截器封装在DynamicAdvisedInterceptor中
    Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);

    // Choose a "straight to target" interceptor. (used for calls that are
    // unadvised but can return this). May be required to expose the proxy.
    Callback targetInterceptor;
    if (exposeProxy) {
        targetInterceptor = isStatic ?
                new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) :
                new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
    }
    else {
        targetInterceptor = isStatic ?
                new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) :
                new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
    }

    // Choose a "direct to target" dispatcher (used for
    // unadvised calls to static targets that cannot return this).
    Callback targetDispatcher = isStatic ?
            new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp();

    Callback[] mainCallbacks = new Callback[]{
        // 将拦截器链加入Callback中
        aopInterceptor, // for normal advice
        targetInterceptor, // invoke target without considering advice, if optimized
        new SerializableNoOp(), // no override for methods mapped to this
        targetDispatcher, this.advisedDispatcher,
        new EqualsInterceptor(this.advised),
        new HashCodeInterceptor(this.advised)
    };

    Callback[] callbacks;

    // If the target is a static one and the advice chain is frozen,
    // then we can make some optimisations by sending the AOP calls
    // direct to the target using the fixed chain for that method.
    if (isStatic && isFrozen) {
        Method[] methods = rootClass.getMethods();
        Callback[] fixedCallbacks = new Callback[methods.length];
        this.fixedInterceptorMap = new HashMap<String, Integer>(methods.length);

        // TODO: small memory optimisation here (can skip creation for methods with no advice)
        for (int x = 0; x < methods.length; x++) {
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
            fixedCallbacks[x] = new FixedChainStaticTargetInterceptor(
                    chain, this.advised.getTargetSource().getTarget(), this.advised.getTargetClass());
            this.fixedInterceptorMap.put(methods[x].toString(), x);
        }

        // Now copy both the callbacks from mainCallbacks
        // and fixedCallbacks into the callbacks array.
        callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
        System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
        System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
        this.fixedInterceptorOffset = mainCallbacks.length;
    }
    else {
        callbacks = mainCallbacks;
    }
    return callbacks;
}

在getCallback中Spring考虑了很多情况，getCallback中实现了一个目的：CGLIB对于方法的拦截是通过将自定义拦截器(MethodInterceptor接口)
加入Callback中并在调用代理时直接激活拦截器中的intercept方法来实现的。DynamicAdvisedInterceptor继承自MethodInterceptor，加入Callback中后
，在再次调用代理时会直接调用DynamicAdvisedInterceptor中的intercept方法


内部类：DynamicAdvisedInterceptor.java

private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
    // Parameters used for optimisation choices...
    boolean exposeProxy = this.advised.isExposeProxy();
    boolean isFrozen = this.advised.isFrozen();
    boolean isStatic = this.advised.getTargetSource().isStatic();

    // Choose an "aop" interceptor (used for AOP calls).
    Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);

    // Choose a "straight to target" interceptor. (used for calls that are
    // unadvised but can return this). May be required to expose the proxy.
    Callback targetInterceptor;
    if (exposeProxy) {
        targetInterceptor = isStatic ?
                new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) :
                new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
    }
    else {
        targetInterceptor = isStatic ?
                new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) :
                new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
    }

    // Choose a "direct to target" dispatcher (used for
    // unadvised calls to static targets that cannot return this).
    Callback targetDispatcher = isStatic ?
            new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp();

    Callback[] mainCallbacks = new Callback[]{
        aopInterceptor, // for normal advice
        targetInterceptor, // invoke target without considering advice, if optimized
        new SerializableNoOp(), // no override for methods mapped to this
        targetDispatcher, this.advisedDispatcher,
        new EqualsInterceptor(this.advised),
        new HashCodeInterceptor(this.advised)
    };

    Callback[] callbacks;

    // If the target is a static one and the advice chain is frozen,
    // then we can make some optimisations by sending the AOP calls
    // direct to the target using the fixed chain for that method.
    if (isStatic && isFrozen) {
        Method[] methods = rootClass.getMethods();
        Callback[] fixedCallbacks = new Callback[methods.length];
        this.fixedInterceptorMap = new HashMap<String, Integer>(methods.length);

        // TODO: small memory optimisation here (can skip creation for methods with no advice)
        for (int x = 0; x < methods.length; x++) {
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
            fixedCallbacks[x] = new FixedChainStaticTargetInterceptor(
                    chain, this.advised.getTargetSource().getTarget(), this.advised.getTargetClass());
            this.fixedInterceptorMap.put(methods[x].toString(), x);
        }

        // Now copy both the callbacks from mainCallbacks
        // and fixedCallbacks into the callbacks array.
        callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
        System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
        System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
        this.fixedInterceptorOffset = mainCallbacks.length;
    }
    else {
        callbacks = mainCallbacks;
    }
    return callbacks;
}




```













## 7.4 静态AOP使用示例


## 7.5 创建AOP静态代理






