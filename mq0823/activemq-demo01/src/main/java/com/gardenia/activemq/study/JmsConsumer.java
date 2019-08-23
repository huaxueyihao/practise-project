package com.gardenia.activemq.study;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {

    public static final String ACTIVEMQ_URL = "tcp://172.16.144.145:61616";
    public static final String QUEUE_NAME = "queue01";


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
        Queue queue = session.createQueue(QUEUE_NAME);//
        // 5.创建消息的消费者
        MessageConsumer consumer = session.createConsumer(queue);
        // 6.循环创建多条消息
        while (true) {
            TextMessage textMessage = (TextMessage) consumer.receive();
            if (null != textMessage) {
                System.out.println("***************消息：" + textMessage.getText());
            } else {
                break;
            }

        }
        consumer.close();
        session.close();
        connection.close();


    }

}
