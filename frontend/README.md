# 学生信息管理系统 - 前端组件

## 概述

本项目包含两个核心 Vue 组件，用于学生信息管理系统的成绩查看和班级管理功能。

## 技术栈

- **Vue 3** - 使用 Composition API
- **Element Plus** - UI 组件库
- **Axios** - HTTP 请求库
- **Vite** - 构建工具

## 组件说明

### 1. ScoreView.vue - 成绩查看页

#### 功能特性

- **📊 成绩展示**: 表格形式展示学生成绩信息
- **🔍 多维度筛选**: 支持按学生姓名、学号、科目、学期筛选
- **📈 统计分析**: 显示总成绩条数、平均成绩、及格率、优秀率
- **🎨 可视化标识**: 
  - 成绩等级颜色标识（A/B/C/D/F）
  - 考试类型标签（期中/期末/小测验/作业）
  - 及格状态图标显示
- **📄 分页功能**: 支持分页浏览和每页条数设置
- **💾 导出功能**: 支持成绩数据导出
- **🔄 实时更新**: 自动计算统计数据

#### 数据结构

```javascript
// 成绩数据结构
{
  id: Number,              // 成绩ID
  studentName: String,     // 学生姓名
  studentNumber: String,   // 学号
  className: String,       // 班级名称
  subject: String,         // 科目
  examType: String,        // 考试类型
  score: Number,           // 成绩
  grade: String,           // 等级
  isPassed: Boolean,       // 是否及格
  semester: String,        // 学期
  academicYear: String,    // 学年
  teacher: String,         // 任课教师
  examDate: String,        // 考试日期
  remarks: String          // 备注
}
```

#### API 接口

- `GET /api/scores` - 获取成绩列表
- `GET /api/scores/subjects` - 获取科目列表
- `GET /api/scores/semesters` - 获取学期列表

### 2. ClassList.vue - 班级列表页

#### 功能特性

- **🏫 班级管理**: 展示班级列表，支持新增、编辑、删除
- **🔍 智能搜索**: 按班级名称、年级、状态筛选
- **📊 统计面板**: 显示总班级数、可用班级、已满班级、总学生数
- **📈 容量管理**: 
  - 进度条显示班级容量使用情况
  - 颜色标识不同容量状态
  - 防止超员保护
- **📝 表单验证**: 新增/编辑班级时的完整表单验证
- **🔒 安全删除**: 班级内有学生时禁止删除
- **📋 详情查看**: 弹窗显示班级详细信息
- **🔗 页面联动**: 班级更新时通知其他组件

#### 功能操作

1. **新增班级**
   - 点击"新增班级"按钮打开表单
   - 填写班级名称、年级、容量等信息
   - 表单验证通过后创建班级

2. **编辑班级**
   - 点击表格中的"编辑"按钮
   - 修改班级信息并保存

3. **删除班级**
   - 仅当班级内无学生时可删除
   - 删除前需要确认操作

4. **查看详情**
   - 点击表格行或"查看详情"按钮
   - 弹窗显示班级完整信息

#### 数据结构

```javascript
// 班级数据结构
{
  id: Number,              // 班级ID
  className: String,       // 班级名称
  grade: String,           // 年级
  capacity: Number,        // 班级容量
  currentCount: Number,    // 当前人数
  createdAt: String,       // 创建时间
  updatedAt: String,       // 更新时间
  remarks: String          // 备注
}
```

#### API 接口

- `GET /api/classes` - 获取班级列表
- `POST /api/classes` - 创建新班级
- `PUT /api/classes/:id` - 更新班级信息
- `DELETE /api/classes/:id` - 删除班级

## 页面联动功能

### 组件间通信

1. **班级列表更新事件**: 
   ```javascript
   // ClassList.vue 创建班级后触发
   window.dispatchEvent(new CustomEvent('classListUpdated'))
   ```

2. **路由跳转**: 
   ```javascript
   // 从成绩页跳转到学生管理页
   // 从班级详情跳转到学生列表页
   ```

### 数据联动

- **学生添加**: 学生表单中的班级下拉框数据来自班级服务
- **成绩查看**: 点击学生的"查看成绩"按钮跳转到成绩页面
- **统计联动**: 仪表盘统计卡片点击跳转到相应详情页

## 样式特性

### 设计风格

- **现代化 UI**: 基于 Element Plus 的现代化设计
- **响应式布局**: 适配不同屏幕尺寸
- **交互反馈**: 
  - 鼠标悬停效果
  - 加载状态提示
  - 操作结果反馈

### 颜色标识

- **成绩等级**: A(绿色) B(蓝色) C(橙色) D(灰色) F(红色)
- **班级状态**: 可用(绿色) 已满(橙色)
- **容量进度**: 绿色(正常) 橙色(接近满员) 红色(已满)

## 使用说明

### 环境要求

- Node.js 16.0+
- Vue 3.0+
- Element Plus 2.0+

### 安装依赖

```bash
cd frontend
npm install
```

### 开发模式

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

## 组件使用示例

### 1. 在路由中使用

```javascript
// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import ScoreView from '@/views/ScoreView.vue'
import ClassList from '@/views/ClassList.vue'

const routes = [
  {
    path: '/scores',
    name: 'ScoreView',
    component: ScoreView
  },
  {
    path: '/classes',
    name: 'ClassList', 
    component: ClassList
  }
]
```

### 2. 作为组件导入

```javascript
// 在其他Vue文件中使用
import ScoreView from '@/views/ScoreView.vue'
import ClassList from '@/views/ClassList.vue'

export default {
  components: {
    ScoreView,
    ClassList
  }
}
```

## 注意事项

1. **API 接口**: 组件中的 API 调用需要配合后端服务
2. **路由配置**: 页面跳转功能需要配置 Vue Router
3. **权限控制**: 可根据需要添加用户权限验证
4. **数据格式**: 确保后端返回的数据格式与组件期望一致

## 扩展功能

### 可添加的功能

- **批量操作**: 批量删除、批量导入等
- **高级筛选**: 更多筛选条件和排序方式
- **数据导出**: Excel、PDF 等格式导出
- **打印功能**: 成绩单、班级名册打印
- **数据可视化**: 图表显示统计数据

### 性能优化

- **虚拟滚动**: 大数据量时的性能优化
- **懒加载**: 按需加载数据
- **缓存策略**: 合理使用数据缓存

## 技术支持

如有问题或建议，请参考以下资源：

- [Vue 3 官方文档](https://v3.vuejs.org/)
- [Element Plus 官方文档](https://element-plus.org/)
- [Axios 官方文档](https://axios-http.com/)

## 更新日志

### v1.0.0 (2024-06-11)
- ✅ 完成 ScoreView.vue 成绩查看页
- ✅ 完成 ClassList.vue 班级列表页
- ✅ 实现基础的增删改查功能
- ✅ 添加数据联动和页面跳转功能
- ✅ 完善UI设计和交互体验