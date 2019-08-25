package com.gardenia.activemq.study.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpringTopicMQ_Consumer {


    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringTopicMQ_Consumer consumer = (SpringTopicMQ_Consumer) app.getBean("springTopicMQ_Consumer");

        String value = (String) consumer.jmsTemplate.receiveAndConvert();

        System.out.println("****** consumer message " + value + "*****");
    }
}
