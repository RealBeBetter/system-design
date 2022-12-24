package com.company.distributedid.service.impl;

import com.company.distributedid.service.UUIDService;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Real
 * Date: 2022/10/16 20:20
 */
@Service
public class UUIDServiceImpl implements UUIDService {

    @Override
    public String getUUID() {
        ObjectIdGenerators.UUIDGenerator uuidGenerator = new ObjectIdGenerators.UUIDGenerator();
        UUID uuid = uuidGenerator.generateId(this);
        String s = uuid.toString();
        return s.replaceAll("-", "").toUpperCase(Locale.ROOT);
    }
}
