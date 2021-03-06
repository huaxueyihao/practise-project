package com.gardenia.activemq.study.basic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducerTopic {

    public static final String ACTIVEMQ_URL = "tcp://172.16.144.145:61616";
    public static final String TOPIC_NAME = "topic-xiaoming";


    public static void main(String[] args) throws JMSException {

        // 1.创建连接工厂，按照给定的url地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂，获得连接connection并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session
        // 两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地（具体是对列是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);//
// 5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        // 6.循环创建多条消息
        for (int i = 0; i < 6; i++) {
            // 7.创建消息
            TextMessage textMessage = session.createTextMessage("msg----" + i);
            // 8.通过messageProducer发送Mq
            messageProducer.send(textMessage);

        }

        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("发布完成");


    }
}
