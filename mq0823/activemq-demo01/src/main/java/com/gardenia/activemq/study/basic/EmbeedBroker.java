package com.gardenia.activemq.study.basic;

import org.apache.activemq.broker.BrokerService;

public class EmbeedBroker {

    public static void main(String[] args) throws Exception {

        // ActvieMQ也支持在VM中通信基于嵌入式的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();


    }

}
