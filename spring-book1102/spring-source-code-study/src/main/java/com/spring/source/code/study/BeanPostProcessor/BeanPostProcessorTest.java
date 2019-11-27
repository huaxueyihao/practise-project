package com.spring.source.code.study.BeanPostProcessor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanPostProcessorTest {



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
}
