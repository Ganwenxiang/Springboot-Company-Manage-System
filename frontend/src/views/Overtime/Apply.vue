<template>
  <div class="overtime-apply-container">
    <el-row :gutter="20">
      <!-- 左侧：加班申请表单 -->
      <el-col :span="14">
        <el-card shadow="never" class="form-card">
          <template #header>
            <span class="card-title">加班申请</span>
          </template>

          <el-form
            ref="formRef"
            :model="formData"
            :rules="formRules"
            label-width="100px"
          >
            <el-form-item label="加班日期" prop="overtimeDate">
              <el-date-picker
                v-model="formData.overtimeDate"
                type="date"
                placeholder="选择加班日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime">
                  <el-time-picker
                    v-model="formData.startTime"
                    format="HH:mm"
                    value-format="HH:mm"
                    placeholder="选择开始时间"
                    style="width: 100%"
                    @change="calculateOvertimeHours"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结束时间" prop="endTime">
                  <el-time-picker
                    v-model="formData.endTime"
                    format="HH:mm"
                    value-format="HH:mm"
                    placeholder="选择结束时间"
                    style="width: 100%"
                    @change="calculateOvertimeHours"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="加班时长">
              <el-input v-model="overtimeHoursDisplay" disabled style="width: 200px">
                <template #suffix>小时</template>
              </el-input>
            </el-form-item>

            <el-form-item label="加班类型" prop="overtimeType">
              <el-radio-group v-model="formData.overtimeType">
                <el-radio :value="1">工作日</el-radio>
                <el-radio :value="2">周末</el-radio>
                <el-radio :value="3">节假日</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="加班原因" prop="reason">
              <el-input
                v-model="formData.reason"
                type="textarea"
                placeholder="请输入加班原因"
                :rows="4"
                maxlength="200"
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

      <!-- 右侧：加班统计和说明 -->
      <el-col :span="10">
        <!-- 本月加班统计 -->
        <el-card shadow="never" class="stat-card">
          <template #header>
            <span class="card-title">本月加班统计</span>
          </template>

          <div class="stat-list">
            <div class="stat-item">
              <div class="stat-icon weekday">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">工作日加班</div>
                <div class="stat-value">{{ monthStats.weekdayHours || 0 }}小时</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon weekend">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">周末加班</div>
                <div class="stat-value">{{ monthStats.weekendHours || 0 }}小时</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon holiday">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">节假日加班</div>
                <div class="stat-value">{{ monthStats.holidayHours || 0 }}小时</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon total">
                <el-icon><Clock /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-label">总计</div>
                <div class="stat-value">{{ monthStats.totalHours || 0 }}小时</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 加班说明 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <span class="card-title">加班说明</span>
          </template>

          <div class="info-list">
            <div class="info-item">
              <div class="info-title">工作日加班</div>
              <div class="info-desc">时薪 × 1.5，需提前申请并获得批准</div>
            </div>
            <div class="info-item">
              <div class="info-title">周末加班</div>
              <div class="info-desc">时薪 × 2，无法安排调休时发放加班费</div>
            </div>
            <div class="info-item">
              <div class="info-title">节假日加班</div>
              <div class="info-desc">时薪 × 3，法定节假日加班需发放3倍工资</div>
            </div>
            <div class="info-item">
              <div class="info-title">注意事项</div>
              <div class="info-desc">加班记录需经审批通过后生效</div>
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
import { submitOvertime, getMyOvertimeRecords } from '@/api/overtime'

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  overtimeDate: '',
  startTime: '',
  endTime: '',
  overtimeHours: 0,
  overtimeType: 1,
  reason: ''
})

// 加班时长显示
const overtimeHoursDisplay = computed(() => {
  return formData.overtimeHours > 0 ? formData.overtimeHours.toFixed(2) : '0.00'
})

// 表单验证规则
const formRules = {
  overtimeDate: [
    { required: true, message: '请选择加班日期', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  overtimeType: [
    { required: true, message: '请选择加班类型', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入加班原因', trigger: 'blur' },
    { min: 5, message: '加班原因至少5个字符', trigger: 'blur' }
  ]
}

// 提交中
const submitting = ref(false)

// 本月加班统计
const monthStats = reactive({
  weekdayHours: 0,
  weekendHours: 0,
  holidayHours: 0,
  totalHours: 0
})

// 计算加班时长
const calculateOvertimeHours = () => {
  if (formData.startTime && formData.endTime) {
    const start = formData.startTime.split(':')
    const end = formData.endTime.split(':')

    const startMinutes = parseInt(start[0]) * 60 + parseInt(start[1])
    const endMinutes = parseInt(end[0]) * 60 + parseInt(end[1])

    if (endMinutes <= startMinutes) {
      ElMessage.warning('结束时间必须晚于开始时间')
      formData.overtimeHours = 0
      return
    }

    // 计算小时数
    formData.overtimeHours = Math.round((endMinutes - startMinutes) / 60 * 100) / 100
  } else {
    formData.overtimeHours = 0
  }
}

// 加载本月加班统计
const loadMonthStats = async () => {
  try {
    const res = await getMyOvertimeRecords()
    const records = res.data || []

    // 筛选本月的已批准记录
    const now = new Date()
    const currentMonth = now.getMonth()
    const currentYear = now.getFullYear()

    const approvedRecords = records.filter(r => {
      const recordDate = new Date(r.overtimeDate)
      return r.status === 1 &&
        recordDate.getMonth() === currentMonth &&
        recordDate.getFullYear() === currentYear
    })

    monthStats.weekdayHours = approvedRecords
      .filter(r => r.overtimeType === 1)
      .reduce((sum, r) => sum + (r.overtimeHours || 0), 0)

    monthStats.weekendHours = approvedRecords
      .filter(r => r.overtimeType === 2)
      .reduce((sum, r) => sum + (r.overtimeHours || 0), 0)

    monthStats.holidayHours = approvedRecords
      .filter(r => r.overtimeType === 3)
      .reduce((sum, r) => sum + (r.overtimeHours || 0), 0)

    monthStats.totalHours = approvedRecords
      .reduce((sum, r) => sum + (r.overtimeHours || 0), 0)
  } catch (error) {
    console.error('加载加班统计失败:', error)
  }
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    if (formData.overtimeHours <= 0) {
      ElMessage.warning('请选择有效的加班时间')
      return
    }

    submitting.value = true
    try {
      await submitOvertime(formData)
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
  formData.overtimeHours = 0
}

onMounted(() => {
  loadMonthStats()
})
</script>

<style scoped lang="css">
.overtime-apply-container {
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

.stat-icon.weekday {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
}

.stat-icon.weekend {
  background: linear-gradient(135deg, #722ed1 0%, #531dab 100%);
}

.stat-icon.holiday {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
}

.stat-icon.total {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
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
