<template>
  <div class="class-list">
    <h1 class="page-title">班级管理</h1>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <button class="btn btn-primary" @click="showAddForm = !showAddForm">
        ➕ 新增班级
      </button>
      <div class="search-box">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索班级名称..."
          @input="handleSearch"
        />
      </div>
    </div>
    
    <!-- 新增班级表单 -->
    <div v-if="showAddForm" class="add-form-container">
      <form @submit.prevent="addClass" class="add-form">
        <h3>新增班级</h3>
        <div class="form-row">
          <div class="form-group">
            <label>班级名称</label>
            <input v-model="newClass.name" type="text" required placeholder="例如：软件工程1班" />
          </div>
          <div class="form-group">
            <label>班级代码</label>
            <input v-model="newClass.code" type="text" required placeholder="例如：SE2023-1" />
          </div>
          <div class="form-group">
            <label>班主任</label>
            <input v-model="newClass.teacher" type="text" required placeholder="班主任姓名" />
          </div>
          <div class="form-group">
            <label>学年</label>
            <input v-model="newClass.academicYear" type="text" required placeholder="例如：2023-2024" />
          </div>
        </div>
        <div class="form-actions">
          <button type="button" class="btn btn-secondary" @click="cancelAdd">
            取消
          </button>
          <button type="submit" class="btn btn-primary">
            创建班级
          </button>
        </div>
      </form>
    </div>
    
    <!-- 班级卡片列表 -->
    <div class="class-grid">
      <div v-for="classItem in filteredClasses" :key="classItem.id" class="class-card">
        <div class="class-header">
          <h3 class="class-name">{{ classItem.name }}</h3>
          <div class="class-code">{{ classItem.code }}</div>
        </div>
        
        <div class="class-info">
          <div class="info-item">
            <span class="label">班主任:</span>
            <span class="value">{{ classItem.teacher }}</span>
          </div>
          <div class="info-item">
            <span class="label">学年:</span>
            <span class="value">{{ classItem.academicYear }}</span>
          </div>
          <div class="info-item">
            <span class="label">学生人数:</span>
            <span class="value">{{ classItem.studentCount }}人</span>
          </div>
          <div class="info-item">
            <span class="label">创建时间:</span>
            <span class="value">{{ formatDate(classItem.createdAt) }}</span>
          </div>
        </div>
        
        <div class="class-actions">
          <button class="btn btn-sm btn-info" @click="viewClassDetail(classItem)">
            👁️ 查看详情
          </button>
          <button class="btn btn-sm btn-warning" @click="editClass(classItem)">
            ✏️ 编辑
          </button>
          <button class="btn btn-sm btn-danger" @click="deleteClass(classItem.id)">
            🗑️ 删除
          </button>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-if="filteredClasses.length === 0" class="empty-state">
      <div class="empty-icon">📚</div>
      <h3>暂无班级</h3>
      <p>{{ searchKeyword ? '没有找到匹配的班级' : '还没有创建任何班级，点击上方按钮开始创建' }}</p>
    </div>
    
    <!-- 编辑班级模态框 -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal-content" @click.stop>
        <h3>编辑班级</h3>
        <form @submit.prevent="updateClass">
          <div class="form-group">
            <label>班级名称</label>
            <input v-model="editForm.name" type="text" required />
          </div>
          <div class="form-group">
            <label>班级代码</label>
            <input v-model="editForm.code" type="text" required />
          </div>
          <div class="form-group">
            <label>班主任</label>
            <input v-model="editForm.teacher" type="text" required />
          </div>
          <div class="form-group">
            <label>学年</label>
            <input v-model="editForm.academicYear" type="text" required />
          </div>
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeEditModal">
              取消
            </button>
            <button type="submit" class="btn btn-primary">
              更新
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { api } from '@/services/api'

// 响应式数据
const classes = ref([])
const searchKeyword = ref('')
const showAddForm = ref(false)
const showEditModal = ref(false)
const editingClass = ref(null)

// 新增班级表单数据
const newClass = ref({
  name: '',
  code: '',
  teacher: '',
  academicYear: ''
})

// 编辑表单数据
const editForm = ref({
  name: '',
  code: '',
  teacher: '',
  academicYear: ''
})

// 计算属性 - 过滤后的班级列表
const filteredClasses = computed(() => {
  if (!searchKeyword.value) return classes.value
  
  return classes.value.filter(classItem => 
    classItem.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    classItem.code.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    classItem.teacher.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 加载班级列表
const loadClasses = async () => {
  try {
    const response = await api.get('/class-service/api/classes')
    classes.value = response.data || []
    
  } catch (error) {
    console.error('加载班级列表失败:', error)
    // 使用模拟数据
    classes.value = [
      {
        id: 1,
        name: '软件工程1班',
        code: 'SE2023-1',
        teacher: '张教授',
        academicYear: '2023-2024',
        studentCount: 45,
        createdAt: '2023-09-01T08:00:00Z'
      },
      {
        id: 2,
        name: '计算机科学2班',
        code: 'CS2023-2',
        teacher: '李教授',
        academicYear: '2023-2024',
        studentCount: 42,
        createdAt: '2023-09-01T09:00:00Z'
      },
      {
        id: 3,
        name: '数据科学1班',
        code: 'DS2023-1',
        teacher: '王教授',
        academicYear: '2023-2024',
        studentCount: 38,
        createdAt: '2023-09-01T10:00:00Z'
      }
    ]
  }
}

// 搜索处理
const handleSearch = () => {
  // 实时搜索，由计算属性自动处理
}

// 新增班级
const addClass = async () => {
  try {
    const response = await api.post('/class-service/api/classes', newClass.value)
    classes.value.push(response.data)
    
    // 重置表单
    resetNewClassForm()
    showAddForm.value = false
    alert('班级创建成功!')
    
  } catch (error) {
    console.error('创建班级失败:', error)
    alert('创建班级失败!')
  }
}

// 取消新增
const cancelAdd = () => {
  resetNewClassForm()
  showAddForm.value = false
}

// 重置新增表单
const resetNewClassForm = () => {
  newClass.value = {
    name: '',
    code: '',
    teacher: '',
    academicYear: ''
  }
}

// 查看班级详情
const viewClassDetail = (classItem) => {
  // 可以跳转到班级详情页面或显示详情模态框
  console.log('查看班级详情:', classItem)
  // 这里可以使用 router.push() 跳转到详情页面
}

// 编辑班级
const editClass = (classItem) => {
  editingClass.value = classItem
  editForm.value = { ...classItem }
  showEditModal.value = true
}

// 更新班级
const updateClass = async () => {
  try {
    const response = await api.put(`/class-service/api/classes/${editingClass.value.id}`, editForm.value)
    
    // 更新本地数据
    const index = classes.value.findIndex(c => c.id === editingClass.value.id)
    if (index !== -1) {
      classes.value[index] = response.data
    }
    
    closeEditModal()
    alert('班级更新成功!')
    
  } catch (error) {
    console.error('更新班级失败:', error)
    alert('更新班级失败!')
  }
}

// 删除班级
const deleteClass = async (classId) => {
  if (!confirm('确定要删除这个班级吗？删除后相关学生信息可能受到影响。')) return
  
  try {
    await api.delete(`/class-service/api/classes/${classId}`)
    
    // 从本地数据中移除
    classes.value = classes.value.filter(c => c.id !== classId)
    alert('班级删除成功!')
    
  } catch (error) {
    console.error('删除班级失败:', error)
    alert('删除班级失败!')
  }
}

// 关闭编辑模态框
const closeEditModal = () => {
  showEditModal.value = false
  editingClass.value = null
  editForm.value = {
    name: '',
    code: '',
    teacher: '',
    academicYear: ''
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 组件挂载时加载数据
onMounted(() => {
  loadClasses()
})
</script>

<style scoped>
.class-list {
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

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
}

.search-box input {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  width: 300px;
  font-size: 14px;
}

/* 新增表单样式 */
.add-form-container {
  background: white;
  border-radius: 8px;
  padding: 25px;
  margin-bottom: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.add-form h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

/* 班级卡片网格 */
.class-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.class-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.class-card:hover {
  transform: translateY(-2px);
}

.class-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 15px;
}

.class-name {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 20px;
  font-weight: 600;
}

.class-code {
  background: #e9ecef;
  color: #495057;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  display: inline-block;
}

.class-info {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.info-item .label {
  color: #666;
  font-size: 14px;
}

.info-item .value {
  color: #2c3e50;
  font-weight: 500;
  font-size: 14px;
}

.class-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-state h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.empty-state p {
  margin: 0;
  color: #666;
}

/* 按钮样式 */
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-primary:hover {
  background: #0056b3;
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #545b62;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
}
</style>