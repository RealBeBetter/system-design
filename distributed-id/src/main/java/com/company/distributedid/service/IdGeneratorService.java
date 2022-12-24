package com.company.distributedid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.distributedid.entity.IdGenerator;

import java.util.List;

/**
 * @author Real
 * @description 针对表【id_generator】的数据库操作Service
 * @createDate 2022-10-16 17:06:15
 */
public interface IdGeneratorService extends IService<IdGenerator> {

    List<Long> getGenerateId(Integer step);

}
