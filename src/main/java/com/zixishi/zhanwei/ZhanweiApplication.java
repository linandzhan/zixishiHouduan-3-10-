package com.zixishi.zhanwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.zixishi.zhanwei.mapper")
@ServletComponentScan
public class ZhanweiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhanweiApplication.class, args);
    }

}
