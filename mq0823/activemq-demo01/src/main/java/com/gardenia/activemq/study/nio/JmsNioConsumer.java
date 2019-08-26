package com.gardenia.activemq.study.nio;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsNioConsumer {

    public static final String ACTIVEMQ_URL = "nio://172.16.144.145:61618";
    public static final String QUEUE_NAME = "nio-queue01";


    public static void main(String[] args) throws JMSException, IOException {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);//
        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("***************消息：" + textMessage.getText());
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
        /**
         * 1.先生产， 启动1号消费者，再启动2号消费者，问题：2号消费者还能消费消息吗
         *
         *
         * 2.先生产，先启动1号消费者，问题：1号
         *
         *
         *
         */

    }

}
