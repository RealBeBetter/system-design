package com.company.distributedid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.distributedid.entity.IdGenerator;
import com.company.distributedid.entity.IdGeneratorDTO;

/**
 * @author Real
 * @description 针对表【id_generator】的数据库操作Mapper
 * @createDate 2022-10-16 17:06:15
 * @Entity com.company.distributedid.entity.IdGenerator
 */
public interface IdGeneratorMapper extends BaseMapper<IdGenerator> {

    Integer getGenerateId(IdGeneratorDTO dto);

    Integer initGenerateId();

    IdGeneratorDTO getMaxId();

}




