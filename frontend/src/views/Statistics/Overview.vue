<template>
  <div class="statistics-overview-container">
    <!-- 日期选择 -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="searchForm" class="filter-form">
        <el-form-item label="统计类型">
          <el-radio-group v-model="searchForm.type" @change="handleTypeChange">
            <el-radio-button value="month">按月</el-radio-button>
            <el-radio-button value="quarter">按季度</el-radio-button>
            <el-radio-button value="year">按年</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="统计时间">
          <el-date-picker
            v-if="searchForm.type === 'month'"
            v-model="searchForm.date"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 150px"
            @change="loadData"
          />
          <el-date-picker
            v-else-if="searchForm.type === 'quarter'"
            v-model="searchForm.date"
            type="month"
            placeholder="选择季度"
            value-format="YYYY-MM"
            style="width: 150px"
            @change="loadData"
          />
          <el-date-picker
            v-else
            v-model="searchForm.date"
            type="year"
            placeholder="选择年份"
            value-format="YYYY"
            style="width: 150px"
            @change="loadData"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon employees">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statsData.totalEmps || 0 }}</div>
              <div class="stat-label">在职员工</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon attendance">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statsData.attendanceRate || 0 }}%</div>
              <div class="stat-label">出勤率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon leave">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statsData.totalLeaveDays || 0 }}</div>
              <div class="stat-label">请假天数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon overtime">
              <el-icon><Timer /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statsData.totalOvertimeHours || 0 }}</div>
              <div class="stat-label">加班时长</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 员工部门分布 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span class="card-title">员工部门分布</span>
          </template>
          <div class="chart-container">
            <el-empty v-if="empDistribution.length === 0" description="暂无数据" :image-size="100" />
            <div v-else class="dept-distribution">
              <div
                v-for="item in empDistribution"
                :key="item.deptId"
                class="dept-item"
              >
                <div class="dept-info">
                  <span class="dept-name">{{ item.deptName }}</span>
                  <span class="dept-count">{{ item.empCount }}人</span>
                </div>
                <div class="dept-bar">
                  <div class="dept-bar-fill" :style="{ width: getPercentage(item.empCount, totalEmps) + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 部门考勤对比 -->
      <el-col :span="12">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span class="card-title">部门考勤对比</span>
          </template>
          <div class="chart-container">
            <el-empty v-if="deptAttendance.length === 0" description="暂无数据" :image-size="100" />
            <div v-else class="attendance-chart">
              <div
                v-for="item in deptAttendance"
                :key="item.deptId"
                class="attendance-item"
              >
                <div class="attendance-label">{{ item.deptName }}</div>
                <div class="attendance-bars">
                  <div class="bar-wrapper">
                    <div class="bar-label">正常</div>
                    <div class="bar-container">
                      <div class="bar bar-normal" :style="{ width: getAttendancePercentage(item.normalCount, item.totalCount) + '%' }"></div>
                      <span class="bar-value">{{ item.normalCount }}</span>
                    </div>
                  </div>
                  <div class="bar-wrapper">
                    <div class="bar-label">异常</div>
                    <div class="bar-container">
                      <div class="bar bar-abnormal" :style="{ width: getAttendancePercentage(item.abnormalCount, item.totalCount) + '%' }"></div>
                      <span class="bar-value">{{ item.abnormalCount }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <!-- 月度考勤汇总 -->
      <el-col :span="24">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span class="card-title">月度考勤汇总</span>
          </template>
          <div class="chart-container">
            <el-empty v-if="attendanceSummary.length === 0" description="暂无数据" :image-size="100" />
            <el-table v-else :data="attendanceSummary" style="width: 100%" :border="true">
              <el-table-column prop="deptName" label="部门" width="150" />
              <el-table-column label="在职人数" width="100" align="center">
                <template #default="{ row }">
                  {{ row.totalEmps || 0 }}
                </template>
              </el-table-column>
              <el-table-column prop="normalDays" label="正常天数" width="100" align="center" />
              <el-table-column prop="lateDays" label="迟到天数" width="100" align="center" />
              <el-table-column prop="earlyDays" label="早退天数" width="100" align="center" />
              <el-table-column prop="absentDays" label="缺勤天数" width="100" align="center" />
              <el-table-column label="出勤率" width="120" align="center">
                <template #default="{ row }">
                  <el-tag :type="getAttendanceRateType(row.attendanceRate)">{{ row.attendanceRate || 0 }}%</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOverviewData, getEmpDistribution, getAttendanceSummary, getDeptAttendance } from '@/api/statistics'

// 搜索表单
const searchForm = reactive({
  type: 'month',
  date: new Date().toISOString().slice(0, 7) // YYYY-MM
})

// 统计数据
const statsData = reactive({
  totalEmps: 0,
  attendanceRate: 0,
  totalLeaveDays: 0,
  totalOvertimeHours: 0
})

// 数据列表
const empDistribution = ref([])
const deptAttendance = ref([])
const attendanceSummary = ref([])

// 总员工数（用于计算百分比）
const totalEmps = computed(() => {
  return empDistribution.value.reduce((sum, item) => sum + (item.empCount || 0), 0)
})

// 加载所有数据
const loadData = async () => {
  try {
    // 加载概览数据
    const overviewRes = await getOverviewData()
    Object.assign(statsData, overviewRes.data || {})

    // 加载员工分布
    const distRes = await getEmpDistribution()
    empDistribution.value = distRes.data || []

    // 根据选择的日期加载考勤数据
    if (searchForm.type === 'month') {
      const [summaryRes, deptRes] = await Promise.all([
        getAttendanceSummary({ startDate: searchForm.date + '-01', endDate: getLastDayOfMonth(searchForm.date) }),
        getDeptAttendance({ month: searchForm.date })
      ])
      attendanceSummary.value = summaryRes.data || []
      deptAttendance.value = deptRes.data || []
    } else if (searchForm.type === 'quarter') {
      const quarter = getQuarterRange(searchForm.date)
      const quarterMonth = quarter.startDate.substring(0, 7) // YYYY-MM
      const [summaryRes, deptRes] = await Promise.all([
        getAttendanceSummary({ startDate: quarter.start, endDate: quarter.end }),
        getDeptAttendance({ month: quarterMonth })
      ])
      attendanceSummary.value = summaryRes.data || []
      deptAttendance.value = deptRes.data || []
    } else {
      const year = searchForm.date
      const [summaryRes, deptRes] = await Promise.all([
        getAttendanceSummary({ startDate: `${year}-01-01`, endDate: `${year}-12-31` }),
        getDeptAttendance({ month: `${year}-01` })
      ])
      attendanceSummary.value = summaryRes.data || []
      deptAttendance.value = deptRes.data || []
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

// 获取月份最后一天
const getLastDayOfMonth = (dateStr) => {
  const [year, month] = dateStr.split('-').map(Number)
  const lastDay = new Date(year, month, 0).getDate()
  return `${year}-${String(month).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`
}

// 获取季度范围
const getQuarterRange = (dateStr) => {
  const [year, month] = dateStr.split('-').map(Number)
  const quarter = Math.ceil(month / 3)
  const startMonth = (quarter - 1) * 3 + 1
  const endMonth = quarter * 3
  const startDate = `${year}-${String(startMonth).padStart(2, '0')}-01`
  const lastDay = new Date(year, endMonth, 0).getDate()
  const endDate = `${year}-${String(endMonth).padStart(2, '0')}-${lastDay}`
  return { startDate, endDate }
}

// 统计类型变化
const handleTypeChange = () => {
  // 重置日期为当前选择类型的当前日期
  const now = new Date()
  if (searchForm.type === 'month') {
    searchForm.date = now.toISOString().slice(0, 7)
  } else if (searchForm.type === 'quarter') {
    searchForm.date = now.toISOString().slice(0, 7)
  } else {
    searchForm.date = now.getFullYear().toString()
  }
  loadData()
}

// 计算百分比
const getPercentage = (value, total) => {
  if (total === 0) return 0
  return Math.round((value / total) * 100)
}

// 计算考勤百分比
const getAttendancePercentage = (value, total) => {
  if (total === 0) return 0
  return Math.round((value / total) * 100)
}

// 获取出勤率标签类型
const getAttendanceRateType = (rate) => {
  if (rate >= 95) return 'success'
  if (rate >= 90) return ''
  if (rate >= 80) return 'warning'
  return 'danger'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="css">
.statistics-overview-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form .el-form-item {
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
  margin-right: 15px;
  font-size: 24px;
  color: #fff;
}

.stat-icon.employees {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}

.stat-icon.attendance {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.stat-icon.leave {
  background: linear-gradient(135deg, #faad14 0%, #f57b2a 100%);
}

.stat-icon.overtime {
  background: linear-gradient(135deg, #722ed1 0%, #531dab 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.chart-container {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 员工部门分布 */
.dept-distribution {
  width: 100%;
  padding: 10px 0;
}

.dept-item {
  margin-bottom: 20px;
}

.dept-item:last-child {
  margin-bottom: 0;
}

.dept-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.dept-name {
  font-size: 15px;
  color: #303133;
  font-weight: 500;
}

.dept-count {
  font-size: 14px;
  color: #606266;
}

.dept-bar {
  height: 24px;
  background: #EBEEF5;
  border-radius: 12px;
  overflow: hidden;
}

.dept-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #1890ff 0%, #096dd9 100%);
  border-radius: 12px;
  transition: width 0.5s ease;
}

/* 部门考勤对比 */
.attendance-chart {
  width: 100%;
  padding: 10px 0;
}

.attendance-item {
  margin-bottom: 20px;
}

.attendance-item:last-child {
  margin-bottom: 0;
}

.attendance-label {
  font-size: 14px;
  color: #303133;
  margin-bottom: 10px;
  font-weight: 500;
}

.attendance-bars {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.bar-wrapper {
  display: flex;
  align-items: center;
}

.bar-label {
  width: 60px;
  font-size: 12px;
  color: #909399;
}

.bar-container {
  flex: 1;
  height: 20px;
  background: #EBEEF5;
  border-radius: 10px;
  position: relative;
}

.bar {
  height: 100%;
  border-radius: 10px;
  transition: width 0.5s ease;
}

.bar-normal {
  background: linear-gradient(90deg, #52c41a 0%, #389e0d 100%);
}

.bar-abnormal {
  background: linear-gradient(90deg, #ff4d4f 0%, #cf1322 100%);
}

.bar-value {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 12px;
  color: #606266;
  font-weight: 500;
}
</style>
