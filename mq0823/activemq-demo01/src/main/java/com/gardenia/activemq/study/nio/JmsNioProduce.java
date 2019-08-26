package com.gardenia.activemq.study.nio;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsNioProduce {

    public static final String ACTIVEMQ_URL = "nio://172.16.144.145:61618";
    public static final String QUEUE_NAME = "nio-queue01";


    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);//
        MessageProducer messageProducer = session.createProducer(queue);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("nio msg----" + i);
            messageProducer.send(textMessage);

        }

        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("发布完成");


    }

}
