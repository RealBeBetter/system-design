package com.example.mybatisplus_springboot.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.mybatisplus_springboot.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author： Real
 * @ date： 2021年06月30日 16:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("tb_user")
public class User extends Model<User> {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;

    // 屏蔽字段查询结果，插入数据时填充字段
    @TableField(select = false, fill = FieldFill.INSERT)
    private String password;

    private String name;
    private Integer age;

    //@TableField(value = "email")        // 解决数据库中字段名不一致的问题
    private String email;

    /*@TableField(exist = false)          // 解决数据库中字段不存在的问题
    private String address;*/

    @Version                              // 乐观锁版本号字段
    private Integer version;

    @TableLogic                           // 逻辑删除标识字段
    private Integer deleted;

    private SexEnum sex;                  // 性别枚举字段

}
