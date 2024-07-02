package com.webinar;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.webinar.dao"})
public class TsetApplication {
    public static void main(String[] args) {
        SpringApplication.run(TsetApplication.class, args);
    }
}
