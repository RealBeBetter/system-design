package com.example.springannotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wei.song
 * @date 2023/04/17 11:10
 */
@EnableAsync
@EnableRetry
@SpringBootApplication
public class SpringAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAnnotationApplication.class, args);
    }

}
