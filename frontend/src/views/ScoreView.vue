<template>
  <div class="score-view">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>成绩查看</h2>
      <p class="subtitle">查看学生成绩信息和统计数据</p>
    </div>

    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input
              v-model="searchForm.studentName"
              placeholder="搜索学生姓名"
              clearable
              prefix-icon="el-icon-user"
              @change="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="searchForm.studentNumber"
              placeholder="搜索学号"
              clearable
              prefix-icon="el-icon-search"
              @change="handleSearch"
            />
          </el-col>
          <el-col :span="5">
            <el-select
              v-model="searchForm.subject"
              placeholder="选择科目"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="subject in subjectList"
                :key="subject"
                :label="subject"
                :value="subject"
              />
            </el-select>
          </el-col>
          <el-col :span="5">
            <el-select
              v-model="searchForm.semester"
              placeholder="选择学期"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="semester in semesterList"
                :key="semester"
                :label="semester"
                :value="semester"
              />
            </el-select>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" icon="el-icon-refresh" @click="resetSearch">
              重置
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalScores }}</div>
              <div class="stat-label">总成绩条数</div>
            </div>
            <el-icon class="stat-icon"><Document /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.averageScore }}</div>
              <div class="stat-label">平均成绩</div>
            </div>
            <el-icon class="stat-icon"><TrendCharts /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.passRate }}%</div>
              <div class="stat-label">及格率</div>
            </div>
            <el-icon class="stat-icon"><CircleCheck /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.excellentRate }}%</div>
              <div class="stat-label">优秀率</div>
            </div>
            <el-icon class="stat-icon"><Star /></el-icon>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 成绩表格 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>成绩列表</span>
          <div class="header-actions">
            <el-button type="success" icon="el-icon-download" @click="exportScores">
              导出成绩
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="scoreList"
        style="width: 100%"
        stripe
        border
        height="500"
      >
        <el-table-column prop="studentName" label="学生姓名" width="120" />
        <el-table-column prop="studentNumber" label="学号" width="140" />
        <el-table-column prop="className" label="班级" width="120" />
        <el-table-column prop="subject" label="科目" width="120" />
        <el-table-column prop="examType" label="考试类型" width="100">
          <template #default="scope">
            <el-tag
              :type="getExamTypeColor(scope.row.examType)"
              size="small"
            >
              {{ getExamTypeName(scope.row.examType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="成绩" width="80" align="center">
          <template #default="scope">
            <span :class="getScoreClass(scope.row.score)">
              {{ scope.row.score }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="等级" width="80" align="center">
          <template #default="scope">
            <el-tag
              :type="getGradeColor(scope.row.grade)"
              size="small"
            >
              {{ scope.row.grade }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isPassed" label="是否及格" width="100" align="center">
          <template #default="scope">
            <el-icon
              :class="scope.row.isPassed ? 'pass-icon' : 'fail-icon'"
            >
              <CircleCheck v-if="scope.row.isPassed" />
              <CircleClose v-else />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="semester" label="学期" width="100" />
        <el-table-column prop="academicYear" label="学年" width="120" />
        <el-table-column prop="teacher" label="任课教师" width="120" />
        <el-table-column prop="examDate" label="考试日期" width="180" />
        <el-table-column prop="remarks" label="备注" min-width="150" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import { Document, TrendCharts, CircleCheck, CircleClose, Star } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'ScoreView',
  components: {
    Document,
    TrendCharts,
    CircleCheck,
    CircleClose,
    Star
  },
  setup() {
    const loading = ref(false)
    const scoreList = ref([])
    const subjectList = ref([])
    const semesterList = ref([])

    // 搜索表单
    const searchForm = reactive({
      studentName: '',
      studentNumber: '',
      subject: '',
      semester: ''
    })

    // 分页信息
    const pagination = reactive({
      currentPage: 1,
      pageSize: 20,
      total: 0
    })

    // 统计信息
    const statistics = reactive({
      totalScores: 0,
      averageScore: 0,
      passRate: 0,
      excellentRate: 0
    })

    // 获取成绩列表
    const fetchScores = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize,
          ...searchForm
        }
        
        const response = await axios.get('/api/scores', { params })
        
        if (response.data.success) {
          scoreList.value = response.data.data.records || []
          pagination.total = response.data.data.total || 0
          
          // 计算统计信息
          calculateStatistics()
        } else {
          ElMessage.error(response.data.message || '获取成绩列表失败')
        }
      } catch (error) {
        console.error('获取成绩列表失败:', error)
        ElMessage.error('获取成绩列表失败')
      } finally {
        loading.value = false
      }
    }

    // 获取科目列表
    const fetchSubjects = async () => {
      try {
        const response = await axios.get('/api/scores/subjects')
        if (response.data.success) {
          subjectList.value = response.data.data || []
        }
      } catch (error) {
        console.error('获取科目列表失败:', error)
      }
    }

    // 获取学期列表
    const fetchSemesters = async () => {
      try {
        const response = await axios.get('/api/scores/semesters')
        if (response.data.success) {
          semesterList.value = response.data.data || []
        }
      } catch (error) {
        console.error('获取学期列表失败:', error)
      }
    }

    // 计算统计信息
    const calculateStatistics = () => {
      const scores = scoreList.value
      statistics.totalScores = scores.length
      
      if (scores.length > 0) {
        const totalScore = scores.reduce((sum, item) => sum + parseFloat(item.score), 0)
        statistics.averageScore = (totalScore / scores.length).toFixed(1)
        
        const passedCount = scores.filter(item => item.isPassed).length
        statistics.passRate = ((passedCount / scores.length) * 100).toFixed(1)
        
        const excellentCount = scores.filter(item => parseFloat(item.score) >= 90).length
        statistics.excellentRate = ((excellentCount / scores.length) * 100).toFixed(1)
      } else {
        statistics.averageScore = 0
        statistics.passRate = 0
        statistics.excellentRate = 0
      }
    }

    // 搜索处理
    const handleSearch = () => {
      pagination.currentPage = 1
      fetchScores()
    }

    // 重置搜索
    const resetSearch = () => {
      Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
      })
      handleSearch()
    }

    // 分页处理
    const handleSizeChange = (val) => {
      pagination.pageSize = val
      pagination.currentPage = 1
      fetchScores()
    }

    const handleCurrentChange = (val) => {
      pagination.currentPage = val
      fetchScores()
    }

    // 导出成绩
    const exportScores = () => {
      const loading = ElLoading.service({
        lock: true,
        text: '正在导出成绩...',
        background: 'rgba(0, 0, 0, 0.7)'
      })

      try {
        // 这里可以调用后端导出接口
        setTimeout(() => {
          ElMessage.success('成绩导出成功')
          loading.close()
        }, 2000)
      } catch (error) {
        ElMessage.error('导出失败')
        loading.close()
      }
    }

    // 获取考试类型颜色
    const getExamTypeColor = (type) => {
      const colorMap = {
        'MIDTERM': 'warning',
        'FINAL': 'danger',
        'QUIZ': 'info',
        'ASSIGNMENT': 'success'
      }
      return colorMap[type] || ''
    }

    // 获取考试类型名称
    const getExamTypeName = (type) => {
      const nameMap = {
        'MIDTERM': '期中',
        'FINAL': '期末',
        'QUIZ': '小测验',
        'ASSIGNMENT': '作业'
      }
      return nameMap[type] || type
    }

    // 获取成绩样式类
    const getScoreClass = (score) => {
      const num = parseFloat(score)
      if (num >= 90) return 'score-excellent'
      if (num >= 80) return 'score-good'
      if (num >= 60) return 'score-pass'
      return 'score-fail'
    }

    // 获取等级颜色
    const getGradeColor = (grade) => {
      const colorMap = {
        'A': 'success',
        'B': 'primary',
        'C': 'warning',
        'D': 'info',
        'F': 'danger'
      }
      return colorMap[grade] || ''
    }

    // 初始化
    onMounted(() => {
      fetchScores()
      fetchSubjects()
      fetchSemesters()
    })

    return {
      loading,
      scoreList,
      subjectList,
      semesterList,
      searchForm,
      pagination,
      statistics,
      handleSearch,
      resetSearch,
      handleSizeChange,
      handleCurrentChange,
      exportScores,
      getExamTypeColor,
      getExamTypeName,
      getScoreClass,
      getGradeColor
    }
  }
}
</script>

<style scoped>
.score-view {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.subtitle {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  padding: 10px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.stat-content {
  position: relative;
  z-index: 2;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.stat-icon {
  position: absolute;
  top: 20px;
  right: 20px;
  font-size: 24px;
  color: #E4E7ED;
  z-index: 1;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.score-excellent {
  color: #67C23A;
  font-weight: bold;
}

.score-good {
  color: #409EFF;
  font-weight: bold;
}

.score-pass {
  color: #E6A23C;
}

.score-fail {
  color: #F56C6C;
  font-weight: bold;
}

.pass-icon {
  color: #67C23A;
  font-size: 18px;
}

.fail-icon {
  color: #F56C6C;
  font-size: 18px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>