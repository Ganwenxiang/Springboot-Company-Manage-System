<template>
  <div class="leave-apply-container">
    <el-row :gutter="20">
      <!-- 左侧：请假申请表单 -->
      <el-col :span="14">
        <el-card shadow="never" class="form-card">
          <template #header>
            <span class="card-title">请假申请</span>
          </template>

          <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="100px"
          >
            <el-form-item label="请假类型" prop="leaveType">
              <el-radio-group v-model="formData.leaveType">
                <el-radio :value="1">年假</el-radio>
                <el-radio :value="2">事假</el-radio>
                <el-radio :value="3">病假</el-radio>
                <el-radio :value="4">调休</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="datetime"
                placeholder="选择开始时间"
                value-format="YYYY-MM-DD HH:mm"
                style="width: 100%"
                @change="calculateLeaveDays"
              />
            </el-form-item>

            <el-form-item label="结束时间" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="datetime"
                placeholder="选择结束时间"
                value-format="YYYY-MM-DD HH:mm"
                style="width: 100%"
                @change="calculateLeaveDays"
              />
            </el-form-item>

            <el-form-item label="请假天数">
              <el-input v-model="leaveDaysDisplay" disabled style="width: 200px">
                <template #suffix>天</template>
              </el-input>
            </el-form-item>

            <el-form-item label="请假原因" prop="reason">
              <el-input
                v-model="formData.reason"
                type="textarea"
                placeholder="请输入请假原因"
                :rows="4"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="submitting" size="large">
                <el-icon><Select /></el-icon>
                提交申请
              </el-button>
              <el-button @click="handleReset" size="large">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧：请假统计和说明 -->
      <el-col :span="10">
        <!-- 本月请假统计 -->
        <el-card shadow="never" class="stat-card">
          <template #header>
            <span class="card-title">本月请假统计</span>
          </template>

          <div class="stat-list">
            <div class="stat-item">
              <div class="stat-icon annual">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">年假</div>
                <div class="stat-value">{{ monthStats.annualLeave || 0 }}天</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon personal">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">事假</div>
                <div class="stat-value">{{ monthStats.personalLeave || 0 }}天</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon sick">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">病假</div>
                <div class="stat-value">{{ monthStats.sickLeave || 0 }}天</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon comp">
                <el-icon><Calendar /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">调休</div>
                <div class="stat-value">{{ monthStats.compTime || 0 }}天</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 请假说明 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <span class="card-title">请假说明</span>
          </template>

          <div class="info-list">
            <div class="info-item">
              <div class="info-title">年假</div>
              <div class="info-desc">根据入职年限享受，需提前申请</div>
            </div>
            <div class="info-item">
              <div class="info-title">事假</div>
              <div class="info-desc">因私事请假，需说明具体原因</div>
            </div>
            <div class="info-item">
              <div class="info-title">病假</div>
              <div class="info-desc">需提供医院证明或病假条</div>
            </div>
            <div class="info-item">
              <div class="info-title">调休</div>
              <div class="info-desc">使用加班时长调换，需有加班记录</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitLeave, getMyLeaveRequests } from '@/api/leave'
import dayjs from 'dayjs'

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  leaveType: 1,
  startDate: '',
  endDate: '',
  reason: '',
  leaveDays: 0
})

// 请假天数显示
const leaveDaysDisplay = computed(() => {
  return formData.leaveDays > 0 ? formData.leaveDays.toFixed(1) : '0.0'
})

// 表单验证规则
const formRules = {
  leaveType: [
    { required: true, message: '请选择请假类型', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入请假原因', trigger: 'blur' },
    { min: 5, message: '请假原因至少5个字符', trigger: 'blur' }
  ]
}

// 提交中
const submitting = ref(false)

// 本月请假统计
const monthStats = reactive({
  annualLeave: 0,
  personalLeave: 0,
  sickLeave: 0,
  compTime: 0
})

// 计算请假天数
const calculateLeaveDays = () => {
  if (formData.startDate && formData.endDate) {
    const start = dayjs(formData.startDate)
    const end = dayjs(formData.endDate)

    if (end.isBefore(start)) {
      ElMessage.warning('结束时间不能早于开始时间')
      formData.leaveDays = 0
      return
    }

    // 计算小时差
    const hours = end.diff(start, 'hour', true)
    // 转换为天数（8小时为1天）
    formData.leaveDays = Math.round((hours / 8) * 10) / 10
  } else {
    formData.leaveDays = 0
  }
}

// 加载本月请假统计
const loadMonthStats = async () => {
  try {
    const res = await getMyLeaveRequests()
    const records = res.data || []

    // 筛选本月的已批准记录
    const now = dayjs()
    const approvedRecords = records.filter(r => {
      return r.status === 1 && dayjs(r.startDate).format('YYYY-MM') === now.format('YYYY-MM')
    })

    monthStats.annualLeave = approvedRecords
      .filter(r => r.leaveType === 1)
      .reduce((sum, r) => sum + (r.leaveDays || 0), 0)

    monthStats.personalLeave = approvedRecords
      .filter(r => r.leaveType === 2)
      .reduce((sum, r) => sum + (r.leaveDays || 0), 0)

    monthStats.sickLeave = approvedRecords
      .filter(r => r.leaveType === 3)
      .reduce((sum, r) => sum + (r.leaveDays || 0), 0)

    monthStats.compTime = approvedRecords
      .filter(r => r.leaveType === 4)
      .reduce((sum, r) => sum + (r.leaveDays || 0), 0)
  } catch (error) {
    console.error('加载请假统计失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    if (formData.leaveDays <= 0) {
      ElMessage.warning('请选择有效的请假时间')
      return
    }

    submitting.value = true
    try {
      await submitLeave(formData)
      ElMessage.success('提交成功，请等待审批')
      handleReset()
      loadMonthStats()
    } catch (error) {
      ElMessage.error(error.message || '提交失败')
    } finally {
      submitting.value = false
    }
  })
}

// 重置表单
const handleReset = () => {
  formRef.value?.resetFields()
  formData.leaveDays = 0
}

onMounted(() => {
  loadMonthStats()
})
</script>

<style scoped lang="css">
.leave-apply-container {
  padding: 20px;
}

.form-card {
  min-height: 500px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-radius: 8px;
  background: #F5F7FA;
}

.stat-icon {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 20px;
  color: #fff;
}

.stat-icon.annual {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}

.stat-icon.personal {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
}

.stat-icon.sick {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  background: linear-gradient(135deg, #36cfc9 0%, #22a6a6 100%);
}

.stat-icon.comp {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  background: linear-gradient(135deg, #73d13d 0%, #52c41a 100%);
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.info-card {
  margin-bottom: 20px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  padding-bottom: 15px;
  border-bottom: 1px solid #EBEEF5;
}

.info-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.info-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.info-desc {
  font-size: 13px;
  color: #909399;
  line-height: 1.5;
}
</style>
