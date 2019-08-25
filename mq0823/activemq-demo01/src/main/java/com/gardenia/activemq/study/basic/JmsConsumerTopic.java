package com.gardenia.activemq.study.basic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopic {

    public static final String ACTIVEMQ_URL = "tcp://172.16.144.145:61616";
    public static final String TOPIC_NAME = "topic-xiaoming";


    public static void main(String[] args) throws JMSException, IOException {
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
        // 5.创建消息的消费者
        MessageConsumer consumer = session.createConsumer(topic);

        //通过监听的方式来消费消息，MessageConsumer messageConsumer = session.createConsumer(queue);
        System.out.println("************consumer 2**********");
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("***************topic消息：" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 这里要阻塞
        System.in.read();
        consumer.close();
        session.close();
        connection.close();


    }

}
