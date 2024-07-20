package com.fitbuddy.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class FitBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitBuddyApplication.class, args);
    }

}
