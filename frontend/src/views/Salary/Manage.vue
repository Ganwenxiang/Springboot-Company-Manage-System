<template>
  <div class="salary-manage-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="action-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="员工姓名">
          <el-input v-model="searchForm.empName" placeholder="请输入员工姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="searchForm.empNo" placeholder="请输入工号" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="月份">
          <el-date-picker
            v-model="searchForm.salaryMonth"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="未发放" :value="0" />
            <el-option label="已发放" :value="1" />
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
        <el-form-item style="float: right">
          <el-button type="success" @click="handleCalculate" :loading="calculating">
            <el-icon><Operation /></el-icon>
            生成工资
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">工资管理</span>
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
        <el-table-column prop="salaryMonth" label="月份" width="100" />
        <el-table-column prop="baseSalary" label="基本工资" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.baseSalary?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="positionSalary" label="岗位工资" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.positionSalary?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalSalary" label="应发工资" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.totalSalary?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="actualSalary" label="实发工资" width="120" align="right">
          <template #default="{ row }">
            <span class="text-success">¥{{ row.actualSalary?.toFixed(2) || '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">未发放</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已发放</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              详情
            </el-button>
            <el-button link type="primary" @click="handleEdit(row)">
              调整
            </el-button>
            <el-button v-if="row.status === 0" link type="success" @click="handlePay(row)">
              标记发放
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="工资详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="工号">{{ currentRow.empNo }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ currentRow.empName }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ currentRow.deptName }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ currentRow.jobTitle || '--' }}</el-descriptions-item>
        <el-descriptions-item label="月份">{{ currentRow.salaryMonth }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 0" type="warning">未发放</el-tag>
          <el-tag v-else-if="currentRow.status === 1" type="success">已发放</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="基本工资">¥{{ currentRow.baseSalary?.toFixed(2) || '0.00' }}</el-descriptions-item>
        <el-descriptions-item label="岗位工资">¥{{ currentRow.positionSalary?.toFixed(2) || '0.00' }}</el-descriptions-item>
        <el-descriptions-item label="加班费">¥{{ currentRow.overtimePay?.toFixed(2) || '0.00' }}</el-descriptions-item>
        <el-descriptions-item label="奖金">¥{{ currentRow.bonus?.toFixed(2) || '0.00' }}</el-descriptions-item>
        <el-descriptions-item label="补贴">¥{{ currentRow.subsidy?.toFixed(2) || '0.00' }}</el-descriptions-item>
        <el-descriptions-item label="应发工资" :span="2">
          <span class="text-primary" style="font-size: 16px; font-weight: 600">
            ¥{{ currentRow.totalSalary?.toFixed(2) || '0.00' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="考勤扣款">
          <span class="text-danger">-¥{{ currentRow.attendanceDeduction?.toFixed(2) || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="社保">
          <span class="text-danger">-¥{{ currentRow.socialSecurity?.toFixed(2) || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="个人所得税">
          <span class="text-danger">-¥{{ currentRow.tax?.toFixed(2) || '0.00' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="实发工资" :span="2">
          <span class="text-success" style="font-size: 18px; font-weight: 600">
            ¥{{ currentRow.actualSalary?.toFixed(2) || '0.00' }}
          </span>
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 调整对话框 -->
    <el-dialog
      v-model="editVisible"
      title="调整工资"
      width="600px"
      @close="handleEditClose"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-form-item label="工号">
          <el-input v-model="editForm.empNo" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editForm.empName" disabled />
        </el-form-item>
        <el-form-item label="月份">
          <el-input v-model="editForm.salaryMonth" disabled />
        </el-form-item>

        <el-divider content-position="left">收入项</el-divider>

        <el-form-item label="基本工资" prop="baseSalary">
          <el-input-number v-model="editForm.baseSalary" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="岗位工资" prop="positionSalary">
          <el-input-number v-model="editForm.positionSalary" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="加班费" prop="overtimePay">
          <el-input-number v-model="editForm.overtimePay" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="奖金" prop="bonus">
          <el-input-number v-model="editForm.bonus" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="补贴" prop="subsidy">
          <el-input-number v-model="editForm.subsidy" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>

        <el-divider content-position="left">扣除项</el-divider>

        <el-form-item label="考勤扣款" prop="attendanceDeduction">
          <el-input-number v-model="editForm.attendanceDeduction" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="社保" prop="socialSecurity">
          <el-input-number v-model="editForm.socialSecurity" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="个人所得税" prop="tax">
          <el-input-number v-model="editForm.tax" :precision="2" :step="100" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>

        <el-divider content-position="left">合计</el-divider>

        <el-form-item label="应发工资">
          <el-input v-model="totalSalaryDisplay" disabled style="width: 200px">
            <template #prefix>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实发工资">
          <el-input v-model="actualSalaryDisplay" disabled style="width: 200px">
            <template #prefix>¥</template>
          </el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEdit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSalaryList, calculateSalary, updateSalary, markSalaryPaid } from '@/api/salary'

// 搜索表单
const searchForm = reactive({
  empName: '',
  empNo: '',
  salaryMonth: '',
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')
const calculating = ref(false)

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 详情对话框
const detailVisible = ref(false)
const currentRow = ref({})

// 编辑对话框
const editVisible = ref(false)
const editFormRef = ref(null)
const editForm = reactive({
  id: null,
  empNo: '',
  empName: '',
  salaryMonth: '',
  baseSalary: 0,
  positionSalary: 0,
  overtimePay: 0,
  bonus: 0,
  subsidy: 0,
  attendanceDeduction: 0,
  socialSecurity: 0,
  tax: 0
})

const editRules = {
  baseSalary: [{ required: true, message: '请输入基本工资', trigger: 'blur' }],
  positionSalary: [{ required: true, message: '请输入岗位工资', trigger: 'blur' }]
}

const submitting = ref(false)

// 计算显示
const totalSalaryDisplay = computed(() => {
  const total = (editForm.baseSalary || 0) + (editForm.positionSalary || 0) +
                (editForm.overtimePay || 0) + (editForm.bonus || 0) + (editForm.subsidy || 0)
  return total.toFixed(2)
})

const actualSalaryDisplay = computed(() => {
  const total = (editForm.baseSalary || 0) + (editForm.positionSalary || 0) +
                (editForm.overtimePay || 0) + (editForm.bonus || 0) + (editForm.subsidy || 0)
  const deduction = (editForm.attendanceDeduction || 0) + (editForm.socialSecurity || 0) + (editForm.tax || 0)
  return (total - deduction).toFixed(2)
})

// 加载工资列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getSalaryList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载工资列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    empName: '',
    empNo: '',
    salaryMonth: '',
    status: null
  })
  handleSearch()
}

// 生成工资
const handleCalculate = () => {
  ElMessageBox.prompt('请输入要生成工资的月份（格式：YYYY-MM）', '生成工资', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d{4}-\d{2}$/,
    inputErrorMessage: '月份格式不正确'
  }).then(async ({ value }) => {
    calculating.value = true
    try {
      await calculateSalary({ salaryMonth: value })
      ElMessage.success('工资生成成功')
      loadTableData()
    } catch (error) {
      ElMessage.error(error.message || '生成失败')
    } finally {
      calculating.value = false
    }
  }).catch(() => {})
}

// 查看详情
const handleView = (row) => {
  currentRow.value = row
  detailVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(editForm, {
    id: row.id,
    empNo: row.empNo,
    empName: row.empName,
    salaryMonth: row.salaryMonth,
    baseSalary: row.baseSalary || 0,
    positionSalary: row.positionSalary || 0,
    overtimePay: row.overtimePay || 0,
    bonus: row.bonus || 0,
    subsidy: row.subsidy || 0,
    attendanceDeduction: row.attendanceDeduction || 0,
    socialSecurity: row.socialSecurity || 0,
    tax: row.tax || 0
  })
  editVisible.value = true
}

// 提交编辑
const handleSubmitEdit = async () => {
  await editFormRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      await updateSalary(editForm)
      ElMessage.success('调整成功')
      editVisible.value = false
      loadTableData()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 编辑对话框关闭
const handleEditClose = () => {
  editFormRef.value?.resetFields()
}

// 标记发放
const handlePay = (row) => {
  ElMessageBox.confirm(`确定要标记 ${row.empName} ${row.salaryMonth} 的工资为已发放吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await markSalaryPaid(row.id)
      ElMessage.success('标记成功')
      loadTableData()
    } catch (error) {
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
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
.salary-manage-container {
  padding: 20px;
}

.action-card {
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

.text-primary {
  color: #1890ff;
}

.text-success {
  color: #52c41a;
}

.text-danger {
  color: #ff4d4f;
}
</style>
