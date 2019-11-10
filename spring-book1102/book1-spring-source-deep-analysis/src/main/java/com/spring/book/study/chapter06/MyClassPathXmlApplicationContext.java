package com.spring.book.study.chapter06;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    public MyClassPathXmlApplicationContext(String... configLocations) throws BeansException {
        super(configLocations);
    }

    @Override
    protected void initPropertySources() {

        // 这里是系统的环境变量
        // VAR是未定义的系统变量
        // JAVA_HOME是定义的系统变量
//        getEnvironment().setRequiredProperties("VAR");
        System.out.println(getEnvironment().getRequiredProperty("JAVA_HOME"));
    }

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        super.setAllowBeanDefinitionOverriding(false);
        super.setAllowCircularReferences(false);
        super.customizeBeanFactory(beanFactory);
    }
}
