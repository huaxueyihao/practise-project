package com.boot.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.boot.study.mapper")
public class LayUIModule0101Application {

    public static void main(String[] args) {
        SpringApplication.run(LayUIModule0101Application.class, args);
    }
}
