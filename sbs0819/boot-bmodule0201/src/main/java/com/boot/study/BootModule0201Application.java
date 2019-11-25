package com.boot.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.boot.study.mapper")
public class BootModule0201Application {

    public static void main(String[] args) {
        SpringApplication.run(BootModule0201Application.class, args);
    }


}
