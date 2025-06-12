<template>
  <div class="class-list">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>班级管理</h2>
      <p class="subtitle">管理班级信息，支持新增班级和查看班级详情</p>
    </div>

    <!-- 搜索和操作区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input
              v-model="searchForm.className"
              placeholder="搜索班级名称"
              clearable
              prefix-icon="el-icon-search"
              @change="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-select
              v-model="searchForm.grade"
              placeholder="选择年级"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="grade in gradeList"
                :key="grade"
                :label="grade"
                :value="grade"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select
              v-model="searchForm.status"
              placeholder="班级状态"
              clearable
              @change="handleSearch"
            >
              <el-option label="可用班级" value="available" />
              <el-option label="已满班级" value="full" />
              <el-option label="所有班级" value="all" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <div class="action-buttons">
              <el-button type="primary" icon="el-icon-plus" @click="openAddDialog">
                新增班级
              </el-button>
              <el-button type="default" icon="el-icon-refresh" @click="resetSearch">
                重置
              </el-button>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card" @click="filterByStatus('all')">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalClasses }}</div>
              <div class="stat-label">总班级数</div>
            </div>
            <el-icon class="stat-icon"><School /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card" @click="filterByStatus('available')">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.availableClasses }}</div>
              <div class="stat-label">可用班级</div>
            </div>
            <el-icon class="stat-icon"><CircleCheck /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card" @click="filterByStatus('full')">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.fullClasses }}</div>
              <div class="stat-label">已满班级</div>
            </div>
            <el-icon class="stat-icon"><Warning /></el-icon>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalStudents }}</div>
              <div class="stat-label">总学生数</div>
            </div>
            <el-icon class="stat-icon"><User /></el-icon>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 班级列表 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>班级列表</span>
          <div class="header-actions">
            <el-tooltip content="刷新数据" placement="top">
              <el-button type="default" icon="el-icon-refresh" circle @click="fetchClasses" />
            </el-tooltip>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="classList"
        style="width: 100%"
        stripe
        border
        @row-click="handleRowClick"
      >
        <el-table-column prop="className" label="班级名称" width="180">
          <template #default="scope">
            <div class="class-name">
              <el-icon class="class-icon"><School /></el-icon>
              <span>{{ scope.row.className }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="grade" label="年级" width="120" />
        
        <el-table-column label="容量情况" width="200">
          <template #default="scope">
            <div class="capacity-info">
              <div class="capacity-text">
                {{ scope.row.currentCount }} / {{ scope.row.capacity }}
              </div>
              <el-progress
                :percentage="getCapacityPercentage(scope.row)"
                :color="getCapacityColor(scope.row)"
                :stroke-width="8"
                :show-text="false"
              />
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="状态" width="120" align="center">
          <template #default="scope">
            <el-tag
              :type="getStatusColor(scope.row)"
              size="small"
            >
              {{ getStatusText(scope.row) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.updatedAt) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-view"
              @click.stop="viewClassDetails(scope.row)"
            >
              查看详情
            </el-button>
            <el-button
              type="warning"
              size="small"
              icon="el-icon-edit"
              @click.stop="editClass(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              icon="el-icon-delete"
              @click.stop="deleteClass(scope.row)"
              :disabled="scope.row.currentCount > 0"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 新增/编辑班级对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="classFormRef"
        :model="classForm"
        :rules="classFormRules"
        label-width="100px"
      >
        <el-form-item label="班级名称" prop="className">
          <el-input
            v-model="classForm.className"
            placeholder="请输入班级名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="年级" prop="grade">
          <el-select
            v-model="classForm.grade"
            placeholder="请选择年级"
            style="width: 100%"
          >
            <el-option
              v-for="grade in gradeOptions"
              :key="grade.value"
              :label="grade.label"
              :value="grade.value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="班级容量" prop="capacity">
          <el-input-number
            v-model="classForm.capacity"
            :min="1"
            :max="100"
            :step="1"
            placeholder="请输入班级容量"
            style="width: 100%"
          />
          <div class="form-tip">建议班级容量在20-50人之间</div>
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input
            v-model="classForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 班级详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="班级详情"
      width="800px"
    >
      <div v-if="selectedClass" class="class-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="班级名称">
            {{ selectedClass.className }}
          </el-descriptions-item>
          <el-descriptions-item label="年级">
            {{ selectedClass.grade }}
          </el-descriptions-item>
          <el-descriptions-item label="班级容量">
            {{ selectedClass.capacity }}人
          </el-descriptions-item>
          <el-descriptions-item label="当前人数">
            {{ selectedClass.currentCount }}人
          </el-descriptions-item>
          <el-descriptions-item label="可用名额">
            {{ selectedClass.capacity - selectedClass.currentCount }}人
          </el-descriptions-item>
          <el-descriptions-item label="班级状态">
            <el-tag :type="getStatusColor(selectedClass)">
              {{ getStatusText(selectedClass) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(selectedClass.createdAt) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(selectedClass.updatedAt) }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-actions">
          <el-button type="primary" @click="goToStudentList">
            查看班级学生
          </el-button>
          <el-button type="success" @click="addStudentToClass">
            添加学生到班级
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { School, User, CircleCheck, Warning } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'ClassList',
  components: {
    School,
    User,
    CircleCheck,
    Warning
  },
  setup() {
    const loading = ref(false)
    const submitLoading = ref(false)
    const classList = ref([])
    const gradeList = ref(['一年级', '二年级', '三年级', '四年级', '五年级', '六年级'])
    const dialogVisible = ref(false)
    const detailDialogVisible = ref(false)
    const isEdit = ref(false)
    const selectedClass = ref(null)
    const classFormRef = ref(null)

    // 搜索表单
    const searchForm = reactive({
      className: '',
      grade: '',
      status: ''
    })

    // 分页信息
    const pagination = reactive({
      currentPage: 1,
      pageSize: 20,
      total: 0
    })

    // 统计信息
    const statistics = reactive({
      totalClasses: 0,
      availableClasses: 0,
      fullClasses: 0,
      totalStudents: 0
    })

    // 班级表单
    const classForm = reactive({
      id: null,
      className: '',
      grade: '',
      capacity: 30,
      remarks: ''
    })

    // 年级选项
    const gradeOptions = [
      { value: '一年级', label: '一年级' },
      { value: '二年级', label: '二年级' },
      { value: '三年级', label: '三年级' },
      { value: '四年级', label: '四年级' },
      { value: '五年级', label: '五年级' },
      { value: '六年级', label: '六年级' }
    ]

    // 表单验证规则
    const classFormRules = {
      className: [
        { required: true, message: '请输入班级名称', trigger: 'blur' },
        { min: 2, max: 50, message: '班级名称长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      grade: [
        { required: true, message: '请选择年级', trigger: 'change' }
      ],
      capacity: [
        { required: true, message: '请输入班级容量', trigger: 'blur' },
        { type: 'number', min: 1, max: 100, message: '班级容量在 1 到 100 之间', trigger: 'blur' }
      ]
    }

    // 计算属性
    const dialogTitle = computed(() => {
      return isEdit.value ? '编辑班级' : '新增班级'
    })

    // 获取班级列表
    const fetchClasses = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize,
          ...searchForm
        }
        
        const response = await axios.get('/api/classes', { params })
        
        if (response.data.success) {
          classList.value = response.data.data.records || []
          pagination.total = response.data.data.total || 0
          
          // 计算统计信息
          calculateStatistics()
        } else {
          ElMessage.error(response.data.message || '获取班级列表失败')
        }
      } catch (error) {
        console.error('获取班级列表失败:', error)
        ElMessage.error('获取班级列表失败')
      } finally {
        loading.value = false
      }
    }

    // 计算统计信息
    const calculateStatistics = () => {
      const classes = classList.value
      statistics.totalClasses = classes.length
      statistics.availableClasses = classes.filter(c => c.currentCount < c.capacity).length
      statistics.fullClasses = classes.filter(c => c.currentCount >= c.capacity).length
      statistics.totalStudents = classes.reduce((sum, c) => sum + c.currentCount, 0)
    }

    // 搜索处理
    const handleSearch = () => {
      pagination.currentPage = 1
      fetchClasses()
    }

    // 重置搜索
    const resetSearch = () => {
      Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
      })
      handleSearch()
    }

    // 按状态筛选
    const filterByStatus = (status) => {
      searchForm.status = status
      handleSearch()
    }

    // 分页处理
    const handleSizeChange = (val) => {
      pagination.pageSize = val
      pagination.currentPage = 1
      fetchClasses()
    }

    const handleCurrentChange = (val) => {
      pagination.currentPage = val
      fetchClasses()
    }

    // 行点击事件
    const handleRowClick = (row) => {
      viewClassDetails(row)
    }

    // 打开新增对话框
    const openAddDialog = () => {
      isEdit.value = false
      dialogVisible.value = true
      resetForm()
    }

    // 编辑班级
    const editClass = (row) => {
      isEdit.value = true
      dialogVisible.value = true
      Object.assign(classForm, row)
    }

    // 查看班级详情
    const viewClassDetails = (row) => {
      selectedClass.value = row
      detailDialogVisible.value = true
    }

    // 删除班级
    const deleteClass = async (row) => {
      if (row.currentCount > 0) {
        ElMessage.warning('班级内还有学生，无法删除')
        return
      }

      try {
        await ElMessageBox.confirm(
          `确定要删除班级 "${row.className}" 吗？`,
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        const response = await axios.delete(`/api/classes/${row.id}`)
        
        if (response.data.success) {
          ElMessage.success('删除成功')
          fetchClasses()
        } else {
          ElMessage.error(response.data.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除班级失败:', error)
          ElMessage.error('删除失败')
        }
      }
    }

    // 提交表单
    const submitForm = async () => {
      try {
        await classFormRef.value.validate()
        
        submitLoading.value = true
        
        const url = isEdit.value ? `/api/classes/${classForm.id}` : '/api/classes'
        const method = isEdit.value ? 'put' : 'post'
        
        const response = await axios[method](url, classForm)
        
        if (response.data.success) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchClasses()
          
          // 发出事件，通知其他组件班级列表已更新
          window.dispatchEvent(new CustomEvent('classListUpdated'))
        } else {
          ElMessage.error(response.data.message || '操作失败')
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }

    // 重置表单
    const resetForm = () => {
      Object.assign(classForm, {
        id: null,
        className: '',
        grade: '',
        capacity: 30,
        remarks: ''
      })
      if (classFormRef.value) {
        classFormRef.value.clearValidate()
      }
    }

    // 跳转到学生列表
    const goToStudentList = () => {
      // 这里可以使用 vue-router 跳转到学生管理页面
      // router.push({
      //   path: '/students',
      //   query: { classId: selectedClass.value.id }
      // })
      ElMessage.info('跳转到学生管理页面（需要配置路由）')
    }

    // 添加学生到班级
    const addStudentToClass = () => {
      // 这里可以打开学生选择对话框或跳转到学生添加页面
      ElMessage.info('打开学生添加页面（需要实现学生选择功能）')
    }

    // 工具函数
    const getCapacityPercentage = (row) => {
      return Math.round((row.currentCount / row.capacity) * 100)
    }

    const getCapacityColor = (row) => {
      const percentage = getCapacityPercentage(row)
      if (percentage >= 100) return '#F56C6C'
      if (percentage >= 80) return '#E6A23C'
      return '#67C23A'
    }

    const getStatusColor = (row) => {
      return row.currentCount >= row.capacity ? 'warning' : 'success'
    }

    const getStatusText = (row) => {
      return row.currentCount >= row.capacity ? '已满' : '可用'
    }

    const formatDate = (dateString) => {
      if (!dateString) return '-'
      return new Date(dateString).toLocaleString('zh-CN')
    }

    // 初始化
    onMounted(() => {
      fetchClasses()
    })

    return {
      loading,
      submitLoading,
      classList,
      gradeList,
      dialogVisible,
      detailDialogVisible,
      isEdit,
      selectedClass,
      classFormRef,
      searchForm,
      pagination,
      statistics,
      classForm,
      gradeOptions,
      classFormRules,
      dialogTitle,
      handleSearch,
      resetSearch,
      filterByStatus,
      handleSizeChange,
      handleCurrentChange,
      handleRowClick,
      openAddDialog,
      editClass,
      viewClassDetails,
      deleteClass,
      submitForm,
      resetForm,
      goToStudentList,
      addStudentToClass,
      getCapacityPercentage,
      getCapacityColor,
      getStatusColor,
      getStatusText,
      formatDate
    }
  }
}
</script>

<style scoped>
.class-list {
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

.search-card {
  margin-bottom: 20px;
}

.search-form {
  padding: 10px;
}

.action-buttons {
  display: flex;
  gap: 10px;
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

.class-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.class-icon {
  color: #409EFF;
}

.capacity-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.capacity-text {
  font-size: 12px;
  color: #606266;
  text-align: center;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.class-detail {
  padding: 20px 0;
}

.detail-actions {
  margin-top: 20px;
  text-align: center;
}

.detail-actions .el-button {
  margin: 0 10px;
}
</style>