<template>
  <div class="salary-list-container">
    <!-- 年度汇总卡片 -->
    <el-row :gutter="20" class="summary-row">
      <el-col :span="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-content">
            <div class="summary-icon base">
              <el-icon><Wallet /></el-icon>
            </div>
            <div class="summary-info">
              <div class="summary-label">年度基本工资</div>
              <div class="summary-value">¥{{ yearSummary.baseSalary || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-content">
            <div class="summary-icon bonus">
              <el-icon><Present /></el-icon>
            </div>
            <div class="summary-info">
              <div class="summary-label">年度奖金补贴</div>
              <div class="summary-value">¥{{ yearSummary.bonus || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-content">
            <div class="summary-icon deduction">
              <el-icon><Minus /></el-icon>
            </div>
            <div class="summary-info">
              <div class="summary-label">年度扣除</div>
              <div class="summary-value">¥{{ yearSummary.deduction || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-content">
            <div class="summary-icon actual">
              <el-icon><Money /></el-icon>
            </div>
            <div class="summary-info">
              <div class="summary-label">年度实发</div>
              <div class="summary-value">¥{{ yearSummary.actualSalary || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="年份">
          <el-date-picker
            v-model="searchForm.year"
            type="year"
            placeholder="选择年份"
            value-format="YYYY"
            style="width: 150px"
            @change="handleYearChange"
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
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">工资明细</span>
        </div>
      </template>

      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
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
        <el-table-column prop="overtimePay" label="加班费" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.overtimePay?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="bonus" label="奖金" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.bonus?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="subsidy" label="补贴" width="100" align="right">
          <template #default="{ row }">
            ¥{{ row.subsidy?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column prop="attendanceDeduction" label="考勤扣款" width="100" align="right">
          <template #default="{ row }">
            <span class="text-danger">-¥{{ row.attendanceDeduction?.toFixed(2) || '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="socialSecurity" label="社保" width="100" align="right">
          <template #default="{ row }">
            <span class="text-danger">-¥{{ row.socialSecurity?.toFixed(2) || '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="tax" label="个税" width="100" align="right">
          <template #default="{ row }">
            <span class="text-danger">-¥{{ row.tax?.toFixed(2) || '0.00' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalSalary" label="应发工资" width="120" align="right">
          <template #default="{ row }">
            <span class="text-primary">¥{{ row.totalSalary?.toFixed(2) || '0.00' }}</span>
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
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              详情
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="工资详情"
      width="700px"
    >
      <el-descriptions :column="2" border>
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
        <el-button v-if="currentRow.status === 0" type="primary" disabled>
          暂未发放
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMySalaries } from '@/api/salary'

// 年度汇总
const yearSummary = reactive({
  baseSalary: 0,
  bonus: 0,
  deduction: 0,
  actualSalary: 0
})

// 搜索表单
const searchForm = reactive({
  year: new Date().getFullYear().toString(),
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

// 加载工资列表
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await getMySalaries()
    let records = res.data || []

    // 筛选选中年份的记录
    if (searchForm.year) {
      records = records.filter(r => r.salaryMonth && r.salaryMonth.startsWith(searchForm.year))
    }

    // 按状态筛选
    if (searchForm.status !== null) {
      records = records.filter(r => r.status === searchForm.status)
    }

    // 按月份倒序
    records.sort((a, b) => b.salaryMonth.localeCompare(a.salaryMonth))

    // 更新年度汇总
    updateYearSummary(records)

    pagination.total = records.length
    const start = (pagination.pageNum - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    tableData.value = records.slice(start, end)
  } catch (error) {
    ElMessage.error('加载工资列表失败')
  } finally {
    loading.value = false
  }
}

// 更新年度汇总
const updateYearSummary = (records) => {
  yearSummary.baseSalary = records.reduce((sum, r) => sum + (r.baseSalary || 0) + (r.positionSalary || 0), 0).toFixed(2)
  yearSummary.bonus = records.reduce((sum, r) => sum + (r.bonus || 0) + (r.subsidy || 0) + (r.overtimePay || 0), 0).toFixed(2)
  yearSummary.deduction = records.reduce((sum, r) => sum + (r.attendanceDeduction || 0) + (r.socialSecurity || 0) + (r.tax || 0), 0).toFixed(2)
  yearSummary.actualSalary = records.reduce((sum, r) => sum + (r.actualSalary || 0), 0).toFixed(2)
}

// 年份变化
const handleYearChange = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  searchForm.year = new Date().getFullYear().toString()
  searchForm.status = null
  handleSearch()
}

// 查看详情
const handleView = (row) => {
  currentRow.value = row
  detailVisible.value = true
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
.salary-list-container {
  padding: 20px;
}

.summary-row {
  margin-bottom: 20px;
}

.summary-card {
  cursor: pointer;
  transition: all 0.3s;
}

.summary-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.summary-content {
  display: flex;
  align-items: center;
}

.summary-icon {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: #fff;
}

.summary-icon.base {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}

.summary-icon.bonus {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.summary-icon.deduction {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
}

.summary-icon.actual {
  background: linear-gradient(135deg, #722ed1 0%, #531dab 100%);
}

.summary-info {
  flex: 1;
}

.summary-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.summary-value {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
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
