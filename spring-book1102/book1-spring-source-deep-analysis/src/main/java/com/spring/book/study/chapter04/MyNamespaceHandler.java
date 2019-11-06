package com.spring.book.study.chapter04;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        /**
         * 当遇到自定义标签<user:aaa>这样类似以user开头的元素，就会把这个元素扔给UserBeanDefinitionParser处理
         */
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}
