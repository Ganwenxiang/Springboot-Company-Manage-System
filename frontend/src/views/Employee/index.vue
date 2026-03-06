<template>
  <div class="employee-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.empName" placeholder="请输入员工姓名" clearable />
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="searchForm.empNo" placeholder="请输入工号" clearable />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="searchForm.deptId" placeholder="请选择部门" clearable style="width: 180px">
            <el-option
              v-for="dept in deptList"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.empStatus" placeholder="全部" clearable style="width: 120px">
            <el-option label="在职" :value="1" />
            <el-option label="离职" :value="2" />
            <el-option label="停职" :value="3" />
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
        <el-form-item style="float: right">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增员工
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
        <el-table-column prop="empNo" label="工号" width="120" />
        <el-table-column prop="empName" label="姓名" width="120" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="jobTitle" label="职位" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="员工状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.empStatus === 1" type="success">在职</el-tag>
            <el-tag v-else-if="row.empStatus === 2" type="info">离职</el-tag>
            <el-tag v-else-if="row.empStatus === 3" type="warning">停职</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">
              查看
            </el-button>
            <el-button link type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="empNo">
              <el-input v-model="formData.empNo" placeholder="请输入工号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="empName">
              <el-input v-model="formData.empName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="formData.deptId" placeholder="请选择部门" style="width: 100%">
                <el-option
                  v-for="dept in deptList"
                  :key="dept.id"
                  :label="dept.deptName"
                  :value="dept.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位">
              <el-input v-model="formData.jobTitle" placeholder="请输入职位" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="formData.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" :value="1" />
                <el-option label="女" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker
                v-model="formData.entryDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="居住地址">
          <el-input
            v-model="formData.address"
            type="textarea"
            placeholder="请输入居住地址"
            :rows="2"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEmployeeList, addEmployee, updateEmployee, deleteEmployee } from '@/api/employee'
import { getDeptTree } from '@/api/dept'

// 搜索表单
const searchForm = reactive({
  empName: '',
  empNo: '',
  deptId: null,
  empStatus: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')

// 部门列表
const deptList = ref([])

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const submitting = ref(false)

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  id: null,
  empNo: '',
  empName: '',
  deptId: null,
  jobTitle: '',
  phone: '',
  email: '',
  gender: null,
  entryDate: '',
  address: ''
})

// 表单验证规则
const formRules = {
  empNo: [
    { required: true, message: '请输入工号', trigger: 'blur' }
  ],
  empName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 加载部门列表
const loadDeptList = async () => {
  try {
    const res = await getDeptTree()
    // 扁平部门树
    const flattenDepts = (tree) => {
      const result = []
      const stack = [...tree]

      while (stack.length) {
        const dept = stack.pop()
        result.push(dept)

        if (dept.children && dept.children.length > 0) {
          stack.push(...dept.children)
        }
      }

      return result
    }

    deptList.value = flattenDepts(res.data)
  } catch (error) {
    console.error('加载部门列表失败:', error)
  }
}

// 加载员工列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getEmployeeList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载员工列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    empName: '',
    empNo: '',
    deptId: null,
    empStatus: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增员工'
  Object.assign(formData, {
    id: null,
    empNo: '',
    empName: '',
    deptId: null,
    jobTitle: '',
    phone: '',
    email: '',
    gender: null,
    entryDate: '',
    address: ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  Object.assign(formData, {
    id: row.id,
    empNo: row.empNo,
    empName: row.empName,
    deptId: row.deptId,
    jobTitle: row.jobTitle,
    phone: row.phone,
    email: row.email || '',
    gender: row.gender || null,
    entryDate: row.entryDate || '',
    address: row.address || ''
  })
  dialogVisible.value = true
}

// 查看
const handleView = (row) => {
  ElMessage.info('查看功能开发中')
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除员工 "${row.empName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteEmployee(row.id)
      ElMessage.success('删除成功')
      loadTableData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (formData.id) {
        await updateEmployee(formData)
        ElMessage.success('更新成功')
      } else {
        await addEmployee(formData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadTableData()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
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
  loadDeptList()
  loadTableData()
})
</script>

<style scoped lang="css">
.employee-container {
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
