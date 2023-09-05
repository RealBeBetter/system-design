package com.company.asyncprogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author wei.song
 * @since 2023/09/05 19:08
 */
@EnableAsync
@SpringBootApplication
public class AsyncProgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncProgramApplication.class, args);
    }

}
