package com.itmk.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: MK
 * @Description: 应用启动类
 * @Date: 2024/3/20
 */
@EnableWebMvc
@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.itmk.web.controller",    // 控制器
    "com.itmk.web.config",        // Web配置
    "com.itmk.web.exception",     // 异常处理
    "com.itmk.service",           // 服务层（包含impl）
    "com.itmk.config"             // 通用配置
})
@MapperScan("com.itmk.mapper")    // Mapper接口扫描
public class RestaurantApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }
} 