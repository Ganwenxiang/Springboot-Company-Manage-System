<template>
  <div class="overtime-approve-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card pending">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ pendingCount }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card approved">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Select /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ approvedCount }}</div>
              <div class="stat-label">已批准</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card rejected">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Close /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ rejectedCount }}</div>
              <div class="stat-label">已拒绝</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card total">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalCount }}</div>
              <div class="stat-label">总计</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="员工姓名">
          <el-input v-model="searchForm.empName" placeholder="请输入员工姓名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="加班类型">
          <el-select v-model="searchForm.overtimeType" placeholder="全部" clearable style="width: 150px">
            <el-option label="工作日" :value="1" />
            <el-option label="周末" :value="2" />
            <el-option label="节假日" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="待审批" :value="0" />
            <el-option label="已批准" :value="1" />
            <el-option label="已拒绝" :value="2" />
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
          <span class="card-title">加班审批列表</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
        <el-table-column prop="empName" label="申请人" width="100" />
        <el-table-column prop="empNo" label="工号" width="120" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="overtimeDate" label="加班日期" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="100" />
        <el-table-column prop="endTime" label="结束时间" width="100" />
        <el-table-column prop="overtimeHours" label="加班时长" width="100" align="center">
          <template #default="{ row }">
            {{ row.overtimeHours }}小时
          </template>
        </el-table-column>
        <el-table-column prop="overtimeType" label="加班类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.overtimeType === 1" type="primary">工作日</el-tag>
            <el-tag v-else-if="row.overtimeType === 2" type="warning">周末</el-tag>
            <el-tag v-else-if="row.overtimeType === 3" type="success">节假日</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="加班原因" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审批</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已批准</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              查看
            </el-button>
            <el-button v-if="row.status === 0" link type="success" @click="handleApprove(row)">
              批准
            </el-button>
            <el-button v-if="row.status === 0" link type="danger" @click="handleReject(row)">
              拒绝
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

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="加班详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请人">{{ currentRow.empName }}</el-descriptions-item>
        <el-descriptions-item label="工号">{{ currentRow.empNo }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ currentRow.deptName }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ currentRow.jobTitle || '--' }}</el-descriptions-item>
        <el-descriptions-item label="加班日期">{{ currentRow.overtimeDate }}</el-descriptions-item>
        <el-descriptions-item label="加班时长">{{ currentRow.overtimeHours }}小时</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ currentRow.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ currentRow.endTime }}</el-descriptions-item>
        <el-descriptions-item label="加班类型">
          <el-tag v-if="currentRow.overtimeType === 1" type="primary">工作日</el-tag>
          <el-tag v-else-if="currentRow.overtimeType === 2" type="warning">周末</el-tag>
          <el-tag v-else-if="currentRow.overtimeType === 3" type="success">节假日</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag v-if="currentRow.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="currentRow.status === 1" type="success">已批准</el-tag>
          <el-tag v-else-if="currentRow.status === 2" type="danger">已拒绝</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="加班原因" :span="2">{{ currentRow.reason }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ currentRow.createTime || '--' }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.approverName" label="审批人">{{ currentRow.approverName }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.approveTime" label="审批时间">{{ currentRow.approveTime }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.approveRemark" label="审批备注" :span="2">
          {{ currentRow.approveRemark }}
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      v-model="approveVisible"
      :title="approveTitle"
      width="600px"
    >
      <el-form
        ref="approveFormRef"
        :model="approveForm"
        :rules="approveRules"
        label-width="100px"
      >
        <el-form-item label="审批类型">
          <el-tag v-if="approveForm.approveType === 1" type="success">批准</el-tag>
          <el-tag v-else type="danger">拒绝</el-tag>
        </el-form-item>
        <el-form-item label="申请人">
          {{ currentRow.empName }} ({{ currentRow.empNo }})
        </el-form-item>
        <el-form-item label="加班日期">
          {{ currentRow.overtimeDate }}
        </el-form-item>
        <el-form-item label="加班时间">
          {{ currentRow.startTime }} - {{ currentRow.endTime }} (共{{ currentRow.overtimeHours }}小时)
        </el-form-item>
        <el-form-item label="加班类型">
          <el-tag v-if="currentRow.overtimeType === 1" type="primary">工作日</el-tag>
          <el-tag v-else-if="currentRow.overtimeType === 2" type="warning">周末</el-tag>
          <el-tag v-else-if="currentRow.overtimeType === 3" type="success">节假日</el-tag>
        </el-form-item>
        <el-form-item label="加班原因">
          {{ currentRow.reason }}
        </el-form-item>
        <el-form-item label="审批备注" prop="approveRemark">
          <el-input
            v-model="approveForm.approveRemark"
            type="textarea"
            placeholder="请输入审批备注（选填）"
            :rows="3"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button :type="approveForm.approveType === 1 ? 'success' : 'danger'" @click="handleSubmitApprove" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOvertimeList, approveOvertime } from '@/api/overtime'

// 统计数据
const pendingCount = ref(0)
const approvedCount = ref(0)
const rejectedCount = ref(0)
const totalCount = computed(() => pendingCount.value + approvedCount.value + rejectedCount.value)

// 搜索表单
const searchForm = reactive({
  empName: '',
  overtimeType: null,
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

// 详情对话框
const detailVisible = ref(false)
const currentRow = ref({})

// 审批对话框
const approveVisible = ref(false)
const approveTitle = computed(() => approveForm.approveType === 1 ? '批准申请' : '拒绝申请')
const approveFormRef = ref(null)
const approveForm = reactive({
  overtimeId: null,
  approveType: 1,
  approveRemark: ''
})
const approveRules = {
  approveRemark: [
    { max: 200, message: '审批备注不能超过200个字符', trigger: 'blur' }
  ]
}
const submitting = ref(false)

// 加载加班列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getOvertimeList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0

    updateStats()
  } catch (error) {
    ElMessage.error('加载加班列表失败')
  } finally {
    loading.value = false
  }
}

// 更新统计数据
const updateStats = async () => {
  try {
    const pendingRes = await getOvertimeList({ status: 0, pageSize: 1 })
    pendingCount.value = pendingRes.data.total || 0

    const approvedRes = await getOvertimeList({ status: 1, pageSize: 1 })
    approvedCount.value = approvedRes.data.total || 0

    const rejectedRes = await getOvertimeList({ status: 2, pageSize: 1 })
    rejectedCount.value = rejectedRes.data.total || 0
  } catch (error) {
    console.error('加载统计数据失败:', error)
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
    overtimeType: null,
    status: null
  })
  handleSearch()
}

// 查看详情
const handleView = (row) => {
  currentRow.value = row
  detailVisible.value = true
}

// 批准
const handleApprove = (row) => {
  currentRow.value = row
  approveForm.overtimeId = row.id
  approveForm.approveType = 1
  approveForm.approveRemark = ''
  approveVisible.value = true
}

// 拒绝
const handleReject = (row) => {
  currentRow.value = row
  approveForm.overtimeId = row.id
  approveForm.approveType = 2
  approveForm.approveRemark = ''
  approveVisible.value = true
}

// 提交审批
const handleSubmitApprove = async () => {
  await approveFormRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      await approveOvertime({
        overtimeId: approveForm.overtimeId,
        status: approveForm.approveType === 1 ? 1 : 2,
        approveRemark: approveForm.approveRemark
      })
      ElMessage.success(approveForm.approveType === 1 ? '已批准' : '已拒绝')
      approveVisible.value = false
      loadTableData()
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      submitting.value = false
    }
  })
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
.overtime-approve-container {
  padding: 20px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 28px;
  color: #fff;
}

.stat-card.pending .stat-icon {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}

.stat-card.approved .stat-icon {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.stat-card.rejected .stat-icon {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
}

.stat-card.total .stat-icon {
  background: linear-gradient(135deg, #8c8c8c 0%, #595959 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
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
