package com.student.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Gateway Service 启动类
 * 
 * @author Student Management System
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("🚀 Gateway Service 启动成功！");
    }

    /**
     * 配置 CORS 跨域处理
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 允许的域名
        corsConfig.addAllowedOriginPattern("http://localhost:*");
        corsConfig.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfig.addAllowedOriginPattern("http://192.168.*.*:*");
        
        // 允许的请求方法
        corsConfig.addAllowedMethod("*");
        
        // 允许的请求头
        corsConfig.addAllowedHeader("*");
        
        // 允许携带认证信息
        corsConfig.setAllowCredentials(true);
        
        // 预检请求的缓存时间
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}