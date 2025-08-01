package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class LearnAzureApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnAzureApplication.class, args);
    }

}
