package com.gardenia.activemq.study.basic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducerTopicPersist {

    public static final String ACTIVEMQ_URL = "tcp://172.16.144.145:61616";
    public static final String TOPIC_NAME = "topic-persist";


    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);//
        MessageProducer messageProducer = session.createProducer(topic);
        for (int i = 0; i < 6; i++) {
            TextMessage textMessage = session.createTextMessage("msg-persist----" + i);
            messageProducer.send(textMessage);

        }
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("发布完成");


    }
}
