<template>
  <div class="my-attendance-container">
    <!-- 今日考勤卡片 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" class="stat-card check-in-card">
          <div class="stat-content">
            <div class="stat-icon check-in">
              <el-icon :size="40"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">签到时间</div>
              <div class="stat-value">{{ todayAttendance.checkInTime || '--:--' }}</div>
              <div v-if="todayAttendance.checkInTime" class="stat-status" :class="getCheckInStatusClass(todayAttendance.checkInTime)">
                {{ getCheckInStatusText(todayAttendance.checkInTime) }}
              </div>
            </div>
          </div>
          <el-button
            v-if="!todayAttendance.checkInTime"
            type="primary"
            size="large"
            @click="handleCheckIn"
            :loading="checkInLoading"
            class="action-btn"
          >
            <el-icon><LocationFilled /></el-icon>
            立即签到
          </el-button>
          <el-tag v-else type="success" size="large" class="checked-tag">已签到</el-tag>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="never" class="stat-card check-out-card">
          <div class="stat-content">
            <div class="stat-icon check-out">
              <el-icon :size="40"><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">签退时间</div>
              <div class="stat-value">{{ todayAttendance.checkOutTime || '--:--' }}</div>
              <div v-if="todayAttendance.checkOutTime" class="stat-status" :class="getCheckOutStatusClass(todayAttendance.checkOutTime)">
                {{ getCheckOutStatusText(todayAttendance.checkOutTime) }}
              </div>
            </div>
          </div>
          <el-button
            v-if="todayAttendance.checkInTime && !todayAttendance.checkOutTime"
            type="warning"
            size="large"
            @click="handleCheckOut"
            :loading="checkOutLoading"
            class="action-btn"
          >
            <el-icon><LocationFilled /></el-icon>
            立即签退
          </el-button>
          <el-tag v-else-if="todayAttendance.checkOutTime" type="success" size="large" class="checked-tag">已签退</el-tag>
          <el-tag v-else type="info" size="large" class="checked-tag">请先签到</el-tag>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="never" class="stat-card hours-card">
          <div class="stat-content">
            <div class="stat-icon hours">
              <el-icon :size="40"><Timer /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">工作时长</div>
              <div class="stat-value">{{ todayAttendance.workHours || '0.0' }}小时</div>
              <div class="stat-desc">今日累计工作时长</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 本月考勤统计 -->
    <el-card shadow="never" class="month-stat-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">本月考勤统计</span>
          <span class="card-date">{{ currentMonth }}</span>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="6">
          <div class="month-stat-item normal">
            <div class="stat-icon-small">
              <el-icon><CircleCheckFilled /></el-icon>
            </div>
            <div class="stat-text">
              <div class="stat-label">正常</div>
              <div class="stat-count">{{ monthStats.normalDays || 0 }}天</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="month-stat-item late">
            <div class="stat-icon-small">
              <el-icon><WarningFilled /></el-icon>
            </div>
            <div class="stat-text">
              <div class="stat-label">迟到</div>
              <div class="stat-count">{{ monthStats.lateDays || 0 }}天</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="month-stat-item early">
            <div class="stat-icon-small">
              <el-icon><WarningFilled /></el-icon>
            </div>
            <div class="stat-text">
              <div class="stat-label">早退</div>
              <div class="stat-count">{{ monthStats.earlyDays || 0 }}天</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="month-stat-item absent">
            <div class="stat-icon-small">
              <el-icon><CircleCloseFilled /></el-icon>
            </div>
            <div class="stat-text">
              <div class="stat-label">缺勤</div>
              <div class="stat-count">{{ monthStats.absentDays || 0 }}天</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 本周考勤记录 -->
    <el-card shadow="never" class="record-card">
      <template #header>
        <span class="card-title">本周考勤记录</span>
      </template>

      <el-table
        :data="weekRecords"
        style="width: 100%"
        v-loading="loading"
        :empty-text="emptyText"
      >
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
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { checkIn, checkOut, getMyToday, getMyMonthly } from '@/api/attendance'
import dayjs from 'dayjs'

// 今日考勤数据
const todayAttendance = ref({})
const checkInLoading = ref(false)
const checkOutLoading = ref(false)

// 本月考勤统计
const monthStats = reactive({
  normalDays: 0,
  lateDays: 0,
  earlyDays: 0,
  absentDays: 0
})

// 本周考勤记录
const weekRecords = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')

// 当前月份
const currentMonth = computed(() => {
  return dayjs().format('YYYY年MM月')
})

// 加载今日考勤
const loadTodayAttendance = async () => {
  try {
    const res = await getMyToday()
    todayAttendance.value = res.data || {}
  } catch (error) {
    console.error('加载今日考勤失败:', error)
  }
}

// 加载本月考勤统计
const loadMonthStats = async () => {
  try {
    const year = dayjs().year()
    const month = dayjs().month() + 1 // dayjs month() is 0-indexed

    const res = await getMyMonthly({ year, month })
    const records = res.data || []

    // 统计各状态天数
    monthStats.normalDays = records.filter(r => r.status === 1).length
    monthStats.lateDays = records.filter(r => r.status === 2).length
    monthStats.earlyDays = records.filter(r => r.status === 3).length
    monthStats.absentDays = records.filter(r => r.status === 4).length

    // 获取本周记录
    const weekStart = dayjs().startOf('week').format('YYYY-MM-DD')
    const weekEnd = dayjs().endOf('week').format('YYYY-MM-DD')
    weekRecords.value = records.filter(r => {
      const date = dayjs(r.attendanceDate).format('YYYY-MM-DD')
      return date >= weekStart && date <= weekEnd
    })
  } catch (error) {
    console.error('加载本月考勤统计失败:', error)
  }
}

// 签到
const handleCheckIn = async () => {
  checkInLoading.value = true
  try {
    await checkIn({})
    ElMessage.success('签到成功')
    loadTodayAttendance()
    loadMonthStats()
  } catch (error) {
    ElMessage.error('签到失败')
  } finally {
    checkInLoading.value = false
  }
}

// 签退
const handleCheckOut = async () => {
  checkOutLoading.value = true
  try {
    await checkOut({})
    ElMessage.success('签退成功')
    loadTodayAttendance()
    loadMonthStats()
  } catch (error) {
    ElMessage.error('签退失败')
  } finally {
    checkOutLoading.value = false
  }
}

// 获取签到状态文本
const getCheckInStatusText = (time) => {
  const hour = parseInt(time.split(':')[0])
  const minute = parseInt(time.split(':')[1])
  if (hour < 9 || (hour === 9 && minute <= 0)) {
    return '正常'
  }
  return '迟到'
}

// 获取签到状态样式
const getCheckInStatusClass = (time) => {
  const status = getCheckInStatusText(time)
  return status === '正常' ? 'status-success' : 'status-warning'
}

// 获取签退状态文本
const getCheckOutStatusText = (time) => {
  const hour = parseInt(time.split(':')[0])
  const minute = parseInt(time.split(':')[1])
  if (hour > 18 || (hour === 18 && minute >= 0)) {
    return '正常'
  }
  return '早退'
}

// 获取签退状态样式
const getCheckOutStatusClass = (time) => {
  const status = getCheckOutStatusText(time)
  return status === '正常' ? 'status-success' : 'status-warning'
}

onMounted(() => {
  loadTodayAttendance()
  loadMonthStats()
})
</script>

<style scoped lang="css">
.my-attendance-container {
  padding: 20px;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.stat-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.stat-icon.check-in {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
}

.stat-icon.check-out {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: #fff;
}

.stat-icon.hours {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  background: linear-gradient(135deg, #36cfc9 0%, #22a6a6 100%);
  color: #fff;
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
  margin-bottom: 5px;
}

.stat-status {
  font-size: 14px;
}

.stat-status.status-success {
  color: #67C23A;
}

.stat-status.status-warning {
  color: #E6A23C;
}

.stat-desc {
  font-size: 12px;
  color: #909399;
}

.action-btn {
  width: 100%;
}

.checked-tag {
  width: 100%;
  text-align: center;
  font-size: 16px;
  padding: 12px 0;
}

.month-stat-card {
  margin-bottom: 20px;
}

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

.card-date {
  font-size: 14px;
  color: #909399;
}

.month-stat-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  background: #F5F7FA;
}

.month-stat-item.normal {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
}

.month-stat-item.late {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
}

.month-stat-item.early {
  background: linear-gradient(135deg, #fff9c4 0%, #fff59d 100%);
}

.month-stat-item.absent {
  background: linear-gradient(135deg, #ffebee 0%, #ffcdd2 100%);
}

.stat-icon-small {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
}

.month-stat-item.normal .stat-icon-small {
  background: #67C23A;
  color: #fff;
}

.month-stat-item.late .stat-icon-small {
  background: #E6A23C;
  color: #fff;
}

.month-stat-item.early .stat-icon-small {
  background: #F0B429;
  color: #fff;
}

.month-stat-item.absent .stat-icon-small {
  background: #F56C6C;
  color: #fff;
}

.stat-text {
  flex: 1;
}

.stat-count {
  font-size: 24px;
  font-weight: 600;
  margin-top: 5px;
}

.record-card {
  margin-bottom: 20px;
}
</style>
