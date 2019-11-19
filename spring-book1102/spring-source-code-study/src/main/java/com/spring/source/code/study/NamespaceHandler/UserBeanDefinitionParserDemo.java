package com.spring.source.code.study.NamespaceHandler;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

// 自定义解析器
public class UserBeanDefinitionParserDemo extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return Demo.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        String demoName = element.getAttribute("demoName");
        String demoDesc = element.getAttribute("demoDesc");

        if (StringUtils.hasText(demoName)) {
            bean.addPropertyValue("demoName", demoName);
        }
        if (StringUtils.hasText(demoDesc)) {
            bean.addPropertyValue("demoDesc", demoDesc);
        }
    }
}
