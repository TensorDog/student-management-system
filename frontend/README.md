# 学生信息管理系统 - 前端

基于 Vue 3 + Vue Router 的学生信息管理系统前端应用。

## 🎯 项目概述

本前端应用包含4个核心页面组件，提供完整的学生信息管理功能：

- **仪表盘 (Dashboard)** - 系统统计概览和图表展示
- **学生管理 (StudentManagement)** - 学生信息的增删改查
- **班级管理 (ClassList)** - 班级信息管理和展示
- **成绩查看 (ScoreView)** - 学生成绩展示和筛选

## 📁 项目结构

```
frontend/
├── public/
│   └── index.html              # HTML模板
├── src/
│   ├── views/                  # 页面组件
│   │   ├── Dashboard.vue       # 仪表盘页面
│   │   ├── StudentManagement.vue # 学生管理页面
│   │   ├── ClassList.vue       # 班级管理页面
│   │   └── ScoreView.vue       # 成绩查看页面
│   ├── router/
│   │   └── index.js            # 路由配置
│   ├── services/
│   │   └── api.js              # API服务封装
│   ├── components/             # 公共组件 (待扩展)
│   ├── App.vue                 # 根组件
│   └── main.js                 # 应用入口
├── package.json                # 项目配置
└── README.md                   # 项目说明
```

## 🚀 快速开始

### 安装依赖
```bash
cd frontend
npm install
```

### 开发环境运行
```bash
npm run serve
# 或
npm run dev
```

### 生产环境构建
```bash
npm run build
```

## 🛠 技术栈

- **Vue 3** - 前端框架，使用 Composition API
- **Vue Router 4** - 路由管理
- **Axios** - HTTP客户端
- **CSS Grid & Flexbox** - 响应式布局

## 📱 页面路由

| 路径 | 组件 | 功能描述 |
|------|------|----------|
| `/dashboard` | Dashboard | 系统仪表盘，显示统计数据和图表 |
| `/students` | StudentManagement | 学生信息管理，支持增删改查和分页 |
| `/classes` | ClassList | 班级信息管理，卡片式展示 |
| `/scores` | ScoreView | 成绩查看，支持多维度筛选 |

## 🔌 API 接口

前端通过封装的 API 服务调用后端微服务：

### 用户服务 (user-service)
- `GET /user-service/api/users` - 获取学生列表
- `POST /user-service/api/users` - 创建学生
- `PUT /user-service/api/users/{id}` - 更新学生信息
- `DELETE /user-service/api/users/{id}` - 删除学生

### 班级服务 (class-service)
- `GET /class-service/api/classes` - 获取班级列表
- `POST /class-service/api/classes` - 创建班级
- `PUT /class-service/api/classes/{id}` - 更新班级信息
- `DELETE /class-service/api/classes/{id}` - 删除班级

### 成绩服务 (score-service)
- `GET /score-service/api/scores` - 获取成绩列表
- `GET /score-service/api/scores/average` - 获取平均分
- `GET /score-service/api/subjects` - 获取课程列表

## 🎨 UI特性

- **响应式设计** - 适配桌面端和移动端
- **现代化界面** - 卡片式布局和柔和色彩
- **交互友好** - 悬停效果和平滑动画
- **数据可视化** - 统计卡片和图表占位
- **操作反馈** - 加载状态和错误处理

## 🔧 开发说明

### 环境变量配置
创建 `.env` 文件配置后端API地址：
```
VUE_APP_API_BASE_URL=http://localhost:8080
```

### 组件开发规范
- 使用 Vue 3 Composition API
- 组件命名采用 PascalCase
- 样式使用 scoped 避免冲突
- 统一使用 async/await 处理异步操作

### API 调用示例
```javascript
import { userAPI } from '@/services/api'

// 获取学生列表
const students = await userAPI.getUsers({ page: 1, size: 10 })

// 创建学生
const newStudent = await userAPI.createUser({
  name: '张三',
  studentId: '20230001',
  classId: 1
})
```

## 🔄 与后端集成

前端设计为与微服务架构的后端集成：

1. **用户服务** - 管理学生和教师信息
2. **班级服务** - 管理班级信息和关联关系  
3. **成绩服务** - 管理成绩数据和统计

所有接口调用都经过统一的 axios 拦截器处理，支持：
- 请求/响应日志
- 错误统一处理
- 自动添加认证头
- 超时处理

## 📈 后续扩展

- [ ] 添加用户认证和权限管理
- [ ] 集成图表库 (ECharts/Chart.js)
- [ ] 添加导入导出功能
- [ ] 支持多语言国际化
- [ ] 添加PWA支持
- [ ] 优化移动端体验