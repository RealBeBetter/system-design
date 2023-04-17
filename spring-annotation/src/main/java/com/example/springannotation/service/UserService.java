package com.example.springannotation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wei.song
 * @since 2023/4/17 11:05
 */
@Slf4j
@Service
public class UserService {

    private int start = 0;

    @Async
    public void asyncSaveUser() {
        String threadName = Thread.currentThread().getName();

        log.info("UserService running......");
        log.info("current Thread Name is: {}", threadName);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void retrySaveUser(int start) {
        if (this.start == 0) {
            this.start = start;
        }
        String threadName = Thread.currentThread().getName();

        log.info("UserService with retry running...... start is: {}", start);
        log.info("UserService with retry current Thread Name is: {}", threadName);

        int maxAttempts = 3;
        if (this.start < maxAttempts) {
            this.start++;
            throw new RuntimeException("UserService with retry exception");
        }

        log.info("UserService with retry end......");
        log.info("UserService with retry start is: {}", start);
    }

}
