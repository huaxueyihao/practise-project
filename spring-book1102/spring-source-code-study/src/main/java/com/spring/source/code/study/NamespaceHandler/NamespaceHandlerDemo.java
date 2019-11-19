package com.spring.source.code.study.NamespaceHandler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

// NamespaceHandlerSupport 是 NamespaceHandler抽象实现
// 将标签和解析器关联起来，就是将解析器注册到容器中
// 解析的时候会通过标签前名称去找解析器，然后进行解析
public class NamespaceHandlerDemo extends NamespaceHandlerSupport {
    public void init() {
        /**
         * 当遇到自定义标签<demo:aaa>这样类似以demo开头的元素，就会把这个元素扔给UserBeanDefinitionParserDemo处理
         */
        registerBeanDefinitionParser("demo",new UserBeanDefinitionParserDemo());
    }
}
