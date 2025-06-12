package com.student.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.mybatis.spring.annotation.MapperScan;

/**
 * User Service Application
 * 用户服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.student.user", "com.student.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.student.user.mapper")
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("用户服务启动成功！");
    }
    
} 