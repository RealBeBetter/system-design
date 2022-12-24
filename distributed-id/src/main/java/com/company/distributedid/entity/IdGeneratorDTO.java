package com.company.distributedid.entity;

import lombok.Data;

/**
 * @author Real
 * Date: 2022/10/16 19:09
 */
@Data
public class IdGeneratorDTO {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 最大ID
     */
    private Long maxId;
    /**
     * 版本号
     */
    private Integer version;

}
