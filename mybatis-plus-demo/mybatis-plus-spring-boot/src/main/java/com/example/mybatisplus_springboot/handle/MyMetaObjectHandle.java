package com.example.mybatisplus_springboot.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @ author： Real
 * @ date： 2021年07月02日 11:10
 */
@Component
public class MyMetaObjectHandle implements MetaObjectHandler {
    /**
     * 插入数据的时候填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object password = getFieldValByName("password", metaObject);
        if (null == password) {
        //字段为空，可以进行填充
            setFieldValByName("password", "123456", metaObject);
        }
    }

    /**
     * 更新数据的时候填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
