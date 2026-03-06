<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #ecf5ff;">
            <el-icon :size="32" color="#409EFF"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.totalEmps || 0 }}</div>
            <div class="stat-label">总员工数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #e1f3d8;">
            <el-icon :size="32" color="#67C23A"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.todayAttendance || 0 }}</div>
            <div class="stat-label">今日出勤</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #fef0f0;">
            <el-icon :size="32" color="#F56C6C"><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ overviewData.pendingLeave || 0 }}</div>
            <div class="stat-label">待审批请假</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon" style="background: #fdf6ec;">
            <el-icon :size="32" color="#E6A23C"><Timer /></el-icon>
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
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>员工部门分布</span>
            </div>
          </template>
          <div class="chart-container">
            <el-empty v-if="empDistribution.length === 0" description="暂无数据" />
            <div v-else class="dept-list">
              <div
                v-for="item in empDistribution"
                :key="item.deptId"
                class="dept-item"
              >
                <div class="dept-name">{{ item.deptName }}</div>
                <div class="dept-count">
                  <el-tag type="primary">{{ item.empCount }}人</el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item" @click="handleAction('attendance')">
              <el-icon :size="24" color="#409EFF"><Clock /></el-icon>
              <span>考勤打卡</span>
            </div>
            <div class="action-item" @click="handleAction('leave')">
              <el-icon :size="24" color="#67C23A"><Document /></el-icon>
              <span>请假申请</span>
            </div>
            <div class="action-item" @click="handleAction('overtime')">
              <el-icon :size="24" color="#E6A23C"><Timer /></el-icon>
              <span>加班申请</span>
            </div>
            <div class="action-item" @click="handleAction('salary')">
              <el-icon :size="24" color="#F56C6C"><Money /></el-icon>
              <span>我的薪资</span>
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
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }

  .stat-icon {
    width: 64px;
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 12px;
    margin-right: 16px;
  }

  .stat-content {
    flex: 1;

    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: var(--color-text-primary);
      line-height: 1;
      margin-bottom: 8px;
    }

    .stat-label {
      font-size: 14px;
      color: var(--color-text-secondary);
    }
  }
}

.charts-row {
  .chart-card {
    .card-header {
      font-size: 16px;
      font-weight: 500;
      color: var(--color-text-primary);
    }

    .chart-container {
      height: 280px;
    }
  }
}

.dept-list {
  .dept-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    background: var(--color-bg-secondary);
    border-radius: 8px;
    margin-bottom: 12px;

    &:last-child {
      margin-bottom: 0;
    }

    .dept-name {
      font-size: 15px;
      color: var(--color-text-primary);
    }
  }
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 10px;

  .action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 24px 16px;
    background: var(--color-bg-secondary);
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background: var(--color-primary-light-9);
      transform: translateY(-2px);
    }

    span {
      margin-top: 12px;
      font-size: 14px;
      color: var(--color-text-primary);
    }
  }
}
</style>
