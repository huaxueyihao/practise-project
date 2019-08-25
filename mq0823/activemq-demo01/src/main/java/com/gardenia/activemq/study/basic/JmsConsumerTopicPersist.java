package com.gardenia.activemq.study.basic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopicPersist {

    public static final String ACTIVEMQ_URL = "tcp://172.16.144.145:61616";
    public static final String TOPIC_NAME = "topic-persist";


    public static void main(String[] args) throws JMSException, IOException {
        // 1.创建连接工厂，按照给定的url地址，采用默认用户名和密码
        System.out.println("---------z3消费者-------");
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂，获得连接connection并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("Z3");


        // 3.创建会话session
        // 两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地（具体是对列是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);//
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark....");
        connection.start();

        Message message = topicSubscriber.receive();

        while (null != message) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*********收到持久化topic：" + textMessage.getText());
            message = topicSubscriber.receive();
        }

        session.close();
        connection.close();


    }

}
