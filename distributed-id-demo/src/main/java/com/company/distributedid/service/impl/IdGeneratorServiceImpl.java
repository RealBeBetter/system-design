package com.company.distributedid.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.distributedid.entity.IdGenerator;
import com.company.distributedid.entity.IdGeneratorDTO;
import com.company.distributedid.mapper.IdGeneratorMapper;
import com.company.distributedid.service.IdGeneratorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Real
 * @description 针对表【id_generator】的数据库操作Service实现
 * @createDate 2022-10-16 17:06:15
 */
@Service
public class IdGeneratorServiceImpl extends ServiceImpl<IdGeneratorMapper, IdGenerator>
        implements IdGeneratorService {

    @Resource
    private IdGeneratorMapper idGeneratorMapper;

    @Override
    public List<Long> getGenerateId(Integer step) {
        IdGeneratorDTO maxId = idGeneratorMapper.getMaxId();
        if (Objects.isNull(maxId)) {
            idGeneratorMapper.initGenerateId();
        }
        IdGeneratorDTO currentMax = idGeneratorMapper.getMaxId();
        Integer version = currentMax.getVersion();
        Long max = currentMax.getMaxId();
        Integer id = currentMax.getId();
        IdGenerator idGenerator = new IdGenerator(id, max + step, step, 1, version + 1);
        List<Long> ids = new ArrayList<>();
        idGeneratorMapper.updateById(idGenerator);
        for (Long i = max; i < max + step; i++) {
            ids.add(i);
        }
        return ids;
    }
}




