<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-blue">
          <div class="stat-icon">
            <el-icon :size="28"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.totalEmps || 0 }}</div>
            <div class="stat-label">总员工数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-green">
          <div class="stat-icon">
            <el-icon :size="28"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.todayAttendance || 0 }}</div>
            <div class="stat-label">今日出勤</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-red">
          <div class="stat-icon">
            <el-icon :size="28"><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.pendingLeave || 0 }}</div>
            <div class="stat-label">待审批请假</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="6">
        <div class="stat-card stat-card-orange">
          <div class="stat-icon">
            <el-icon :size="28"><Timer /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.pendingOvertime || 0 }}</div>
            <div class="stat-label">待审批加班</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header-title">
              <el-icon class="header-icon"><PieChart /></el-icon>
              员工部门分布
            </div>
          </template>
          <div class="chart-container">
            <el-empty v-if="empDistribution.length === 0" description="暂无数据" :image-size="80" />
            <div v-else class="dept-list">
              <div
                v-for="(item, index) in empDistribution"
                :key="item.deptId"
                class="dept-item"
              >
                <div class="dept-info">
                  <div class="dept-color" :style="{ background: colors[index % colors.length] }"></div>
                  <span class="dept-name">{{ item.deptName }}</span>
                </div>
                <div class="dept-count">
                  <span class="count-number">{{ item.empCount }}</span>
                  <span class="count-unit">人</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header-title">
              <el-icon class="header-icon"><Operation /></el-icon>
              快捷操作
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item" @click="handleAction('attendance')">
              <div class="action-icon action-icon-blue">
                <el-icon :size="24"><Clock /></el-icon>
              </div>
              <span class="action-text">考勤打卡</span>
            </div>
            <div class="action-item" @click="handleAction('leave')">
              <div class="action-icon action-icon-green">
                <el-icon :size="24"><Document /></el-icon>
              </div>
              <span class="action-text">请假申请</span>
            </div>
            <div class="action-item" @click="handleAction('overtime')">
              <div class="action-icon action-icon-orange">
                <el-icon :size="24"><Timer /></el-icon>
              </div>
              <span class="action-text">加班申请</span>
            </div>
            <div class="action-item" @click="handleAction('salary')">
              <div class="action-icon action-icon-purple">
                <el-icon :size="24"><Money /></el-icon>
              </div>
              <span class="action-text">我的薪资</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOverviewData, getEmpDistribution } from '@/api/statistics'

const router = useRouter()

const overviewData = ref({})
const empDistribution = ref([])

const colors = ['#5b8def', '#52c41a', '#faad14', '#ff4d4f', '#722ed1', '#13c2c2']

// 加载数据
const loadData = async () => {
  try {
    const [overviewRes, distributionRes] = await Promise.all([
      getOverviewData(),
      getEmpDistribution()
    ])
    overviewData.value = overviewRes.data
    empDistribution.value = distributionRes.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

// 快捷操作
const handleAction = (type) => {
  switch (type) {
    case 'attendance':
      router.push('/attendance/my')
      break
    case 'leave':
      router.push('/leave/apply')
      break
    case 'overtime':
      router.push('/overtime/apply')
      break
    case 'salary':
      router.push('/salary/my')
      break
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="css">
.dashboard-container {
  padding: 24px;
  min-height: calc(100vh - 60px);
}

/* 统计卡片 */
.stats-row {
  margin-bottom: 20px;
}

.stats-row .el-col {
  margin-bottom: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 24px;
  background: #fff;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
  height: 100%;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  margin-right: 16px;
  flex-shrink: 0;
}

.stat-card-blue .stat-icon {
  background: linear-gradient(135deg, #e8f4fd 0%, #d6e9fc 100%);
  color: #5b8def;
}

.stat-card-green .stat-icon {
  background: linear-gradient(135deg, #e8f7ed 0%, #d1f0db 100%);
  color: #52c41a;
}

.stat-card-red .stat-icon {
  background: linear-gradient(135deg, #fef1f2 0%, #fde3e5 100%);
  color: #ff4d4f;
}

.stat-card-orange .stat-icon {
  background: linear-gradient(135deg, #fff8eb 0%, #ffefcf 100%);
  color: #faad14;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-text-primary);
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: var(--color-text-secondary);
}

/* 图表卡片 */
.charts-row .el-col {
  margin-bottom: 20px;
}

.chart-card {
  height: 100%;
}

.card-header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.header-icon {
  color: var(--el-color-primary);
}

.chart-container {
  min-height: 280px;
}

/* 部门列表 */
.dept-list {
  padding: 8px 0;
}

.dept-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-sm);
  margin-bottom: 10px;
  transition: all 0.25s ease;
}

.dept-item:hover {
  background: #f0f5ff;
}

.dept-item:last-child {
  margin-bottom: 0;
}

.dept-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.dept-color {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.dept-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
}

.dept-count {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.count-number {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.count-unit {
  font-size: 13px;
  color: var(--color-text-secondary);
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 8px 0;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 28px 16px;
  background: var(--color-bg-secondary);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.25s ease;
}

.action-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.action-icon {
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  margin-bottom: 12px;
}

.action-icon-blue {
  background: linear-gradient(135deg, #5b8def 0%, #4a7de0 100%);
  color: #fff;
}

.action-icon-green {
  background: linear-gradient(135deg, #52c41a 0%, #3fad0a 100%);
  color: #fff;
}

.action-icon-orange {
  background: linear-gradient(135deg, #faad14 0%, #e89c0a 100%);
  color: #fff;
}

.action-icon-purple {
  background: linear-gradient(135deg, #722ed1 0%, #5e1db5 100%);
  color: #fff;
}

.action-text {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
}

/* 响应式 */
@media (max-width: 768px) {
  .quick-actions {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-card {
    padding: 20px;
  }

  .stat-value {
    font-size: 26px;
  }
}
</style>