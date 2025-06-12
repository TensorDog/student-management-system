# Config Service - 配置中心服务

配置中心服务是学生信息管理系统的核心基础设施服务，负责统一管理所有微服务的配置信息。

## 🎯 功能特点

### 1. 统一配置管理
- 集中管理所有微服务的配置文件
- 支持不同环境（dev、test、prod）的配置隔离
- 提供配置文件版本控制和回滚功能

### 2. 动态配置刷新
- 支持配置热更新，无需重启服务
- 集成 Spring Cloud Bus 实现配置批量推送
- 提供 `/actuator/refresh` 端点手动刷新配置

### 3. 多种配置源支持
- **本地文件模式**：适用于开发和测试环境
- **Git 仓库模式**：适用于生产环境，支持版本控制
- **Nacos 模式**：与 Nacos 配置中心无缝集成

### 4. 高可用保障
- 支持配置缓存，服务启动时优先使用本地缓存
- 提供健康检查和监控端点
- 集成 Nacos 注册中心，支持服务发现

## 🚀 快速开始

### 1. 环境准备

确保以下服务已启动：
```bash
# 启动 Nacos Server
docker run -d --name nacos \
  -p 8848:8848 \
  -e MODE=standalone \
  nacos/nacos-server:v2.2.3
```

### 2. 启动配置中心服务

```bash
# 进入项目根目录
cd student-management-system

# 编译项目
mvn clean package -DskipTests

# 启动 Config Service
cd config-service
mvn spring-boot:run
```

### 3. 验证服务状态

访问以下端点验证服务是否正常运行：

- **服务健康检查**：http://localhost:8888/actuator/health
- **配置信息获取**：http://localhost:8888/user-service/default
- **Nacos 控制台**：http://localhost:8848/nacos（nacos/nacos）

## 📁 配置文件管理

### 目录结构
```
config-service/
├── src/main/resources/
│   ├── application.yml          # 配置中心自身配置
│   └── configs/                 # 微服务配置文件目录
│       ├── user-service.yml     # 用户服务配置
│       ├── class-service.yml    # 班级服务配置
│       └── gateway-service.yml  # 网关服务配置
```

### 配置文件命名规则
- **默认配置**：`{服务名}.yml`
- **环境配置**：`{服务名}-{环境}.yml`
- **示例**：
  - `user-service.yml`（默认配置）
  - `user-service-dev.yml`（开发环境）
  - `user-service-prod.yml`（生产环境）

### 配置获取接口

客户端可通过以下 REST API 获取配置：

```
GET /{服务名}/{环境}/{分支}
GET /{服务名}/{环境}
GET /{服务名}/default
```

**示例**：
```bash
# 获取用户服务默认配置
curl http://localhost:8888/user-service/default

# 获取用户服务开发环境配置
curl http://localhost:8888/user-service/dev
```

## 🔧 配置说明

### 核心配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 服务端口 | 8888 |
| `spring.cloud.config.server.native.search-locations` | 本地配置文件路径 | classpath:/configs/ |
| `spring.cloud.nacos.discovery.server-addr` | Nacos 注册中心地址 | localhost:8848 |
| `spring.cloud.nacos.config.server-addr` | Nacos 配置中心地址 | localhost:8848 |

### 客户端配置

其他微服务需要在 `bootstrap.yml` 中添加以下配置：

```yaml
spring:
  application:
    name: user-service  # 服务名称
  cloud:
    config:
      discovery:
        enabled: true
        service-id: config-service
      name: ${spring.application.name}
      profile: default
    nacos:
      discovery:
        server-addr: localhost:8848
```

## 🔄 配置刷新机制

### 1. 手动刷新
```bash
# 刷新指定服务配置
curl -X POST http://localhost:8080/actuator/refresh
```

### 2. 自动刷新
配置文件修改后，支持以下自动刷新方式：
- **Nacos 推送**：修改 Nacos 控制台配置后自动推送
- **Git Webhook**：Git 仓库变更触发配置刷新
- **定时轮询**：定期检查配置变更

## 📊 监控与管理

### 管理端点

| 端点 | 功能 | 访问地址 |
|------|------|----------|
| `/actuator/health` | 健康检查 | http://localhost:8888/actuator/health |
| `/actuator/info` | 服务信息 | http://localhost:8888/actuator/info |
| `/actuator/env` | 环境变量 | http://localhost:8888/actuator/env |
| `/actuator/refresh` | 刷新配置 | http://localhost:8888/actuator/refresh |

### 日志监控

配置中心提供详细的日志输出：
```bash
# 查看配置加载日志
tail -f logs/config-service.log | grep "Config"

# 查看客户端请求日志
tail -f logs/config-service.log | grep "Request"
```

## 🚨 故障排查

### 常见问题

1. **服务启动失败**
   - 检查 Nacos 服务是否启动
   - 验证端口 8888 是否被占用
   - 确认 Java 版本为 17+

2. **配置获取失败**
   - 检查配置文件路径是否正确
   - 验证服务名称拼写是否正确
   - 确认 Nacos 注册状态

3. **配置刷新不生效**
   - 检查客户端是否添加 `@RefreshScope` 注解
   - 验证 `/actuator/refresh` 端点是否可访问
   - 确认配置中心与客户端网络连通性

### 调试模式

启用调试日志：
```yaml
logging:
  level:
    org.springframework.cloud.config: DEBUG
    com.student.config: DEBUG
```

## 🌐 部署建议

### 开发环境
- 使用本地文件模式
- 单机部署，端口 8888

### 生产环境
- 使用 Git 仓库模式
- 集群部署，负载均衡
- 配置 HTTPS 和认证
- 启用配置加密

### Docker 部署
```dockerfile
FROM openjdk:17-jre-slim
COPY target/config-service-1.0.0.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# 构建镜像
docker build -t student-config-service .

# 启动容器
docker run -d --name config-service \
  -p 8888:8888 \
  -e NACOS_ADDR=nacos:8848 \
  student-config-service
```

## 📝 更新日志

- **v1.0.0** (2024-06-11)
  - 初始版本发布
  - 支持本地文件和 Nacos 配置源
  - 集成服务注册与发现
  - 提供配置管理 REST API

---

## 💡 技术支持

如有问题，请查看：
1. [Spring Cloud Config 官方文档](https://spring.io/projects/spring-cloud-config)
2. [Nacos 配置中心文档](https://nacos.io/zh-cn/docs/quick-start.html)
3. 项目 Issue 跟踪系统