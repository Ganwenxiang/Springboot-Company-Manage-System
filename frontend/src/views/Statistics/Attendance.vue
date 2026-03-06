<template>
  <div class="attendance-stats-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="日期类型">
          <el-radio-group v-model="searchForm.type" @change="handleTypeChange">
            <el-radio-button value="day">按日</el-radio-button>
            <el-radio-button value="week">按周</el-radio-button>
            <el-radio-button value="month">按月</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-if="searchForm.type === 'day'"
            v-model="searchForm.date"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 150px"
          />
          <el-date-picker
            v-else-if="searchForm.type === 'week'"
            v-model="searchForm.week"
            type="week"
            placeholder="选择周"
            format="YYYY 第 ww 周"
            value-format="YYYY-MM-DD"
            style="width: 180px"
          />
          <el-date-picker
            v-else
            v-model="searchForm.month"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card normal">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><CircleCheckFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">正常人次</div>
              <div class="stat-value">{{ stats.normalCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card late">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><WarningFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">迟到人次</div>
              <div class="stat-value">{{ stats.lateCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card early">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><WarningFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">早退人次</div>
              <div class="stat-value">{{ stats.earlyCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card absent">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><CircleCloseFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">缺勤人次</div>
              <div class="stat-value">{{ stats.absentCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 考勤记录表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <span class="card-title">考勤明细</span>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
        <el-table-column prop="empName" label="姓名" width="100" />
        <el-table-column prop="empNo" label="工号" width="120" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="attendanceDate" label="考勤日期" width="120" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAttendanceList } from '@/api/attendance'
import dayjs from 'dayjs'

// 搜索表单
const searchForm = reactive({
  type: 'month',
  date: dayjs().format('YYYY-MM-DD'),
  week: dayjs().format('YYYY-MM-DD'),
  month: dayjs().format('YYYY-MM')
})

// 统计数据
const stats = reactive({
  normalCount: 0,
  lateCount: 0,
  earlyCount: 0,
  absentCount: 0
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

// 加载考勤数据
const loadTableData = async () => {
  loading.value = true
  try {
    let startDate, endDate

    if (searchForm.type === 'day') {
      startDate = endDate = searchForm.date
    } else if (searchForm.type === 'week') {
      startDate = dayjs(searchForm.week).startOf('week').format('YYYY-MM-DD')
      endDate = dayjs(searchForm.week).endOf('week').format('YYYY-MM-DD')
    } else {
      startDate = dayjs(searchForm.month).startOf('month').format('YYYY-MM-DD')
      endDate = dayjs(searchForm.month).endOf('month').format('YYYY-MM-DD')
    }

    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      startDate,
      endDate
    }

    const res = await getAttendanceList(params)
    const pageInfo = res.data
    tableData.value = pageInfo.list || []
    pagination.total = pageInfo.total || 0

    // 统计各状态数量
    stats.normalCount = tableData.value.filter(r => r.status === 1).length
    stats.lateCount = tableData.value.filter(r => r.status === 2).length
    stats.earlyCount = tableData.value.filter(r => r.status === 3).length
    stats.absentCount = tableData.value.filter(r => r.status === 4).length
  } catch (error) {
    ElMessage.error('加载考勤数据失败')
  } finally {
    loading.value = false
  }
}

// 日期类型变化
const handleTypeChange = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
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
.attendance-stats-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.stats-row {
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

.stat-card.normal .stat-icon {
  background: linear-gradient(135deg, #67C23A 0%, #52c41a 100%);
}

.stat-card.late .stat-icon {
  background: linear-gradient(135deg, #E6A23C 0%, #d4a026 100%);
}

.stat-card.early .stat-icon {
  background: linear-gradient(135deg, #F0B429 0%, #cfa020 100%);
}

.stat-card.absent .stat-icon {
  background: linear-gradient(135deg, #F56C6C 0%, #cf1322 100%);
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.table-card {
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
