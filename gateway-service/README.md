# Gateway Service - API 网关服务

## 📖 项目说明

Gateway Service 是学生信息管理系统的 API 网关服务，负责：
- 统一路由管理
- 跨域处理 (CORS)
- 统一异常处理
- 请求日志记录

## 🚀 服务配置

### 端口配置
- **服务端口**: 8080
- **管理端口**: 8080/actuator

### 路由配置

| 路由路径 | 目标服务 | 服务端口 | 说明 |
|---------|---------|----------|------|
| `/api/users/**` | user-service | 8081 | 用户管理服务 |
| `/api/classes/**` | class-service | 8082 | 班级管理服务 |
| `/api/scores/**` | score-service | 8083 | 成绩管理服务 |

### 示例请求

```bash
# 通过网关访问用户服务
curl http://localhost:8080/api/users/list

# 通过网关访问班级服务  
curl http://localhost:8080/api/classes/list

# 通过网关访问成绩服务
curl http://localhost:8080/api/scores/list
```

## 🔧 功能特性

### 1. CORS 跨域处理
- 自动处理跨域请求
- 支持前端开发环境 (localhost:*)
- 支持所有 HTTP 方法
- 支持携带认证信息

### 2. 统一异常处理
- 服务不可用异常
- 连接超时异常
- 目标服务异常
- 返回标准 JSON 格式

### 3. 请求日志记录
- 记录请求开始时间
- 记录响应状态码
- 记录请求处理时长
- 记录客户端地址

## 📊 监控端点

访问 `http://localhost:8080/actuator` 查看可用的监控端点：

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息  
- `/actuator/gateway/routes` - 路由信息

## 🏃‍♂️ 启动方式

### 开发环境
```bash
# 进入项目根目录
cd gateway-service

# 使用 Maven 启动
mvn spring-boot:run

# 或者使用 IDE 直接运行 GatewayApplication.java
```

### 生产环境
```bash
# 构建 JAR 包
mvn clean package

# 运行 JAR 包
java -jar target/gateway-service-1.0.0.jar
```

## ⚙️ 配置说明

主要配置文件：`src/main/resources/application.yml`

```yaml
# 服务端口
server:
  port: 8080

# Spring Cloud Gateway 配置
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/users/**
```

## 🔍 日志查看

启动后可以通过日志查看请求信息：

```
2024-01-01 10:00:00 [reactor-http-nio-2] INFO  c.s.gateway.filter.LoggingFilter - 🚀 请求开始 - Method: GET, URI: http://localhost:8080/api/users/list, RemoteAddress: /127.0.0.1:12345
2024-01-01 10:00:01 [reactor-http-nio-2] INFO  c.s.gateway.filter.LoggingFilter - ✅ 请求完成 - URI: http://localhost:8080/api/users/list, Status: 200 OK, Duration: 156ms
```

## 🚧 注意事项

1. **服务依赖**: Gateway 启动前需要确保目标服务正常运行
2. **端口冲突**: 确保 8080 端口未被占用
3. **路由配置**: 修改路由配置后需要重启服务
4. **跨域设置**: 生产环境建议配置具体的允许域名

## 🛠️ 故障排查

### 常见问题

1. **503 Service Unavailable**
   - 检查目标服务是否启动
   - 检查目标服务端口是否正确

2. **CORS 错误**
   - 检查前端域名是否在允许列表中
   - 检查 CORS 配置是否正确

3. **路由不生效**
   - 检查路径匹配规则
   - 查看 Gateway 日志信息