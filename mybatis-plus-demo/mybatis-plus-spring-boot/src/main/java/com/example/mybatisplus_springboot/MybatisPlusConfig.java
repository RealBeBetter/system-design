package com.example.mybatisplus_springboot;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.example.mybatisplus_springboot.injectors.MySqlInjector;
import com.example.mybatisplus_springboot.plugins.MyInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年07月01日 14:28
 */
@Configuration
// 设置mapper接口的扫描包
@MapperScan("com.example.mybatisplus_springboot.mapper")
public class MybatisPlusConfig {

    @Bean       // 配置 MybatisPlus 的分页插件
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean       // 配置 Oracle 的主键生成器
    public OracleKeyGenerator oracleKeyGenerator() {
        return new OracleKeyGenerator();
    }

    @Bean       // 自定义拦截器（插件）
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }

    @Bean       // SQL分析插件配置
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();

        List<ISqlParser> list = new ArrayList<>();
        list.add(new BlockAttackSqlParser());   // 攻击阻断器，阻断删除、更新操作

        sqlExplainInterceptor.setSqlParserList(list);

        return sqlExplainInterceptor;
    }

    /*@Bean       // 乐观锁插件
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }*/

    @Bean         // 自定义SQL注入器
    public MySqlInjector mySqlInjector(){
        return new MySqlInjector();
    }

}
