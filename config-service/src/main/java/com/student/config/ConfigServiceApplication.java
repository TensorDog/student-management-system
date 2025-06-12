package com.student.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 配置中心服务启动类
 * 
 * 功能说明：
 * 1. 提供统一的配置管理服务
 * 2. 集成 Nacos 注册中心，实现服务发现
 * 3. 支持配置文件的动态刷新
 * 4. 为其他微服务提供配置获取接口
 * 
 * @author System
 * @version 1.0.0
 * @since 2024-06-11
 */
@Slf4j
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ConfigServiceApplication.class, args);
            log.info("===============================================");
            log.info("🚀 Config Service Started Successfully!");
            log.info("🌐 Server Port: 8888");
            log.info("📝 Config Management Available");
            log.info("🔗 Nacos Registration Enabled");
            log.info("===============================================");
        } catch (Exception e) {
            log.error("❌ Config Service startup failed: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}