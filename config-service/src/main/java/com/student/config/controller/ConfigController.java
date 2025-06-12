package com.student.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理控制器
 * 
 * 提供配置中心的管理接口，包括：
 * 1. 服务状态查询
 * 2. 配置信息获取
 * 3. 配置刷新通知
 * 
 * @author System
 * @version 1.0.0
 * @since 2024-06-11
 */
@Slf4j
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${spring.application.name:config-service}")
    private String applicationName;

    @Value("${server.port:8888}")
    private String serverPort;

    @Value("${spring.cloud.nacos.discovery.server-addr:localhost:8848}")
    private String nacosAddr;

    /**
     * 获取配置中心状态信息
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        log.info("获取配置中心状态信息");
        
        Map<String, Object> status = new HashMap<>();
        status.put("service", applicationName);
        status.put("port", serverPort);
        status.put("nacos", nacosAddr);
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        status.put("message", "Config Service is running normally");
        
        return status;
    }

    /**
     * 获取服务配置概览
     */
    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        log.info("获取配置服务概览信息");
        
        Map<String, Object> overview = new HashMap<>();
        overview.put("configServer", Map.of(
            "name", applicationName,
            "version", "1.0.0",
            "description", "Student Management System Config Center"
        ));
        
        overview.put("supportedServices", Map.of(
            "user-service", "学生用户管理服务",
            "class-service", "班级信息管理服务",
            "score-service", "成绩查看服务",
            "gateway-service", "API网关服务"
        ));
        
        overview.put("configSources", Map.of(
            "native", "本地文件配置",
            "nacos", "Nacos配置中心",
            "git", "Git仓库配置"
        ));
        
        overview.put("endpoints", Map.of(
            "health", "/actuator/health",
            "refresh", "/actuator/refresh",
            "env", "/actuator/env",
            "info", "/actuator/info"
        ));
        
        return overview;
    }

    /**
     * 配置刷新通知
     */
    @PostMapping("/refresh")
    public Map<String, Object> refreshConfig(@RequestParam(value = "service", required = false) String serviceName) {
        log.info("接收到配置刷新请求，服务: {}", serviceName);
        
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.put("operation", "refresh");
        result.put("service", serviceName != null ? serviceName : "all");
        result.put("status", "success");
        result.put("message", "配置刷新通知已发送");
        
        return result;
    }

    /**
     * 获取可用配置列表
     */
    @GetMapping("/list")
    public Map<String, Object> getConfigList() {
        log.info("获取可用配置列表");
        
        Map<String, Object> configList = new HashMap<>();
        configList.put("availableConfigs", Map.of(
            "user-service", Map.of(
                "default", "/user-service/default",
                "dev", "/user-service/dev", 
                "prod", "/user-service/prod"
            ),
            "class-service", Map.of(
                "default", "/class-service/default",
                "dev", "/class-service/dev",
                "prod", "/class-service/prod"
            ),
            "gateway-service", Map.of(
                "default", "/gateway-service/default",
                "dev", "/gateway-service/dev",
                "prod", "/gateway-service/prod"
            )
        ));
        
        configList.put("usage", "访问 /{service}/{profile} 获取对应配置");
        configList.put("example", "curl http://localhost:8888/user-service/default");
        
        return configList;
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", applicationName);
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        return health;
    }
}