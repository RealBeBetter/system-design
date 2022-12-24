package com.company.distributedid.service.impl;

import com.company.distributedid.common.IdWorker;
import com.company.distributedid.service.SnowflakeService;
import org.springframework.stereotype.Service;

/**
 * @author Real
 * Date: 2022/10/16 20:25
 */
@Service
public class SnowflakeServiceImpl implements SnowflakeService {
    @Override
    public Long getSnowId() {
        return IdWorker.id();
    }
}
