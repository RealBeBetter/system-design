package com.company.distributedid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.distributedid.entity.SequenceId;
import com.company.distributedid.mapper.SequenceIdMapper;
import com.company.distributedid.service.SequenceIdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Real
 * @description 针对表【sequence_id】的数据库操作Service实现
 * @createDate 2022-10-16 17:06:15
 */
@Service
public class SequenceIdServiceImpl extends ServiceImpl<SequenceIdMapper, SequenceId>
        implements SequenceIdService {

    @Resource
    private SequenceIdMapper sequenceIdMapper;

    @Override
    public Long getSequenceID(String value) {
        SequenceId id = new SequenceId();
        id.setValue(value);
        int insert = sequenceIdMapper.insert(id);
        if (insert == 1) {
            return id.getId();
        }
        return null;
    }
}




