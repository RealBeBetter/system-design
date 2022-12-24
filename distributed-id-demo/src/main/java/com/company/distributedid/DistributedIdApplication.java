package com.company.distributedid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.company.distributedid.mapper")
public class DistributedIdApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedIdApplication.class, args);
    }

}
