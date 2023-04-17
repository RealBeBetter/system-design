package com.example.springannotation.test;

import com.example.springannotation.service.CallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wei.song
 * @since 2023/4/17 11:11
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CallServiceTests {

    @Autowired
    private CallService callService;

    @Test
    public void testCallService() {
        callService.callAsyncService();
    }

    @Test
    public void testCallRetryService() {
        callService.callRetryService();
    }

}
