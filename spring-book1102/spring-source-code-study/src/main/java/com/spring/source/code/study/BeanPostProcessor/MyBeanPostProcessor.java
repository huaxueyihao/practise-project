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
