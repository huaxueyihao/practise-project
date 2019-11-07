5 bean的加载

```
MyTestBean bean = (MyTestBean) bf.getBean("myTestBean");


# AbstractBeanFactory.java中
public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}


protected <T> T doGetBean(
        final String name, final Class<T> requiredType, final Object[] args, boolean typeCheckOnly)
        throws BeansException {
    // 提取对应的beanName
    final String beanName = transformedBeanName(name);
    Object bean;

    /*
     * 检查缓存中或者实例工厂中是否有对应的实例
     * 为什么首先会使用这段代码呢
     * 因为在创建单例Bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，
     * Spring创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光
     * 也就是将ObjectFactory加入到缓存中，一旦下一个bean创建的时候需要依赖上个bean则直接使用ObjectFactory
     ***/
     // 直接尝试从缓存获取或则singletonFactories中的ObjectFactory中获取
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
        // 返回对应的实例，有时候存在诸如BeanFactory的情况并不是直接返回实例本身而是返回指定方法返回的实例
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }

    else {
        // Fail if we're already creating this bean instance:
        // We're assumably within a circular reference.
        // 只有在单例情况下回尝试解决循环依赖，原型模式情况下，如果存在
        // A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为
        // 对于B的创建再次返回创建A，造成循环依赖，也就是下面的情况
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        }

        // Check if bean definition exists in this factory.
        BeanFactory parentBeanFactory = getParentBeanFactory();
        // 如果beanDefinitionMap中也就是在所有已经加载的类中不包括beanName则尝试从parentBeanFactory中加测
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // Not found -> check parent.
            String nameToLookup = originalBeanName(name);
            if (args != null) {
                // 递归到BeanFactory中寻找
                // Delegation to parent with explicit args.
                return (T) parentBeanFactory.getBean(nameToLookup, args);
            }
            else {
                // No args -> delegate to standard getBean method.
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
        }
        
        // 如果不是仅仅做类型检查则是创建bean，这里要进行记录
        if (!typeCheckOnly) {
            markBeanAsCreated(beanName);
        }

        try {
            // 将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition，如果指定
            // BeanName是子Bean的话同时会合并父类的相关属性
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            // Guarantee initialization of beans that the current bean depends on.
            String[] dependsOn = mbd.getDependsOn();
            // 若存在依赖则需要递归实例化依赖的Bean
            if (dependsOn != null) {
                for (String dependsOnBean : dependsOn) {
                    if (isDependent(beanName, dependsOnBean)) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "Circular depends-on relationship between '" + beanName + "' and '" + dependsOnBean + "'");
                    }
                    // 缓存依赖调用
                    registerDependentBean(dependsOnBean, beanName);
                    getBean(dependsOnBean);
                }
            }

            // Create bean instance.
            // 实例化依赖的bean后便可以实例化mbd本身了
            // singleton模式的创建
            if (mbd.isSingleton()) {
                sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        try {
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
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }
            
            else if (mbd.isPrototype()) {
                // prototype模式的创建
                // It's a prototype -> create a new instance.
                Object prototypeInstance = null;
                try {
                    beforePrototypeCreation(beanName);
                    prototypeInstance = createBean(beanName, mbd, args);
                }
                finally {
                    afterPrototypeCreation(beanName);
                }
                bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
            }

            else {
                // 指定的scope上实例化bean
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
    
    // 检查需要的类型是否符合bean的实际类型
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


加载的步骤:
1.转换对应beanName
  传入的name可能是别名，也可能是FactoryBean。
  去除FactoryBean的修饰符，也就是如果name="&aa",那么会首先去除&而使name="aa"
  取指定alias锁表示的最终beanName，例如别名A指向名称为B的bean则返回B；若别名A指向别名B，别名B又指向名称为C的bean则返回C。
2.尝试从缓存中加载单例
  单例在Spring的同一个容器内只会被创建一次，后续再获取bean，就直接从单例缓存中获取了。在Spring中创建bean的原则是不等bean创建完成
  就会将创建bean的ObjectFactory提早曝光加入到缓存中，一旦下一个bean创建时候需要依赖上一个bean则直接使用ObjectFactory。
3.bean的实例化
  从缓存中得到了bean的原始状态，则需要对bean进行实例化。缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。假如我们需要
  对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是真正需要的是工厂bean中定义的factory-method方法中返回的bean，而
  getObjectForBeanInstance就是完成这个工作的。

4.原型模式的依赖检查
 只有在单例情况下才会尝试解决循环依赖，如果存在A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为对于B的
 创建再次返回创建A，造成循环依赖，也就是情况：isPrototypeCurrentInCreation(beanName)判断为true。

5.检测parentBeanFactory
 如果缓存没有数据的话直接转到父类工厂上去加载了
 parentBeanFactory != null && !containsBeanDefinition(beanName)，
 
6.将存储XML配置文件的GernericBeanDefinition转换WieRootBeanDefinition
 因为从XML配置文件中读取到的bean信息是存储在GerericBeanDefinition中的，但是所有的bean后续处理都是针对于RootBeanDefinition的，所以
 这里需要进行一个转换，转换的同时如果父类bean部位空的话，则会一并合并父类的属性。

7.寻找依赖
 因为bean的初始化过程中很可能用到某些属性，而某些属性很可能是动态配置的，并且配置成依赖于其他的bean，那么这个时候就有必要加载依赖的bean，
 所以，在Spring的加载顺序中，在初始化某一个bean的时候手续爱你会初始化这个bean锁对应的依赖。
 
8.针对不同的scope进行bean的创建
 在Spring中存在着不同的scope，其中默认是singleton，但是还有些其他的配置如prototype、request之类的。这个步骤中，Spring会根据不同的
 配置进行不同的初始化策略 

9.类型转换


```

![avatar](images/01_get_bean.jpg)

### 5.1 FactoryBean的使用

```
Spring提供了一个org.springframework.beans.factory.FactoryBean的工厂列接口，
用户可以通过实现该接口定制实例化bean的逻辑。Spring自身提供了70多个FactoryBean的实现。

public interface FactoryBean<T> {

    // 返回有FactoryBean创建的bean实例，如果isSingleton()返回true，则该实例会放到Spring容器中单实例缓存池中。
	T getObject() throws Exception;

    // 返回FactoryBean创建的bean类型
	Class<?> getObjectType();
    
    // 返回由FactoryBean创建的bean实例的作用域是singleton还是prototype
	boolean isSingleton();
	
}

在com.spring.book.study.chapter05新增Car，CarFactoryBean
package com.spring.book.study.chapter05;

public class Car {

    private int maxSpeed;

    private String brand;

    private double price;

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "maxSpeed=" + maxSpeed +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }
}


package com.spring.book.study.chapter05;

import org.springframework.beans.factory.FactoryBean;

public class CarFactoryBean implements FactoryBean<Car> {

    private String carInfo;

    public Car getObject() throws Exception {
        Car car = new Car();
        String[] infos = carInfo.split(",");
        car.setBrand(infos[0]);
        car.setMaxSpeed(Integer.valueOf(infos[1]));
        car.setPrice(Double.valueOf(infos[1]));
        return car;
    }

    public Class<?> getObjectType() {
        return Car.class;
    }

    public boolean isSingleton() {
        return false;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }
}

在src/main/resources/下新增factoryBeanTest.java

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="car" class="com.spring.book.study.chapter05.CarFactoryBean">
        <property name="carInfo" value="超级跑车,400,2000000"/>
    </bean>
</beans>

测试类

package com.spring.book.study.chapter05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ap = new ClassPathXmlApplicationContext("factoryBeanTest.xml");
        Car car = (Car) ap.getBean("car");
        // Car{maxSpeed=400, brand='超级跑车', price=400.0}
        System.out.println(car);

        // 在getBean(beanName)的beanName前加上"&"，可以获得FactoryBean
        Object carFactoryBean =  ap.getBean("&car");
        // class com.spring.book.study.chapter05.CarFactoryBean
        System.out.println(carFactoryBean.getClass());

    }
}

```

### 5.2 缓存中获取单例bean

```

单例在Spring的同一个容器内只会被创建异常，后续再获取bean直接从单例缓存中获取，当然这里也只是尝试加载，首先尝试从缓存中加载，
然后再次尝试尝试从singletonFactories中加载。因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环
依赖，Spring创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加到缓存中，一旦下一个bean创建时需要依赖
上个bean的，直接使用ObjectFactory

public Object getSingleton(String beanName) {
    return getSingleton(beanName, true);
}

protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    // 检查缓存中是否存在实例
    Object singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        // 锁定全局变量
        synchronized (this.singletonObjects) {
            // 如果此bean正在加载则不处理
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                // 当某些方法需要提前初始化的时候则会调用addSingletonFactory方法将对应的
                // ObjectFactory初始化策略存储在singletonFactories
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    // 调用预先设定的getObject方法
                    singletonObject = singletonFactory.getObject();
                    // 记录在缓存中，earlySingletonObjects和singletonFactories互斥
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    return (singletonObject != NULL_OBJECT ? singletonObject : null);
}

首先尝试从singletonObjects里面获取实例，如果获取不到再从earlySingletonObjects里面获取，如果还获取不到，
再尝试从singletonFactories里面获取beanName对应的ObjectFactory，然后调用这个ObjeectFactory的getObject来创建bean
并放到earlySingletonObjects里面去，并且从singletonFactories里面remove掉这个ObjectFactory，而对于后续的所有内存操作
都只为了循环依赖检测时候使用，也就是在allowEarlyReference为true的情况下才会使用。
singletonObjects：用于保存BeanName和创建bean实例之间的关系，bean name-->bean instance
singletonFactories：用于保存BeanName和创建bean的工厂之间的关系，bean name--> ObjectFactory
earlySingletonObjects：也是保存BeanName和创建bean实例之间的关系，与singletonObjects的不同之处在于，
当一个单例bean被放到这里面后，那么当bean还在创建过程中，就可以通过getBean方法获取到了，七亩地是用来检测循环引用。
registeredSingletons：用力啊保存当前所有已注册的bean。


```

## 5.3 从bean的实例中获取对象

```
getObjectForBeanInstance是在得到bean的实力后要做的第一步就是调用这个方法来检测一下正确性，其实就是用于检车当前bean是否是FactoryBean
类型的bean，如果是，那么需要调用该bean对应的FactoryBean实例中的getObject()作为返回值。
无论是从缓存中获取到的bean还是通过不同的scope策略加载的bean都只是最原始的bean状态，并不一定是我们最终想要的bean。

protected Object getObjectForBeanInstance(
			Object beanInstance, String name, String beanName, RootBeanDefinition mbd) {

    // 如果指定的name是工厂相关(以&为前缀)且beanInstance又不是FactoryBean类型则验证不通过
    // Don't let calling code try to dereference the factory if the bean isn't a factory.
    if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
        throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
    }
    
    // 现在我们有了个bean的实例，这个实例可能会是正常的bean或者是FactoryBean
    // 如果是FactoryBean我们使用它创建实例，但是如果用户想要直接获取工厂实例而不是工厂的getObject方法对应的实例那么传入的name应该加入前缀&
    // Now we have the bean instance, which may be a normal bean or a FactoryBean.
    // If it's a FactoryBean, we use it to create a bean instance, unless the
    // caller actually wants a reference to the factory.
    if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
        return beanInstance;
    }

    // 加载FactoryBean
    Object object = null;
    if (mbd == null) {
        // 尝试从缓存中加载bean
        object = getCachedObjectForFactoryBean(beanName);
    }
    if (object == null) {
        // 到这里已经明确知道beanInstance一定是FactoryBean类型
        // Return bean instance from factory.
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        // Caches object obtained from FactoryBean if it is a singleton.
        if (mbd == null && containsBeanDefinition(beanName)) {
            // 将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition，
            // 如果指定BeanName是子Bean的话同时会和并父类的相关属性
            mbd = getMergedLocalBeanDefinition(beanName);
        }
        // 是否是用户定义的而不是应用程序本身定义的
        boolean synthetic = (mbd != null && mbd.isSynthetic());
        object = getObjectFromFactoryBean(factory, beanName, !synthetic);
    }
    return object;
}

getObjectForBeanInstance所做的工作：
1.对FactoryBean正确性的验证。
2.对非FactoryBean不做任何处理
3.对bean进行装换
4.将从Factory中解析bean的工作委托给getObjectFromFactoryBean。


protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
    // 如果是单例模式
    if (factory.isSingleton() && containsSingleton(beanName)) {
        synchronized (getSingletonMutex()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factory, beanName);
                // Only post-process and store if not put there already during getObject() call above
                // (e.g. because of circular reference processing triggered by custom getBean calls)
                Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
                if (alreadyThere != null) {
                    object = alreadyThere;
                }
                else {
                    if (object != null && shouldPostProcess) {
                        try {
                            // 调用ObjectFactory的后处理器
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


# FactoryBeanRegistrySupport.java
private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName)
			throws BeanCreationException {

    Object object;
    try {
        // 需要权限验证
        if (System.getSecurityManager() != null) {
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
            // 直接调用getObject方法
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


# AbstractAutowireCapableBeanFactory.java 后处理器
protected Object postProcessObjectFromFactoryBean(Object object, String beanName) {
    return applyBeanPostProcessorsAfterInitialization(object, beanName);
}


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

## 5.4 获取单例


```


public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(beanName, "'beanName' must not be null");
    // 全局变量需要同步
    synchronized (this.singletonObjects) {
        // 首先检查对应的bean是否已经加载过，因为singleton模式其实就是复用已创建的bean
        // 所以这一步是必须的
        Object singletonObject = this.singletonObjects.get(beanName);
        // 如果为空才可以进行singleton的bean初始化
        if (singletonObject == null) {
            if (this.singletonsCurrentlyInDestruction) {
                throw new BeanCreationNotAllowedException(beanName,
                        "Singleton bean creation not allowed while the singletons of this factory are in destruction " +
                        "(Do not request a bean from a BeanFactory in a destroy method implementation!)");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
            }
            beforeSingletonCreation(beanName);
            boolean newSingleton = false;
            boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
            if (recordSuppressedExceptions) {
                this.suppressedExceptions = new LinkedHashSet<Exception>();
            }
            try {
                // 初始化bean
                singletonObject = singletonFactory.getObject();
                newSingleton = true;
            }
            catch (IllegalStateException ex) {
                // Has the singleton object implicitly appeared in the meantime ->
                // if yes, proceed with it since the exception indicates that state.
                singletonObject = this.singletonObjects.get(beanName);
                if (singletonObject == null) {
                    throw ex;
                }
            }
            catch (BeanCreationException ex) {
                if (recordSuppressedExceptions) {
                    for (Exception suppressedException : this.suppressedExceptions) {
                        ex.addRelatedCause(suppressedException);
                    }
                }
                throw ex;
            }
            finally {
                if (recordSuppressedExceptions) {
                    this.suppressedExceptions = null;
                }
                afterSingletonCreation(beanName);
            }
            if (newSingleton) {
                // 加入缓存
                addSingleton(beanName, singletonObject);
            }
        }
        return (singletonObject != NULL_OBJECT ? singletonObject : null);
    }
}

上述代码中其实是使用了回调方法，使得程序可以在单例创建的前后做一些准备及处理操作，而真正的
获取单例bean的方法其实并不是在此方法中实现的，实现逻辑是在ObjectFactory类型的实例singletonFactory中
实现的。
1.检查缓存是否已经加载过
2.若没有加载，则记录beanName的正在加载状态，
3.加载单例前记录加载状态
  beforSingletonCreation做的事情：记录加载状态，也就是通过this.singletonsCurrentlyInCreation.add(beanName)
  将当前正要创建的bean记录在缓存中，
  protected void beforeSingletonCreation(String beanName) {
    if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
        throw new BeanCurrentlyInCreationException(beanName);
    }
}
4.通过调用参数传入的ObjectFactory的个体ObjectFactory的个体Object方法实例化bean。
5.加载单例后的处理方法调用
 同步骤3的记录加载状态相似，当bean架子啊结束后需要移除缓存中的对该bean的正确加载状态的记录
 protected void afterSingletonCreation(String beanName) {
    if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
        throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
    }
 }
 
6.将结果记录至缓存并删除加载bean过程中所记录的各种辅助状态
protected void addSingleton(String beanName, Object singletonObject) {
    synchronized (this.singletonObjects) {
        this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
        this.singletonFactories.remove(beanName);
        this.earlySingletonObjects.remove(beanName);
        this.registeredSingletons.add(beanName);
    }
}

7.返回处理结果
bean的加载逻辑其实是在传入的ObjectFactory类型的参数singletonFactory中定义的，反推参数的获取，得到如下代码：
sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
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




```

## 5.5 准备创建bean

> 一个真正的干活的函数其实是以do开头的，比如doGetObjectFromFactoryBean。而给我们错觉的函数，比如getObjectFromFactoryBean
  其实只是从全局的角度去做一些统筹的工作。这个规则对于createBean也比例外。
  
```
# AbstractAutowireCapableBeanFactory.java

protected Object createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
			throws BeanCreationException {
    
    
    if (logger.isDebugEnabled()) {
        logger.debug("Creating instance of bean '" + beanName + "'");
    }
    // Make sure bean class is actually resolved at this point.
    // 锁定class，根据设置的class属性或者根据className来解析Class
    resolveBeanClass(mbd, beanName);

    // Prepare method overrides.
    // 验证及准备覆盖的方法
    try {
        mbd.prepareMethodOverrides();
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanDefinitionStoreException(mbd.getResourceDescription(),
                beanName, "Validation of method overrides failed", ex);
    }

    try {
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        // 给BeanPostProcessors一个机会来返回代理来替代真正的实例
        Object bean = resolveBeforeInstantiation(beanName, mbd);
        if (bean != null) {
            return bean;
        }
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                "BeanPostProcessor before instantiation of bean failed", ex);
    }
    
    // 真正干活的方法
    Object beanInstance = doCreateBean(beanName, mbd, args);
    if (logger.isDebugEnabled()) {
        logger.debug("Finished creating instance of bean '" + beanName + "'");
    }
    return beanInstance;
}

上述代码的步骤：
1.根据设置的class属性或者根据ClassName来解析Class
2.对override属性进行标记及验证。
  在Spring中没有override-method这样的配置，存在lookup-method和replace-method的，而这两个配置的加载其实就
  是讲配置统一存放在BeanDefinition中的methodOverrides属性里，而这个函数的操作其实也就是针对于这两个配置的
3.应用初始化前的后处理器，解指定bean是否存在初始化前的短路操作。
4.创建bean。


```

### 5.5.1 处理override属性

```
# AbstractBeanDefinition.java
public void prepareMethodOverrides() throws BeanDefinitionValidationException {
    // Check that lookup methods exists.
    MethodOverrides methodOverrides = getMethodOverrides();
    if (!methodOverrides.isEmpty()) {
        for (MethodOverride mo : methodOverrides.getOverrides()) {
            prepareMethodOverride(mo);
        }
    }
}


protected void prepareMethodOverride(MethodOverride mo) throws BeanDefinitionValidationException {
    //获取对应类中对饮方法名的个数
    int count = ClassUtils.getMethodCountForName(getBeanClass(), mo.getMethodName());
    if (count == 0) {
        throw new BeanDefinitionValidationException(
                "Invalid method override: no method with name '" + mo.getMethodName() +
                "' on class [" + getBeanClassName() + "]");
    }
    else if (count == 1) {
        // Mark override as not overloaded, to avoid the overhead of arg type checking.
        // 编辑MethodOverride暂未被覆盖，避免参数类型检查的开销
        mo.setOverloaded(false);
    }
}

```

> 在Spring配置中存在lookup-method和replace-method两个配置功能，而这两个配置的加载其实就是将配置统一存放在
  BeanDefinition中的methodOverrides属性里，这两个功能实现原理其实是在bean实例化的时候如果检测到存在
  methodOverrides属性里，这两个功能实现原理其实是在bean实例化的时候如果检测到存在methodOverrides属性，会动态
  地位当前bean生成代理并使用对应的拦截器为bean做增强处理，相关逻辑是现在bean的实例化不封详细介绍
  
### 5.5.2 实例化的前置处理

> 在真正调用doCreate方法创建bean的实例前使用了这样一个方法resolveBeforeInstantiation对BeanDefinition中属性
  做些前置处理。
  
```

# AbstractAutowireCapableBeanFactory.java
# createBean方法中的前置处理
Object bean = resolveBeforeInstantiation(beanName, mbd);
# 短路判断，前置处理后返回的结果如果不为空，那么会直接略过后续的bean的创建而直接返回结果
if (bean != null) {
    return bean;
}

protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
    Object bean = null;
    // 如果尚未被解析
    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
        // Make sure bean class is actually resolved at this point.
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            Class<?> targetType = determineTargetType(beanName, mbd);
            if (targetType != null) {
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
        }
        mbd.beforeInstantiationResolved = (bean != null);
    }
    return bean;
}




```  

**1.实例化前的后处理器应用**

> bean的实例化前调用，也就是将AbstractBeanDefinition转换为BeanWrapper前的处理。给子类一个修改BeanDefinition的机会
  也就是说当程序经过这个方法后，可能不是我们认为的bean了，或许是处理过的代理bean，可能是cglib生成的，也可能是通过其他技术生成的 
  
```

# AbstractAutowireCapableBeanFactory.java

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

**2.实例化后的后处理器应用**

> Spring中的规则是在bean的初始化后尽可能保证将注册的后处理器的postProcessAfterInitialization
  方法应用到该bean中，因为如果返回的bean不为空，那么便不会再次经历普通bean的创建过程。
  
```
# AbstractAutowireCapableBeanFactory.java

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

## 5.6 循环依赖

### 5.6.1 什么是循环依赖

> 循环依赖就是循环引用，就是两个或多个bean相互之间的持有对方，比如CircleA引用CircleB，CircleB引用CircleC，CircleC引用CircleA，则它们最终反映为一个环。

![avatar](images/02_circle_dependence.jpg)

### 5.6.2 Spring如何解决循环依赖

> Spring容器循环依赖包括构造器循环依赖和setter循环依赖。
  在Spring中将循环一开的处理分成3种情况
  

```

在com.spring.book.study.chapter05 创建TestA，TestB，TestC。

package com.spring.book.study.chapter05;
public class TestA {

    private TestB testB;

    public void a(){
        testB.b();
    }

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }
}

package com.spring.book.study.chapter05;

public class TestB {

    private TestC testC;

    public void b(){
        testC.c();
    }

    public TestC getTestC() {
        return testC;
    }

    public void setTestC(TestC testC) {
        this.testC = testC;
    }
}

package com.spring.book.study.chapter05;

public class TestC {

    private TestA testA;

    public void c() {
        testA.a();
    }

    public TestA getTestA() {
        return testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }
}


```

**1.构造器循环依赖**
> 表示通过构造器注入构成的循环依赖，此依赖是无法解决的，只能抛出BeanCurrentlyInCreationException异常表示循环依赖。
  创建TestA，需要构建TestB，创建TestB，需要构建TestC，创建TestC，需要TestA，从而形成一个环，没办法创建。
  Spring容器将每一个正在创建的bean标识符放在一个"当前创建bean池"中，bean表示符在创建过程中将一致保持在这个池子中，因此如果在
  创建bean过程中发现自己已经在"当前创建bean池"里时，将抛出BeanCurrentlyInCreationException异常表示循环依赖；而对于创建完毕的
  bean将从"当前创建的bean池"中清除掉

```
# 1.创建配置文件constructorCircleTest.xml。

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="testA" class="com.spring.book.study.chapter05.TestA">
        <constructor-arg index="0" ref="testB" />
    </bean>

    <bean id="testB" class="com.spring.book.study.chapter05.TestB">
        <constructor-arg index="0" ref="testC" />
    </bean>

    <bean id="testC" class="com.spring.book.study.chapter05.TestC">
        <constructor-arg index="0" ref="testA" />
    </bean>

</beans>

# 测试类
package com.spring.book.study.chapter05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static ApplicationContext ap;

    public static void main(String[] args) throws Exception {
        // Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException:
        testConstructorCirleDepend();

    }

    private static void testConstructorCirleDepend() throws Exception {
        ap = new ClassPathXmlApplicationContext("constructorCircleTest.xml");
    }

    public static void testFactBean() {
        ap = new ClassPathXmlApplicationContext("factoryBeanTest.xml");
        Car car = (Car) ap.getBean("car");
        // Car{maxSpeed=400, brand='超级跑车', price=400.0}
        System.out.println(car);

        Object carFactoryBean = ap.getBean("&car");
        // class com.spring.book.study.chapter05.CarFactoryBean
        System.out.println(carFactoryBean.getClass());
    }

}

测试结果抛出异常：
 Error creating bean with name 'testA': Requested bean is currently in creation: Is there an unresolvable circular reference?


```  

> Spring容器创建"testA"bean，首先去"当前创建bean池"查找是否当前bean正在创建，如果发现，则继续准备器需要的构造器参数"testB"，并将"testA"表示符放到"当前创建bean池"
  Spring容器创建"testB"bean，首先去"当前创建bean池"查找是否当前bean正在创建，如果发现，则继续准备器需要的构造器参数"testC"，并将"testB"表示符放到"当前创建bean池"
  Spring容器创建"testC"bean，首先去"当前创建bean池"查找是否当前bean正在创建，如果发现，则继续准备器需要的构造器参数"testA"，并将"testC"表示符放到"当前创建bean池"
  Spring容器创建"testA"bean，发现该bean标识符在"当前创建bean池"中，因为表示循环依赖，抛出BeanCurrentlyInCreationException
  
**2.setter循环依赖**

> 对于setter注入造成的依赖是通过Spring容器提前暴露刚完成构造器注入但为完成其他步骤(setter注入)的bean来完成的，而且只能解决单例作用域的bean循环依赖，
  通过提前暴露一个单例工厂的方法，从而使其他bean能引用到该bean。

```


#AbstractAutowireCapableBeanFactory.java
addSingletonFactory(beanName, new ObjectFactory<Object>() {
    @Override
    public Object getObject() throws BeansException {
        return getEarlyBeanReference(beanName, mbd, bean);
    }
});


```

> 1.Spring容器创建单例"testA"bean，首先根据无参构造器创建bean并暴露一个"ObjectFactory"用于返回一个提前暴露一个创建中的bean，
  并将"testA"标识符放到"当前创建bean池"，然后进行setter注入"testB"
  2.Spring容器创建单例"testB"bean，首先根据无参构造器创建bean，并暴露一个"ObjectFactory"用于返回一个提前暴露一个创建中的bean，
  并将"testB"标识符放到"当前创建bean池"，然后进行setter注入"circle"
  3.Spring容器创建单例"testC"bean，首先根据无参构造器创建bean，并暴露一个"ObjectFactory"用于返回一个提前暴露一个创建中的bean，
  并将"testC"标识符放到"当前创建bean池"，然后进行setter注入"testA"。进行注入"testA"时由于提前暴露了"ObjectFactory"工厂，从而使
  它返回提前暴露一个创建中的bean。
  4.最后在依赖注入"testB"和"testA"，完成setter注入。
  
**3.prototype**
> 对于"prototype"作用域bean，Spring容器无法完成依赖注入，因为Spring容器不进行缓存"prototype"作用域的bean。
  对于"singleton"作用域bean，可以通过"setAllowCircularReferences(false)"来禁用循环引用



## 5.7 创建bean



  

















