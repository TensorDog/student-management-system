<template>
  <div class="dashboard">
    <!-- 统计卡片区域 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon student-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.studentTotal }}</div>
                <div class="stat-label">学生总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon class-icon">
                <el-icon><School /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.classTotal }}</div>
                <div class="stat-label">班级总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon score-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-number">{{ statistics.averageScore }}</div>
                <div class="stat-label">平均成绩</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>成绩分布</span>
              </div>
            </template>
            <div ref="pieChart" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>班级成绩趋势</span>
              </div>
            </template>
            <div ref="barChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getStatistics, getScoreDistribution, getClassScoreTrend } from '@/api/dashboard'

// 统计数据
const statistics = reactive({
  studentTotal: 0,
  classTotal: 0,
  averageScore: 0
})

// 图表引用
const pieChart = ref(null)
const barChart = ref(null)

// 图表实例
let pieChartInstance = null
let barChartInstance = null

// 初始化饼图
const initPieChart = async () => {
  try {
    const data = await getScoreDistribution()
    pieChartInstance = echarts.init(pieChart.value)
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '成绩分布',
          type: 'pie',
          radius: '50%',
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    
    pieChartInstance.setOption(option)
  } catch (error) {
    console.error('初始化饼图失败:', error)
  }
}

// 初始化柱状图
const initBarChart = async () => {
  try {
    const data = await getClassScoreTrend()
    barChartInstance = echarts.init(barChart.value)
    
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: data.series.map(item => item.name)
      },
      xAxis: {
        type: 'category',
        data: data.categories
      },
      yAxis: {
        type: 'value'
      },
      series: data.series.map(item => ({
        name: item.name,
        type: 'bar',
        data: item.data
      }))
    }
    
    barChartInstance.setOption(option)
  } catch (error) {
    console.error('初始化柱状图失败:', error)
  }
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    const data = await getStatistics()
    Object.assign(statistics, data)
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 窗口大小变化时调整图表
const handleResize = () => {
  if (pieChartInstance) {
    pieChartInstance.resize()
  }
  if (barChartInstance) {
    barChartInstance.resize()
  }
}

// 组件挂载后初始化
onMounted(() => {
  loadStatistics()
  
  nextTick(() => {
    initPieChart()
    initBarChart()
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize)
  })
})

// 组件卸载时清理
import { onBeforeUnmount } from 'vue'
onBeforeUnmount(() => {
  if (pieChartInstance) {
    pieChartInstance.dispose()
  }
  if (barChartInstance) {
    barChartInstance.dispose()
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.student-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.class-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.score-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.charts-section {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  color: #303133;
}

.chart-container {
  height: 350px;
  width: 100%;
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>