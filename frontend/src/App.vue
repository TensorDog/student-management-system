<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  Fold, 
  Expand, 
  HomeFilled, 
  User, 
  School, 
  TrendCharts,
  CaretBottom
} from '@element-plus/icons-vue'
</script>

<template>
  <div class="app-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="sidebarCollapsed ? '64px' : '200px'" class="sidebar">
        <div class="logo-container">
          <div class="logo">
            <el-icon><School /></el-icon>
            <span v-show="!sidebarCollapsed" class="logo-text">学生管理系统</span>
          </div>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          :collapse="sidebarCollapsed"
          :collapse-transition="false"
          router
          unique-opened
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <template #title>仪表板</template>
          </el-menu-item>
          
          <el-sub-menu index="student">
            <template #title>
              <el-icon><User /></el-icon>
              <span>学生管理</span>
            </template>
            <el-menu-item index="/student/list">学生列表</el-menu-item>
            <el-menu-item index="/student/add">添加学生</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="class">
            <template #title>
              <el-icon><School /></el-icon>
              <span>班级管理</span>
            </template>
            <el-menu-item index="/class/list">班级列表</el-menu-item>
            <el-menu-item index="/class/add">添加班级</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="score">
            <template #title>
              <el-icon><TrendCharts /></el-icon>
              <span>成绩管理</span>
            </template>
            <el-menu-item index="/score/list">成绩列表</el-menu-item>
            <el-menu-item index="/score/import">成绩录入</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container>
        <!-- 顶部栏 -->
        <el-header class="header">
          <div class="header-left">
            <el-button 
              :icon="sidebarCollapsed ? Expand : Fold" 
              @click="toggleSidebar"
              text
              size="large"
            />
            <span class="current-page">{{ currentPageTitle }}</span>
          </div>
          
          <div class="header-right">
            <el-dropdown>
              <div class="user-dropdown">
                <el-avatar :size="32" :src="userAvatar">
                  <el-icon><User /></el-icon>
                </el-avatar>
                <span class="username">管理员</span>
                <el-icon><CaretBottom /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>修改密码</el-dropdown-item>
                  <el-dropdown-item divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主内容区 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  Fold, 
  Expand, 
  HomeFilled, 
  User, 
  School, 
  TrendCharts,
  CaretBottom
} from '@element-plus/icons-vue'

const route = useRoute()

// 侧边栏状态
const sidebarCollapsed = ref(false)

// 用户头像（这里使用默认头像）
const userAvatar = ref('')

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 当前页面标题
const currentPageTitle = computed(() => {
  const title = route.meta?.title
  return title || '学生信息管理系统'
})

// 切换侧边栏折叠状态
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}
</script>

<style scoped>
.app-container {
  height: 100vh;
  background-color: #f0f2f5;
}

.el-container {
  height: 100%;
}

/* 侧边栏样式 */
.sidebar {
  background-color: #001529;
  transition: width 0.3s;
  overflow: hidden;
}

.logo-container {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #1f2937;
}

.logo {
  display: flex;
  align-items: center;
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.logo .el-icon {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  white-space: nowrap;
}

.sidebar-menu {
  border: none;
  background-color: #001529;
}

.sidebar-menu .el-menu-item,
.sidebar-menu .el-sub-menu .el-sub-menu__title {
  color: rgba(255, 255, 255, 0.65);
  border-bottom: none;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-sub-menu .el-sub-menu__title:hover {
  background-color: #1890ff;
  color: white;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #1890ff;
  color: white;
}

/* 顶部栏样式 */
.header {
  background-color: white;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.current-page {
  margin-left: 16px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f5f5;
}

.username {
  margin: 0 8px;
  color: #303133;
  font-size: 14px;
}

/* 主内容区样式 */
.main-content {
  background-color: #f0f2f5;
  padding: 0;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
    height: 100vh;
  }
  
  .logo-text {
    display: none;
  }
}
</style>
