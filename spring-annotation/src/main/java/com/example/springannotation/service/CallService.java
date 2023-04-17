package com.example.springannotation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wei.song
 * @since 2023/4/17 11:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallService {

    private final UserService userService;

    public void callAsyncService() {
        log.info("call Service start......");
        log.info("call Service thread is: {}", Thread.currentThread().getName());
        userService.asyncSaveUser();
        log.info("call Service end......");
    }

    public void callRetryService() {
        log.info("call retry Service start......");
        log.info("call retry Service thread is: {}", Thread.currentThread().getName());
        userService.retrySaveUser(2);
        log.info("call retry Service end......");
    }

}
