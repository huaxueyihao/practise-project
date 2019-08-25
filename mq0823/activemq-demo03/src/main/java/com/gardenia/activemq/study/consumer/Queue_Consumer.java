package com.gardenia.activemq.study.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.sql.SQLOutput;
import java.util.UUID;

@Component
public class Queue_Consumer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;


    @JmsListener(destination = "${myqueue}")
    public void receive(TextMessage textMessage) throws JMSException {
        System.out.println("*****************消费者收到消息：" + textMessage.getText());
    }

}
