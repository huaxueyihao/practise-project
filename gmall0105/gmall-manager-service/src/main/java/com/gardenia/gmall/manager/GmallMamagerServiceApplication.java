package com.gardenia.gmall.manager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.gardenia.gmall.manager.mapper")
public class GmallMamagerServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(GmallMamagerServiceApplication.class,args);

    }

}
