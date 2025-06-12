package com.student.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 日志记录过滤器
 * 记录请求和响应的基本信息
 * 
 * @author Student Management System
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.currentTimeMillis();
        
        // 记录请求信息
        log.info("🚀 请求开始 - Method: {}, URI: {}, RemoteAddress: {}", 
                request.getMethod(), 
                request.getURI(), 
                request.getRemoteAddress());

        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                // 记录响应信息
                log.info("✅ 请求完成 - URI: {}, Status: {}, Duration: {}ms", 
                        request.getURI(), 
                        response.getStatusCode(), 
                        duration);
            })
        );
    }

    @Override
    public int getOrder() {
        return -1; // 最高优先级，最先执行
    }
}