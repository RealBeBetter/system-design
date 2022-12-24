package com.company.distributedid.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName sequence_id
 */
@TableName(value = "sequence_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceId implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * value值
     */
    private String value;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
