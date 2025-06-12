# Score Service - 成绩管理服务

## 概述
成绩管理微服务，提供学生成绩的增删改查功能，支持按学生ID、班级ID、科目等多种维度查询成绩。

## 服务信息
- **服务名称**: score-service
- **端口**: 8083
- **数据库**: score_db
- **API前缀**: /api/scores

## 功能特性
- ✅ 按学生ID查询成绩
- ✅ 按学生ID和科目查询成绩
- ✅ 按班级ID查询成绩
- ✅ 按科目查询成绩
- ✅ 按学期查询成绩
- ✅ 分页查询成绩
- ✅ 成绩统计分析
- ✅ 成绩的增删改查
- ✅ 自动计算成绩等级和及格状态
- ✅ 注册到Nacos配置中心
- ✅ 支持Gateway路由

## API接口

### 查询接口
| 方法 | 路径 | 描述 |
|-----|------|------|
| GET | `/api/scores/student/{studentId}` | 根据学生ID查询成绩 |
| GET | `/api/scores/student/{studentId}/subject/{subject}` | 根据学生ID和科目查询成绩 |
| GET | `/api/scores/class/{classId}` | 根据班级ID查询成绩 |
| GET | `/api/scores/subject/{subject}` | 根据科目查询成绩 |
| GET | `/api/scores/semester/{semester}` | 根据学期查询成绩 |
| GET | `/api/scores/{id}` | 根据ID查询单个成绩 |
| GET | `/api/scores/page` | 分页查询成绩 |
| GET | `/api/scores/student/{studentId}/statistics` | 获取学生成绩统计 |

### 管理接口
| 方法 | 路径 | 描述 |
|-----|------|------|
| POST | `/api/scores` | 添加成绩 |
| PUT | `/api/scores/{id}` | 更新成绩 |
| DELETE | `/api/scores/{id}` | 删除成绩 |

## 分页查询参数
- `page`: 页码，默认1
- `size`: 每页大小，默认10
- `studentId`: 学生ID（可选）
- `classId`: 班级ID（可选）
- `subject`: 科目（可选）
- `semester`: 学期（可选）

## 成绩实体字段
```java
{
  "id": "成绩ID",
  "studentId": "学生ID",
  "studentName": "学生姓名",
  "studentNumber": "学号",
  "classId": "班级ID",
  "className": "班级名称",
  "subject": "科目名称",
  "subjectCode": "科目代码",
  "examType": "考试类型(MIDTERM/FINAL/QUIZ/ASSIGNMENT)",
  "score": "成绩分数",
  "totalScore": "总分",
  "grade": "等级(A/B/C/D/F)",
  "isPassed": "是否及格",
  "semester": "学期",
  "academicYear": "学年",
  "examDate": "考试日期",
  "createTime": "录入时间",
  "updateTime": "更新时间",
  "teacher": "任课教师",
  "remarks": "备注"
}
```

## 数据库设计
数据库名：`score_db`
主表：`scores`

包含完整的索引设计以支持高效查询：
- 学生ID索引
- 班级ID索引
- 科目索引
- 学期索引
- 考试日期索引
- 复合索引（学生+科目，班级+科目）

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/score_db
    username: root
    password: Abcd1234
```

### Nacos配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        metadata:
          context-path: /api/scores
      config:
        server-addr: localhost:8848
```

## 启动步骤

1. **初始化数据库**
   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   ```

2. **启动Nacos（如果使用）**
   ```bash
   # 下载并启动Nacos Server
   ./nacos/bin/startup.sh -m standalone
   ```

3. **启动服务**
   ```bash
   mvn spring-boot:run
   ```

4. **验证服务**
   - 访问健康检查：http://localhost:8083/actuator/health
   - 测试API：http://localhost:8083/api/scores/student/1

## 示例请求

### 查询学生成绩
```bash
GET http://localhost:8083/api/scores/student/1
```

### 分页查询
```bash
GET http://localhost:8083/api/scores/page?page=1&size=10&studentId=1
```

### 添加成绩
```bash
POST http://localhost:8083/api/scores
Content-Type: application/json

{
  "studentId": 1,
  "studentName": "张三",
  "studentNumber": "2024001",
  "classId": 1,
  "className": "计算机科学与技术1班",
  "subject": "算法设计",
  "subjectCode": "CS102",
  "examType": "FINAL",
  "score": 88.5,
  "semester": "2024-1",
  "academicYear": "2023-2024",
  "examDate": "2024-06-15T09:00:00",
  "teacher": "李老师"
}
```

### 获取成绩统计
```bash
GET http://localhost:8083/api/scores/student/1/statistics
```

## Gateway路由配置
确保在Gateway中配置路由规则：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: score-service
          uri: lb://score-service
          predicates:
            - Path=/api/scores/**
```

## 注意事项
- 成绩录入时会自动计算等级和及格状态
- 支持多种考试类型：期中(MIDTERM)、期末(FINAL)、小测验(QUIZ)、作业(ASSIGNMENT)
- 所有查询接口都按考试日期倒序排列
- 删除操作请谨慎使用，建议实现软删除