package com.company.distributedid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.distributedid.entity.SequenceId;

/**
 * @author Real
 * @description 针对表【sequence_id】的数据库操作Service
 * @createDate 2022-10-16 17:06:15
 */
public interface SequenceIdService extends IService<SequenceId> {

    Long getSequenceID(String value);

}
