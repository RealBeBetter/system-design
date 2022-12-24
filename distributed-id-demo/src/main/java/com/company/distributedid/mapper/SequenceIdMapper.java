package com.company.distributedid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.distributedid.entity.SequenceId;

/**
 * @author Real
 * @description 针对表【sequence_id】的数据库操作Mapper
 * @createDate 2022-10-16 17:06:15
 * @Entity com.company.distributedid.entity.SequenceId
 */
public interface SequenceIdMapper extends BaseMapper<SequenceId> {

    Integer addSequenceID(String value);

}




