import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import StudentManagement from '@/views/StudentManagement.vue'
import ClassList from '@/views/ClassList.vue'
import ScoreView from '@/views/ScoreView.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: {
      title: '系统仪表盘',
      icon: '📊'
    }
  },
  {
    path: '/students',
    name: 'StudentManagement',
    component: StudentManagement,
    meta: {
      title: '学生管理',
      icon: '👥'
    }
  },
  {
    path: '/classes',
    name: 'ClassList',
    component: ClassList,
    meta: {
      title: '班级管理',
      icon: '🏫'
    }
  },
  {
    path: '/scores',
    name: 'ScoreView',
    component: ScoreView,
    meta: {
      title: '成绩查看',
      icon: '📈'
    }
  },
  // 可以添加更多路由，如：
  // {
  //   path: '/students/:id',
  //   name: 'StudentDetail',
  //   component: () => import('@/views/StudentDetail.vue'),
  //   props: true
  // },
  // {
  //   path: '/classes/:id',
  //   name: 'ClassDetail',
  //   component: () => import('@/views/ClassDetail.vue'),
  //   props: true
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // 路由切换时回到顶部
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 学生信息管理系统`
  } else {
    document.title = '学生信息管理系统'
  }
  
  next()
})

export default router