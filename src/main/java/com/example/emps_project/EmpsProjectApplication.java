package com.example.emps_project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.emps_project.mapper")
public class EmpsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpsProjectApplication.class, args);
    }

}
