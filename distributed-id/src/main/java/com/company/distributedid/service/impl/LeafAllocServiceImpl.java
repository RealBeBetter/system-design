package com.company.distributedid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.distributedid.entity.LeafAlloc;
import com.company.distributedid.mapper.LeafAllocMapper;
import com.company.distributedid.service.LeafAllocService;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Real
 * @description 针对表【leaf_alloc】的数据库操作Service实现
 * @createDate 2022-10-16 21:32:49
 */
@Slf4j
@Service
public class LeafAllocServiceImpl extends ServiceImpl<LeafAllocMapper, LeafAlloc>
        implements LeafAllocService {

    @Resource
    private IDGen idGen;

    @Override
    public Long getLeafId() {
        Result res = idGen.get("leaf-segment-test");
        return res.getId();
    }
}




