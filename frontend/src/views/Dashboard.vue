<template>
  <div class="dashboard">
    <h1 class="page-title">系统仪表盘</h1>
    
    <!-- 统计卡片区域 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-content">
          <h3>学生总数</h3>
          <p class="stat-number">{{ studentCount }}</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">🏫</div>
        <div class="stat-content">
          <h3>班级总数</h3>
          <p class="stat-number">{{ classCount }}</p>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">📊</div>
        <div class="stat-content">
          <h3>平均成绩</h3>
          <p class="stat-number">{{ averageScore.toFixed(1) }}</p>
        </div>
      </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-section">
      <div class="chart-container">
        <h3>成绩分布图</h3>
        <div class="chart-placeholder">
          <!-- 图表组件占位 -->
          <p>图表组件占位 - 可集成 ECharts 或 Chart.js</p>
        </div>
      </div>
      
      <div class="chart-container">
        <h3>班级人数统计</h3>
        <div class="chart-placeholder">
          <!-- 图表组件占位 -->
          <p>图表组件占位 - 可集成 ECharts 或 Chart.js</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '@/services/api'

// 响应式数据
const studentCount = ref(0)
const classCount = ref(0)
const averageScore = ref(0)

// 获取统计数据
const loadStatistics = async () => {
  try {
    // 获取学生数量
    const studentsResponse = await api.get('/user-service/api/users/count')
    studentCount.value = studentsResponse.data || 0
    
    // 获取班级数量
    const classesResponse = await api.get('/class-service/api/classes/count')
    classCount.value = classesResponse.data || 0
    
    // 获取平均成绩
    const scoresResponse = await api.get('/score-service/api/scores/average')
    averageScore.value = scoresResponse.data || 0
    
  } catch (error) {
    console.error('加载统计数据失败:', error)
    // 使用模拟数据
    studentCount.value = 156
    classCount.value = 8
    averageScore.value = 85.6
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  color: #2c3e50;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
}

.stat-content h3 {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 16px;
  font-weight: 500;
}

.stat-number {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 30px;
}

.chart-container {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.chart-container h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
  font-size: 18px;
  font-weight: 600;
}

.chart-placeholder {
  height: 250px;
  background: #f8f9fa;
  border: 2px dashed #dee2e6;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6c757d;
  font-style: italic;
}
</style>