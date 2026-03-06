<template>
  <div class="leave-list-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="请假类型">
          <el-select v-model="searchForm.leaveType" placeholder="全部" clearable style="width: 150px">
            <el-option label="年假" :value="1" />
            <el-option label="事假" :value="2" />
            <el-option label="病假" :value="3" />
            <el-option label="调休" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 150px">
            <el-option label="待审批" :value="0" />
            <el-option label="已批准" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已撤销" :value="3" />
          </el-select>
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
          <el-button type="primary" @click="handleApply">
            <el-icon><Plus /></el-icon>
            请假申请
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
        <el-table-column prop="leaveType" label="请假类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.leaveType === 1" type="primary">年假</el-tag>
            <el-tag v-else-if="row.leaveType === 2" type="warning">事假</el-tag>
            <el-tag v-else-if="row.leaveType === 3" type="success">病假</el-tag>
            <el-tag v-else-if="row.leaveType === 4" type="info">调休</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始时间" width="160" />
        <el-table-column prop="endDate" label="结束时间" width="160" />
        <el-table-column prop="leaveDays" label="请假天数" width="100" align="center">
          <template #default="{ row }">
            {{ row.leaveDays }}天
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="请假原因" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审批</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已批准</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="row.status === 3" type="info">已撤销</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approveTime" label="审批时间" width="160">
          <template #default="{ row }">
            {{ row.approveTime || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              查看
            </el-button>
            <el-button v-if="row.status === 0" link type="danger" @click="handleCancel(row)">
              撤销
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, prev, pager, next"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="请假详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="请假类型">
          <el-tag v-if="currentRow.leaveType === 1" type="primary">年假</el-tag>
          <el-tag v-else-if="currentRow.leaveType === 2" type="warning">事假</el-tag>
          <el-tag v-else-if="currentRow.leaveType === 3" type="success">病假</el-tag>
          <el-tag v-else-if="currentRow.leaveType === 4" type="info">调休</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请假天数">{{ currentRow.leaveDays }}天</el-descriptions-item>
        <el-descriptions-item label="开始时间" :span="2">{{ currentRow.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束时间" :span="2">{{ currentRow.endDate }}</el-descriptions-item>
        <el-descriptions-item label="请假原因" :span="2">{{ currentRow.reason }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag v-if="currentRow.status === 0" type="warning">待审批</el-tag>
          <el-tag v-else-if="currentRow.status === 1" type="success">已批准</el-tag>
          <el-tag v-else-if="currentRow.status === 2" type="danger">已拒绝</el-tag>
          <el-tag v-else-if="currentRow.status === 3" type="info">已撤销</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批人">{{ currentRow.approverName || '--' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间" :span="2">{{ currentRow.approveTime || '--' }}</el-descriptions-item>
        <el-descriptions-item v-if="currentRow.approveRemark" label="审批备注" :span="2">
          {{ currentRow.approveRemark }}
        </el-descriptions-item>
      </el-descriptions>

      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyLeaveRequests, cancelLeave } from '@/api/leave'
import dayjs from 'dayjs'

const router = useRouter()

// 日期范围
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  leaveType: null,
  status: null,
  startDate: '',
  endDate: ''
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

// 加载请假列表
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await getMyLeaveRequests()
    let records = res.data || []

    // 客户端筛选
    if (searchForm.leaveType !== null) {
      records = records.filter(r => r.leaveType === searchForm.leaveType)
    }
    if (searchForm.status !== null) {
      records = records.filter(r => r.status === searchForm.status)
    }
    if (searchForm.startDate && searchForm.endDate) {
      records = records.filter(r => {
        return r.startDate >= searchForm.startDate && r.endDate <= searchForm.endDate
      })
    }

    // 按申请时间倒序
    records.sort((a, b) => b.createTime?.localeCompare(a.createTime || '') || 0)

    pagination.total = records.length
    const start = (pagination.pageNum - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    tableData.value = records.slice(start, end)
  } catch (error) {
    ElMessage.error('加载请假记录失败')
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
    leaveType: null,
    status: null,
    startDate: '',
    endDate: ''
  })
  handleSearch()
}

// 请假申请
const handleApply = () => {
  router.push('/leave/apply')
}

// 查看详情
const handleView = (row) => {
  currentRow.value = row
  detailVisible.value = true
}

// 撤销申请
const handleCancel = (row) => {
  ElMessageBox.confirm('确定要撤销该请假申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelLeave(row.id)
      ElMessage.success('撤销成功')
      loadTableData()
    } catch (error) {
      ElMessage.error('撤销失败')
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
.leave-list-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.table-card {
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
