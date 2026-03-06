<template>
  <div class="attendance-manage-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="员工姓名">
          <el-input v-model="searchForm.empName" placeholder="请输入员工姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="searchForm.empNo" placeholder="请输入工号" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 240px"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="迟到" :value="2" />
            <el-option label="早退" :value="3" />
            <el-option label="缺勤" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">考勤记录管理</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
        <el-table-column prop="empNo" label="工号" width="120" />
        <el-table-column prop="empName" label="姓名" width="100" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="attendanceDate" label="日期" width="120" />
        <el-table-column prop="checkInTime" label="签到时间" width="100" />
        <el-table-column prop="checkOutTime" label="签退时间" width="100" />
        <el-table-column prop="workHours" label="工作时长" width="100" align="center">
          <template #default="{ row }">
            {{ row.workHours || 0 }}小时
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else-if="row.status === 2" type="warning">迟到</el-tag>
            <el-tag v-else-if="row.status === 3" type="warning">早退</el-tag>
            <el-tag v-else-if="row.status === 4" type="danger">缺勤</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkInLocation" label="签到地点" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              修正
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 修正对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="修正考勤记录"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号">
              <el-input v-model="formData.empNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="formData.empName" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="签到时间" prop="checkInTime">
              <el-time-picker
                v-model="formData.checkInTime"
                format="HH:mm"
                value-format="HH:mm"
                placeholder="选择签到时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签退时间" prop="checkOutTime">
              <el-time-picker
                v-model="formData.checkOutTime"
                format="HH:mm"
                value-format="HH:mm"
                placeholder="选择签退时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="考勤状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="2">迟到</el-radio>
            <el-radio :value="3">早退</el-radio>
            <el-radio :value="4">缺勤</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="签到地点">
          <el-input v-model="formData.checkInLocation" placeholder="请输入签到地点" />
        </el-form-item>

        <el-form-item label="签退地点">
          <el-input v-model="formData.checkOutLocation" placeholder="请输入签退地点" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAttendanceList, updateAttendance } from '@/api/attendance'

// 日期范围
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  empName: '',
  empNo: '',
  startDate: '',
  endDate: '',
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const submitting = ref(false)

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  id: null,
  empNo: '',
  empName: '',
  checkInTime: '',
  checkOutTime: '',
  status: 1,
  checkInLocation: '',
  checkOutLocation: ''
})

// 表单验证规则
const formRules = {
  status: [
    { required: true, message: '请选择考勤状态', trigger: 'change' }
  ]
}

// 加载考勤记录
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getAttendanceList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载考勤记录失败')
  } finally {
    loading.value = false
  }
}

// 日期范围变化
const handleDateChange = (value) => {
  if (value && value.length === 2) {
    searchForm.startDate = value[0]
    searchForm.endDate = value[1]
  } else {
    searchForm.startDate = ''
    searchForm.endDate = ''
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  dateRange.value = []
  Object.assign(searchForm, {
    empName: '',
    empNo: '',
    startDate: '',
    endDate: '',
    status: null
  })
  handleSearch()
}

// 编辑
const handleEdit = (row) => {
  Object.assign(formData, {
    id: row.id,
    empNo: row.empNo,
    empName: row.empName,
    checkInTime: row.checkInTime || '',
    checkOutTime: row.checkOutTime || '',
    status: row.status,
    checkInLocation: row.checkInLocation || '',
    checkOutLocation: row.checkOutLocation || ''
  })
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      await updateAttendance(formData)
      ElMessage.success('修正成功')
      dialogVisible.value = false
      loadTableData()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.pageNum = 1
  loadTableData()
}

// 页码变化
const handleCurrentChange = (val) => {
  pagination.pageNum = val
  loadTableData()
}

onMounted(() => {
  loadTableData()
})
</script>

<style scoped lang="css">
.attendance-manage-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.table-card {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
