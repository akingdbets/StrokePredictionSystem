package com.team3.stroke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 👈 추가

@SpringBootApplication
@EnableScheduling
public class StrokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrokeApplication.class, args);
    }

}