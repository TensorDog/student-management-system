<template>
  <div class="score-view">
    <h1 class="page-title">成绩查看</h1>
    
    <!-- 过滤器栏 -->
    <div class="filter-bar">
      <div class="filter-group">
        <label>选择班级:</label>
        <select v-model="selectedClassId" @change="loadScores">
          <option value="">全部班级</option>
          <option v-for="classItem in classes" :key="classItem.id" :value="classItem.id">
            {{ classItem.name }}
          </option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>选择课程:</label>
        <select v-model="selectedSubject" @change="loadScores">
          <option value="">全部课程</option>
          <option v-for="subject in subjects" :key="subject" :value="subject">
            {{ subject }}
          </option>
        </select>
      </div>
      
      <div class="filter-group">
        <label>考试类型:</label>
        <select v-model="selectedExamType" @change="loadScores">
          <option value="">全部类型</option>
          <option value="期中考试">期中考试</option>
          <option value="期末考试">期末考试</option>
          <option value="平时测验">平时测验</option>
          <option value="作业">作业</option>
        </select>
      </div>
      
      <div class="search-group">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索学生姓名..."
          @input="handleSearch"
        />
      </div>
    </div>
    
    <!-- 统计信息 -->
    <div class="stats-section">
      <div class="stat-card">
        <h3>平均分</h3>
        <p class="stat-value">{{ averageScore.toFixed(1) }}</p>
      </div>
      <div class="stat-card">
        <h3>最高分</h3>
        <p class="stat-value">{{ maxScore }}</p>
      </div>
      <div class="stat-card">
        <h3>最低分</h3>
        <p class="stat-value">{{ minScore }}</p>
      </div>
      <div class="stat-card">
        <h3>及格率</h3>
        <p class="stat-value">{{ passRate.toFixed(1) }}%</p>
      </div>
    </div>
    
    <!-- 成绩表格 -->
    <div class="table-container">
      <table class="score-table">
        <thead>
          <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>班级</th>
            <th>课程</th>
            <th>考试类型</th>
            <th>分数</th>
            <th>等级</th>
            <th>考试时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="score in filteredScores" :key="score.id" :class="getScoreRowClass(score.score)">
            <td>{{ score.studentId }}</td>
            <td>{{ score.studentName }}</td>
            <td>{{ score.className }}</td>
            <td>{{ score.subject }}</td>
            <td>{{ score.examType }}</td>
            <td class="score-cell">
              <span class="score-value">{{ score.score }}</span>
              <span class="score-full">/100</span>
            </td>
            <td>
              <span class="grade-badge" :class="getGradeClass(score.score)">
                {{ getGrade(score.score) }}
              </span>
            </td>
            <td>{{ formatDate(score.examDate) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 空状态 -->
    <div v-if="filteredScores.length === 0" class="empty-state">
      <div class="empty-icon">📊</div>
      <h3>暂无成绩数据</h3>
      <p>{{ getEmptyMessage() }}</p>
    </div>
    
    <!-- 分页 -->
    <div v-if="filteredScores.length > 0" class="pagination">
      <button 
        class="btn btn-secondary" 
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        上一页
      </button>
      <span class="page-info">
        第 {{ currentPage }} 页，共 {{ totalPages }} 页，总计 {{ totalScores }} 条记录
      </span>
      <button 
        class="btn btn-secondary" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '@/services/api'

const route = useRoute()

// 响应式数据
const scores = ref([])
const classes = ref([])
const subjects = ref([])
const searchKeyword = ref('')
const selectedClassId = ref('')
const selectedSubject = ref('')
const selectedExamType = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalScores = ref(0)

// 计算属性
const filteredScores = computed(() => {
  let filtered = scores.value
  
  if (searchKeyword.value) {
    filtered = filtered.filter(score => 
      score.studentName.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  }
  
  return filtered
})

const totalPages = computed(() => Math.ceil(totalScores.value / pageSize.value))

const averageScore = computed(() => {
  if (filteredScores.value.length === 0) return 0
  const sum = filteredScores.value.reduce((acc, score) => acc + score.score, 0)
  return sum / filteredScores.value.length
})

const maxScore = computed(() => {
  if (filteredScores.value.length === 0) return 0
  return Math.max(...filteredScores.value.map(score => score.score))
})

const minScore = computed(() => {
  if (filteredScores.value.length === 0) return 0
  return Math.min(...filteredScores.value.map(score => score.score))
})

const passRate = computed(() => {
  if (filteredScores.value.length === 0) return 0
  const passCount = filteredScores.value.filter(score => score.score >= 60).length
  return (passCount / filteredScores.value.length) * 100
})

// 方法
const loadScores = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      classId: selectedClassId.value,
      subject: selectedSubject.value,
      examType: selectedExamType.value
    }
    
    const response = await api.get('/score-service/api/scores', { params })
    scores.value = response.data.content || []
    totalScores.value = response.data.totalElements || 0
    
  } catch (error) {
    console.error('加载成绩失败:', error)
    // 使用模拟数据
    scores.value = [
      {
        id: 1,
        studentId: '20230001',
        studentName: '张三',
        className: '软件工程1班',
        subject: '数据结构',
        examType: '期末考试',
        score: 85,
        examDate: '2023-12-15'
      },
      {
        id: 2,
        studentId: '20230002',
        studentName: '李四',
        className: '软件工程1班',
        subject: '算法设计',
        examType: '期中考试',
        score: 92,
        examDate: '2023-11-20'
      }
    ]
    totalScores.value = 2
  }
}

const loadClasses = async () => {
  try {
    const response = await api.get('/class-service/api/classes')
    classes.value = response.data || []
  } catch (error) {
    console.error('加载班级失败:', error)
    classes.value = [
      { id: 1, name: '软件工程1班' },
      { id: 2, name: '计算机科学2班' }
    ]
  }
}

const loadSubjects = async () => {
  try {
    const response = await api.get('/score-service/api/subjects')
    subjects.value = response.data || []
  } catch (error) {
    console.error('加载课程失败:', error)
    subjects.value = ['数据结构', '算法设计', '操作系统', '计算机网络', '数据库原理']
  }
}

const handleSearch = () => {
  // 搜索由计算属性自动处理
}

const changePage = (page) => {
  currentPage.value = page
  loadScores()
}

const getGrade = (score) => {
  if (score >= 90) return 'A'
  if (score >= 80) return 'B'
  if (score >= 70) return 'C'
  if (score >= 60) return 'D'
  return 'F'
}

const getGradeClass = (score) => {
  if (score >= 90) return 'grade-a'
  if (score >= 80) return 'grade-b'
  if (score >= 70) return 'grade-c'
  if (score >= 60) return 'grade-d'
  return 'grade-f'
}

const getScoreRowClass = (score) => {
  if (score >= 90) return 'row-excellent'
  if (score >= 80) return 'row-good'
  if (score >= 60) return 'row-pass'
  return 'row-fail'
}

const getEmptyMessage = () => {
  if (searchKeyword.value) return '没有找到匹配的成绩记录'
  if (selectedClassId.value || selectedSubject.value || selectedExamType.value) {
    return '当前筛选条件下没有成绩记录'
  }
  return '暂无成绩数据，请联系管理员录入成绩'
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 监听路由参数变化
watch(() => route.query, (newQuery) => {
  if (newQuery.classId) {
    selectedClassId.value = newQuery.classId
  }
  if (newQuery.subject) {
    selectedSubject.value = newQuery.subject
  }
  if (newQuery.examType) {
    selectedExamType.value = newQuery.examType
  }
  loadScores()
}, { immediate: true })

// 组件挂载时加载数据
onMounted(() => {
  loadClasses()
  loadSubjects()
  loadScores()
})
</script>

<style scoped>
.score-view {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-title {
  color: #2c3e50;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
}

.filter-bar {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  align-items: end;
}

.filter-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.filter-group select,
.search-group input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.stat-card h3 {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
}

.stat-value {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
}

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.score-table {
  width: 100%;
  border-collapse: collapse;
}

.score-table th,
.score-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.score-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.score-cell {
  font-weight: 600;
}

.score-value {
  font-size: 16px;
}

.score-full {
  color: #666;
  font-size: 12px;
}

.grade-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.grade-a { background: #d4edda; color: #155724; }
.grade-b { background: #cce7ff; color: #004085; }
.grade-c { background: #fff3cd; color: #856404; }
.grade-d { background: #f8d7da; color: #721c24; }
.grade-f { background: #f5c6cb; color: #721c24; }

.row-excellent { background: #f8fff9; }
.row-good { background: #f0f8ff; }
.row-pass { background: #fffbf0; }
.row-fail { background: #fff5f5; }

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.page-info {
  color: #666;
  font-size: 14px;
}
</style>