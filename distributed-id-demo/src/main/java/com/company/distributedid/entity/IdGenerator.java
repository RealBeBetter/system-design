package com.company.distributedid.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName id_generator
 */
@TableName(value = "id_generator")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdGenerator implements Serializable {
    /**
     *
     */
    @TableId
    private Integer id;

    /**
     * 当前最大id
     */
    private Long maxId;

    /**
     * 号段的布长
     */
    private Integer step;

    /**
     * 业务类型
     */
    private Integer bizType;

    /**
     * 版本号
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
