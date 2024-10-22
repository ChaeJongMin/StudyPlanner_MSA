package com.studyplaner.statisticservcie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StatisticservcieApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticservcieApplication.class, args);
    }

}
