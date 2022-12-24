package com.company.distributedid.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Real
 * @TableName leaf_alloc
 */
@Data
@TableName(value ="leaf_alloc")
public class LeafAlloc implements Serializable {
    /**
     * 业务标识
     */
    @TableId
    private String bizTag;

    /**
     * 最大id
     */
    private Long maxId;

    /**
     * 步长
     */
    private Integer step;

    /**
     * 描述
     */
    private String description;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
