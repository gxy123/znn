package com.taobao.znn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.taobao.znn.dao")
public class ZnnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZnnApplication.class, args);
    }

}
