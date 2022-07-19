package com.beval.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RedditServerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditServerSpringApplication.class, args);
    }

}
