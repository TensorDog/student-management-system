import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 在请求发送前执行的逻辑
    console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`)
    
    // 如果有token，添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  (error) => {
    console.error('❌ Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    console.log(`✅ API Response: ${response.config.method?.toUpperCase()} ${response.config.url}`, response.data)
    return response
  },
  (error) => {
    console.error('❌ Response Error:', error)
    
    // 统一错误处理
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          localStorage.removeItem('token')
          // router.push('/login') // 需要导入router
          console.warn('用户未授权，请重新登录')
          break
        case 403:
          console.warn('访问被拒绝')
          break
        case 404:
          console.warn('请求的资源不存在')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error(`请求失败: ${status}`, data?.message || error.message)
      }
    } else if (error.request) {
      console.error('网络连接错误，请检查网络设置')
    } else {
      console.error('请求配置错误:', error.message)
    }
    
    return Promise.reject(error)
  }
)

// 导出封装的API方法
export { api }

// 导出具体的API调用函数
export const userAPI = {
  // 用户相关API
  getUsers: (params) => api.get('/user-service/api/users', { params }),
  getUserById: (id) => api.get(`/user-service/api/users/${id}`),
  createUser: (data) => api.post('/user-service/api/users', data),
  updateUser: (id, data) => api.put(`/user-service/api/users/${id}`, data),
  deleteUser: (id) => api.delete(`/user-service/api/users/${id}`),
  getUserCount: () => api.get('/user-service/api/users/count'),
  
  // 学生特定API
  getStudentsByClass: (classId) => api.get(`/user-service/api/users/class/${classId}`),
  searchStudents: (keyword) => api.get('/user-service/api/users/search', { params: { keyword } })
}

export const classAPI = {
  // 班级相关API
  getClasses: () => api.get('/class-service/api/classes'),
  getClassById: (id) => api.get(`/class-service/api/classes/${id}`),
  createClass: (data) => api.post('/class-service/api/classes', data),
  updateClass: (id, data) => api.put(`/class-service/api/classes/${id}`, data),
  deleteClass: (id) => api.delete(`/class-service/api/classes/${id}`),
  getClassCount: () => api.get('/class-service/api/classes/count'),
  
  // 班级统计API
  getClassStudentCount: (classId) => api.get(`/class-service/api/classes/${classId}/student-count`)
}

export const scoreAPI = {
  // 成绩相关API
  getScores: (params) => api.get('/score-service/api/scores', { params }),
  getScoreById: (id) => api.get(`/score-service/api/scores/${id}`),
  createScore: (data) => api.post('/score-service/api/scores', data),
  updateScore: (id, data) => api.put(`/score-service/api/scores/${id}`, data),
  deleteScore: (id) => api.delete(`/score-service/api/scores/${id}`),
  
  // 成绩统计API
  getAverageScore: () => api.get('/score-service/api/scores/average'),
  getScoresByStudent: (studentId) => api.get(`/score-service/api/scores/student/${studentId}`),
  getScoresByClass: (classId) => api.get(`/score-service/api/scores/class/${classId}`),
  getSubjects: () => api.get('/score-service/api/subjects'),
  
  // 成绩统计分析
  getScoreStatistics: (params) => api.get('/score-service/api/scores/statistics', { params })
}

// 导出默认实例
export default api