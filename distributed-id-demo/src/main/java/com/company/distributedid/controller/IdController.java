package com.company.distributedid.controller;

import com.company.distributedid.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Real
 * Date: 2022/10/16 16:51
 */
@Slf4j
@RestController
public class IdController {

    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private SequenceIdService sequenceIdService;
    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private UUIDService uuidService;
    @Autowired
    private SnowflakeService snowflakeService;
    @Autowired
    private LeafAllocService leafAllocService;

    @GetMapping("/id/generate/{step}")
    public List<Long> getId(@PathVariable("step") Integer step) {
        return idGeneratorService.getGenerateId(step);
    }

    @GetMapping("/id/sequence")
    public Long getSequenceId() {
        String value = this.getClass().toString();
        return sequenceIdService.getSequenceID(value);
    }

    @GetMapping("/id/redis")
    public Long getRedisId() {
        return redisIdService.getMaxId();
    }

    @GetMapping("/id/uuid")
    public String getUUID() {
        return uuidService.getUUID();
    }

    @GetMapping("/id/snow")
    public Long getSnowId() {
        return snowflakeService.getSnowId();
    }

    @GetMapping("/id/leaf")
    public Long getLeafId() {
        return leafAllocService.getLeafId();
    }

}
