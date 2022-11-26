package com.example.mybatisplus_springboot.injectors;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年07月02日 10:49
 */
public class MySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();
        methodList.add(new FindAll());
        // 再扩充自定义的方法
        // list.add(new FindAll());
        return methodList;
    }
}
