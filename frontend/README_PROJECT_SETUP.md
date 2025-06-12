# Vue3 + Vite 学生信息管理系统前端

## 项目概述
这是一个基于 Vue3 + Vite 构建的学生信息管理系统前端项目，集成了 Element Plus UI 组件库、ECharts 图表库和 Axios 请求库。

## 已完成的配置

### 1. 基础项目结构
- ✅ Vue3 + Vite 项目初始化
- ✅ TypeScript 支持
- ✅ Vue Router 路由配置
- ✅ 现代化的项目结构

### 2. 依赖包安装
```json
{
  "dependencies": {
    "element-plus": "^2.10.1",
    "axios": "^1.9.0", 
    "echarts": "^5.6.0",
    "@element-plus/icons-vue": "^2.3.1",
    "vue": "^3.5.13",
    "vue-router": "^4.5.0"
  }
}
```

### 3. Vite 配置 (vite.config.ts)
- ✅ 路径别名配置 (`@` 指向 `src` 目录)
- ✅ 跨域代理配置：
  - `/api/user` → `http://localhost:8081`
  - `/api/class` → `http://localhost:8082`
  - `/api/score` → `http://localhost:8083`

### 4. 主入口配置 (main.ts)
- ✅ Element Plus 全局注册
- ✅ Element Plus 样式导入
- ✅ 所有图标组件全局注册
- ✅ 路由和状态管理配置

### 5. Axios 请求封装 (utils/request.js)
- ✅ 统一的请求拦截器（自动添加 token）
- ✅ 统一的响应拦截器（错误处理和消息提示）
- ✅ 完善的错误状态码处理
- ✅ 与 Element Plus 消息组件集成

### 6. API 接口层 (api/dashboard.js)
- ✅ 模块化的 API 接口管理
- ✅ 模拟数据支持（可轻松切换到真实接口）
- ✅ 包含以下接口：
  - `getStatistics()` - 获取统计数据
  - `getScoreDistribution()` - 获取成绩分布数据
  - `getClassScoreTrend()` - 获取班级成绩趋势数据

### 7. 仪表板页面 (views/Dashboard.vue)
- ✅ 响应式统计卡片设计：
  - 学生总数卡片
  - 班级总数卡片
  - 平均成绩卡片
- ✅ ECharts 图表集成：
  - 成绩分布饼图
  - 班级成绩趋势柱状图
- ✅ 现代化 UI 设计
- ✅ 响应式布局
- ✅ 图表自适应窗口大小

### 8. 路由配置 (router/index.ts)
- ✅ `/dashboard` 路由配置
- ✅ 根路径自动重定向到仪表板
- ✅ 路由元信息配置（页面标题等）

### 9. 应用布局 (App.vue)
- ✅ 现代化管理后台布局：
  - 可折叠侧边栏导航
  - 顶部工具栏
  - 主内容区域
- ✅ 导航菜单结构：
  - 仪表板
  - 学生管理（学生列表、添加学生）
  - 班级管理（班级列表、添加班级）
  - 成绩管理（成绩列表、成绩录入）
- ✅ 用户界面元素：
  - 用户头像和下拉菜单
  - 页面标题显示
  - 侧边栏折叠切换
- ✅ 响应式设计支持

## 项目特色

### 🎨 现代化 UI 设计
- 基于 Element Plus 的专业级组件库
- 精美的渐变色卡片设计
- 优雅的深色侧边栏
- 响应式布局适配

### 📊 数据可视化
- ECharts 专业图表库集成
- 饼图展示成绩分布
- 柱状图展示班级趋势
- 图表自适应和交互支持

### 🚀 开发体验
- Vite 快速构建和热重载
- TypeScript 类型安全
- 模块化代码组织
- 完善的错误处理机制

### 🔧 生产就绪
- 统一的请求拦截和响应处理
- 跨域代理配置
- 可扩展的 API 层设计
- 易于部署的配置

## 启动项目

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 目录结构

```
frontend/
├── src/
│   ├── api/              # API 接口层
│   │   └── dashboard.js  # 仪表板相关接口
│   ├── components/       # 公共组件
│   ├── router/           # 路由配置
│   │   └── index.ts      # 主路由文件
│   ├── utils/            # 工具函数
│   │   └── request.js    # Axios 封装
│   ├── views/            # 页面组件
│   │   └── Dashboard.vue # 仪表板页面
│   ├── App.vue           # 根组件
│   └── main.ts           # 应用入口
├── vite.config.ts        # Vite 配置
├── package.json          # 依赖配置
└── README_PROJECT_SETUP.md
```

## 后续开发建议

1. **添加更多页面**：根据路由配置添加学生管理、班级管理、成绩管理页面
2. **状态管理**：如需复杂状态管理，可考虑集成 Pinia
3. **权限控制**：添加路由守卫和权限验证
4. **国际化**：集成 Vue I18n 支持多语言
5. **测试**：添加单元测试和 E2E 测试
6. **部署**：配置 Docker 或静态文件服务器部署

## 技术栈版本

- Vue: 3.5.13
- Vite: 6.2.4
- Element Plus: 2.10.1
- ECharts: 5.6.0
- Axios: 1.9.0
- Vue Router: 4.5.0

---

*项目配置完成，可以开始业务开发！* 🎉