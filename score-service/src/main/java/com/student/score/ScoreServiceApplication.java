package com.student.score;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Score Service Application
 * 成绩服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.student.score", "com.student.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.student.score.mapper")
public class ScoreServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ScoreServiceApplication.class, args);
        System.out.println("成绩服务启动成功！端口: 8083");
    }
    
}