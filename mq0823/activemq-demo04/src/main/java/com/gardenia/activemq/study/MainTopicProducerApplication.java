package com.gardenia.activemq.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainTopicProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainTopicProducerApplication.class, args);
    }

}
