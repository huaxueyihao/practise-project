package com.gardenia.activemq.study.spring;


import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class SpringMQ_Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringMQ_Producer produce = (SpringMQ_Producer) app.getBean("springMQ_Producer");

//        produce.jmsTemplate.send(new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//
//                TextMessage textMessage = session.createTextMessage("******spring和ActiveMQ的整合case*****");
//                return textMessage;
//            }
//        });
        produce.jmsTemplate.send(session -> session.createTextMessage("******spring和ActiveMQ的整合case*****"));

        System.out.println("****** send message over*****");
    }

}
