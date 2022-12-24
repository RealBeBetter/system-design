package com.company.distributedid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.distributedid.entity.LeafAlloc;

/**
 * @author Real
 * @description 针对表【leaf_alloc】的数据库操作Service
 * @createDate 2022-10-16 21:32:49
 */
public interface LeafAllocService extends IService<LeafAlloc> {

    Long getLeafId();

}
