<template>
  <div class="overtime-list-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
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
            加班申请
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
      title="加班详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
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
        <el-descriptions-item label="审批人">{{ currentRow.approverName || '--' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ currentRow.approveTime || '--' }}</el-descriptions-item>
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyOvertimeRecords, deleteOvertime } from '@/api/overtime'

const router = useRouter()

// 日期范围
const dateRange = ref([])

// 搜索表单
const searchForm = reactive({
  overtimeType: null,
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

// 加载加班列表
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await getMyOvertimeRecords()
    let records = res.data || []

    // 客户端筛选
    if (searchForm.overtimeType !== null) {
      records = records.filter(r => r.overtimeType === searchForm.overtimeType)
    }
    if (searchForm.status !== null) {
      records = records.filter(r => r.status === searchForm.status)
    }
    if (searchForm.startDate && searchForm.endDate) {
      records = records.filter(r => {
        return r.overtimeDate >= searchForm.startDate && r.overtimeDate <= searchForm.endDate
      })
    }

    // 按申请时间倒序
    records.sort((a, b) => b.createTime?.localeCompare(a.createTime || '') || 0)

    pagination.total = records.length
    const start = (pagination.pageNum - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    tableData.value = records.slice(start, end)
  } catch (error) {
    ElMessage.error('加载加班记录失败')
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
    overtimeType: null,
    status: null,
    startDate: '',
    endDate: ''
  })
  handleSearch()
}

// 加班申请
const handleApply = () => {
  router.push('/overtime/apply')
}

// 查看详情
const handleView = (row) => {
  currentRow.value = row
  detailVisible.value = true
}

// 撤销申请
const handleCancel = (row) => {
  ElMessageBox.confirm('确定要撤销该加班申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteOvertime(row.id)
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
.overtime-list-container {
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
