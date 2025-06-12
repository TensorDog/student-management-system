package com.student.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理 Gateway 中的异常，返回标准 JSON 格式
 * 
 * @author Student Management System
 */
@Slf4j
@Order(-1)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // 构建错误响应
        ErrorResponse errorResponse = buildErrorResponse(ex);
        
        // 设置状态码
        response.setStatusCode(HttpStatus.valueOf(errorResponse.getStatus()));

        // 记录错误日志
        log.error("Gateway 异常处理: {}", errorResponse.getMessage(), ex);

        try {
            String result = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(result.getBytes());
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("序列化错误响应失败", e);
            return Mono.error(e);
        }
    }

    /**
     * 构建错误响应对象
     */
    private ErrorResponse buildErrorResponse(Throwable ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setPath(getCurrentPath(ex));

        if (ex instanceof NotFoundException) {
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setError("服务不可用");
            errorResponse.setMessage("请求的服务暂时不可用，请稍后重试");
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorResponse.setStatus(responseStatusException.getStatusCode().value());
            HttpStatus httpStatus = HttpStatus.valueOf(responseStatusException.getStatusCode().value());
            errorResponse.setError(httpStatus.getReasonPhrase());
            errorResponse.setMessage(responseStatusException.getReason());
        } else if (ex instanceof java.net.ConnectException) {
            errorResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            errorResponse.setError("服务连接失败");
            errorResponse.setMessage("目标服务连接失败，请检查服务是否正常运行");
        } else if (ex instanceof java.util.concurrent.TimeoutException) {
            errorResponse.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
            errorResponse.setError("请求超时");
            errorResponse.setMessage("请求处理超时，请稍后重试");
        } else {
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setError("内部服务器错误");
            errorResponse.setMessage("系统内部错误，请联系管理员");
        }

        return errorResponse;
    }

    /**
     * 获取当前请求路径
     */
    private String getCurrentPath(Throwable ex) {
        // 这里可以根据异常类型或其他方式获取路径
        return "/gateway/error";
    }

    /**
     * 错误响应对象
     */
    public static class ErrorResponse {
        private long timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        // Getters and Setters
        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}