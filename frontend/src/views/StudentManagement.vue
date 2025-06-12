<template>
  <div class="student-management">
    <h1 class="page-title">学生管理</h1>
    
    <!-- 操作栏 -->
    <div class="action-bar">
      <button class="btn btn-primary" @click="showAddModal = true">
        ➕ 新增学生
      </button>
      <div class="search-box">
        <input 
          v-model="searchKeyword" 
          type="text" 
          placeholder="搜索学生姓名或学号..."
          @input="handleSearch"
        />
      </div>
    </div>
    
    <!-- 学生表格 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>学号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>班级</th>
            <th>联系方式</th>
            <th>入学时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="student in students" :key="student.id">
            <td>{{ student.studentId }}</td>
            <td>{{ student.name }}</td>
            <td>{{ student.gender }}</td>
            <td>{{ student.className }}</td>
            <td>{{ student.phone }}</td>
            <td>{{ formatDate(student.enrollmentDate) }}</td>
            <td class="actions">
              <button class="btn btn-sm btn-info" @click="viewStudent(student)">
                👁️ 查看
              </button>
              <button class="btn btn-sm btn-warning" @click="editStudent(student)">
                ✏️ 编辑
              </button>
              <button class="btn btn-sm btn-danger" @click="deleteStudent(student.id)">
                🗑️ 删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <!-- 分页组件 -->
    <div class="pagination">
      <button 
        class="btn btn-secondary" 
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        上一页
      </button>
      <span class="page-info">
        第 {{ currentPage }} 页，共 {{ totalPages }} 页，总计 {{ totalStudents }} 名学生
      </span>
      <button 
        class="btn btn-secondary" 
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        下一页
      </button>
    </div>
    
    <!-- 新增/编辑学生模态框 -->
    <div v-if="showAddModal || showEditModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑学生' : '新增学生' }}</h3>
        <form @submit.prevent="saveStudent">
          <div class="form-group">
            <label>学号</label>
            <input v-model="formData.studentId" type="text" required />
          </div>
          <div class="form-group">
            <label>姓名</label>
            <input v-model="formData.name" type="text" required />
          </div>
          <div class="form-group">
            <label>性别</label>
            <select v-model="formData.gender" required>
              <option value="男">男</option>
              <option value="女">女</option>
            </select>
          </div>
          <div class="form-group">
            <label>班级ID</label>
            <input v-model="formData.classId" type="number" required />
          </div>
          <div class="form-group">
            <label>联系方式</label>
            <input v-model="formData.phone" type="text" />
          </div>
          <div class="form-group">
            <label>入学时间</label>
            <input v-model="formData.enrollmentDate" type="date" required />
          </div>
          <div class="form-actions">
            <button type="button" class="btn btn-secondary" @click="closeModal">
              取消
            </button>
            <button type="submit" class="btn btn-primary">
              {{ isEditing ? '更新' : '创建' }}
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
const students = ref([])
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalStudents = ref(0)
const showAddModal = ref(false)
const showEditModal = ref(false)
const editingStudent = ref(null)

// 表单数据
const formData = ref({
  studentId: '',
  name: '',
  gender: '男',
  classId: '',
  phone: '',
  enrollmentDate: ''
})

// 计算属性
const totalPages = computed(() => Math.ceil(totalStudents.value / pageSize.value))
const isEditing = computed(() => !!editingStudent.value)

// 加载学生列表
const loadStudents = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    }
    
    const response = await api.get('/user-service/api/users', { params })
    students.value = response.data.content || []
    totalStudents.value = response.data.totalElements || 0
    
  } catch (error) {
    console.error('加载学生列表失败:', error)
    // 使用模拟数据
    students.value = [
      {
        id: 1,
        studentId: '20230001',
        name: '张三',
        gender: '男',
        classId: 1,
        className: '软件工程1班',
        phone: '13800138001',
        enrollmentDate: '2023-09-01'
      },
      {
        id: 2,
        studentId: '20230002',
        name: '李四',
        gender: '女',
        classId: 1,
        className: '软件工程1班',
        phone: '13800138002',
        enrollmentDate: '2023-09-01'
      }
    ]
    totalStudents.value = 2
  }
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  loadStudents()
}

// 分页处理
const changePage = (page) => {
  currentPage.value = page
  loadStudents()
}

// 查看学生详情
const viewStudent = (student) => {
  // 跳转到学生详情页面或显示详情模态框
  console.log('查看学生:', student)
}

// 编辑学生
const editStudent = (student) => {
  editingStudent.value = student
  formData.value = { ...student }
  showEditModal.value = true
}

// 删除学生
const deleteStudent = async (studentId) => {
  if (!confirm('确定要删除这名学生吗？')) return
  
  try {
    await api.delete(`/user-service/api/users/${studentId}`)
    await loadStudents()
    alert('删除成功!')
  } catch (error) {
    console.error('删除学生失败:', error)
    alert('删除失败!')
  }
}

// 保存学生（新增或编辑）
const saveStudent = async () => {
  try {
    if (isEditing.value) {
      await api.put(`/user-service/api/users/${editingStudent.value.id}`, formData.value)
      alert('更新成功!')
    } else {
      await api.post('/user-service/api/users', formData.value)
      alert('创建成功!')
    }
    
    closeModal()
    await loadStudents()
    
  } catch (error) {
    console.error('保存学生失败:', error)
    alert('保存失败!')
  }
}

// 关闭模态框
const closeModal = () => {
  showAddModal.value = false
  showEditModal.value = false
  editingStudent.value = null
  formData.value = {
    studentId: '',
    name: '',
    gender: '男',
    classId: '',
    phone: '',
    enrollmentDate: ''
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 组件挂载时加载数据
onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.student-management {
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

.table-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.data-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.data-table tr:hover {
  background: #f8f9fa;
}

.actions {
  display: flex;
  gap: 8px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.page-info {
  color: #666;
  font-size: 14px;
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

.btn-secondary:disabled {
  background: #ccc;
  cursor: not-allowed;
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
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
  margin-top: 30px;
}
</style>