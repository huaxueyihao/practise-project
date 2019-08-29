package com.gardenia.activemq.study.produce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.jms.Topic;
import java.util.UUID;

@Component
public class Topic_Produce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Topic topic;

    public void produceTopic(){
        jmsMessagingTemplate.convertAndSend(topic,"主题消息："+ UUID.randomUUID().toString().substring(0,6));
    }

    @Scheduled(fixedDelay = 3000)
    public void produceMsgScheduled(){
        jmsMessagingTemplate.convertAndSend(topic, "****scheduled:" + UUID.randomUUID().toString().substring(0, 6));
        System.out.println("---produceMsgScheduled send message-");
    }

}