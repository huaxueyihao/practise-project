package com.spring.source.code.study.ApplicationContext;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContextTest {

    @Test
    public void testClassPathXmlApplicationContextTest() {
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("01.ApplicationContext/parentApplicationContext.xml");

    }


    @Test
    public void testMyClassPathXmlApplicationContextTestWithCustomPropertySource() {
        MyClassPathXmlApplicationContext myApp = new MyClassPathXmlApplicationContext("01.ApplicationContext/parentApplicationContext.xml");
    }


    @Test
    public void testParentAndChlidrenContext() {
        // 父容器拿不到子容器的bean
        ClassPathXmlApplicationContext parentApp = new ClassPathXmlApplicationContext("01.ApplicationContext/parentApplicationContext.xml");
        System.out.println(parentApp.getBean("parent"));

        // org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'children' is defined
        // System.out.println(parentApp.getBean("children"));
        ClassPathXmlApplicationContext childrenApp = new ClassPathXmlApplicationContext(new String[]{"01.ApplicationContext/childrenApplicationContext.xml"}, true, parentApp);
        System.out.println(childrenApp.getBean("parent"));
        System.out.println(childrenApp.getBean("children"));
    }


    @Test
    public void testChildrenContextWithPropertySource() {

        // 创建并设置父容器
        ClassPathXmlApplicationContext parentApp = createParentContext();
        // 容器
        ClassPathXmlApplicationContext childrenApp = new ClassPathXmlApplicationContext(new String[]{"01.ApplicationContext/childrenApplicationContext.xml"}, true, parentApp);
        System.out.println(childrenApp.getEnvironment().getRequiredProperty("children"));
        System.out.println(childrenApp.getEnvironment().getRequiredProperty("HELLO2"));
    }


    @Test
    public void testChildrenContextWithPlaceHolder() {

        // 创建并设置父容器
        ClassPathXmlApplicationContext parentApp = createParentContext();
        // org.springframework.context.support.ClassPathXmlApplicationContext@7113b13f
        System.out.println(parentApp);
        // org.springframework.context.support.ClassPathXmlApplicationContext@7113b13f
        System.out.println(parentApp.getId());
        // org.springframework.context.support.ClassPathXmlApplicationContext@7113b13f
        System.out.println(((DefaultListableBeanFactory) parentApp.getBeanFactory()).getSerializationId());
        // 子容器
        ClassPathXmlApplicationContext childrenApp = new ClassPathXmlApplicationContext(new String[]{"01.ApplicationContext/${children}.xml", "01.ApplicationContext/${context}.xml"}, true, parentApp);
        // org.springframework.context.support.ClassPathXmlApplicationContext@5d7148e2
        System.out.println(childrenApp);
        // org.springframework.context.support.ClassPathXmlApplicationContext@5d7148e2
        System.out.println(childrenApp.getId());

        DefaultListableBeanFactory childrenAppBeanFactory = (DefaultListableBeanFactory) childrenApp.getBeanFactory();
        // org.springframework.context.support.ClassPathXmlApplicationContext@5d7148e2
        System.out.println(childrenAppBeanFactory.getSerializationId());
        // org.springframework.context.support.ClassPathXmlApplicationContext@7113b13f
        // 这个是父应用上下文
        System.out.println(childrenApp.getParentBeanFactory());

    }

    private ClassPathXmlApplicationContext createParentContext() {
        /* 父容器 */
        ClassPathXmlApplicationContext parentApp = new ClassPathXmlApplicationContext("01.ApplicationContext/parentApplicationContext.xml");

        // 环境配置相关：自定义些变量在父容器中
        ConfigurableEnvironment environment = parentApp.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("children", "childrenApplicationContext");
        MapPropertySource mapProperty = new MapPropertySource("mapProperty", map);
        propertySources.addLast(mapProperty);

        ResourcePropertySource resourcePropertySource = null;
        try {
            resourcePropertySource = new ResourcePropertySource("01.ApplicationContext/resourceProperty.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        propertySources.addLast(resourcePropertySource);
        return parentApp;
    }


}
