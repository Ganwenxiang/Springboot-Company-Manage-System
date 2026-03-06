<template>
  <div class="user-list-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="action-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入昵称" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
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
            新增用户
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
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="empNo" label="关联工号" width="120">
          <template #default="{ row }">
            {{ row.empNo || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="empName" label="关联员工" width="120">
          <template #default="{ row }">
            {{ row.empName || '--' }}
          </template>
        </el-table-column>
        <el-table-column label="角色" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="role in row.roles"
              :key="role.id"
              type="primary"
              style="margin-right: 5px"
            >
              {{ role.roleName }}
            </el-tag>
            <span v-if="!row.roles || row.roles.length === 0">--</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="160">
          <template #default="{ row }">
            {{ row.lastLoginTime || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="primary" @click="handleAssignRole(row)">
              分配角色
            </el-button>
            <el-button link type="warning" @click="handleResetPwd(row)">
              重置密码
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
      v-model="formVisible"
      :title="formTitle"
      width="600px"
      @close="handleFormClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            :disabled="formData.id !== null"
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="formData.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="关联员工" prop="empId">
          <el-select
            v-model="formData.empId"
            placeholder="请选择关联员工"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="emp in employeeList"
              :key="emp.id"
              :label="`${emp.empNo} - ${emp.empName}`"
              :value="emp.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="formData.id === null" label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog
      v-model="roleVisible"
      title="分配角色"
      width="500px"
    >
      <el-form label-width="80px">
        <el-form-item label="用户">
          {{ currentUser.username }} ({{ currentUser.nickname || '--' }})
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox
              v-for="role in allRoles"
              :key="role.id"
              :value="role.id"
            >
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="roleVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAssignRole" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser, resetPassword, assignRoles } from '@/api/system'
import { getEmployeeList } from '@/api/employee'
import { getRoleList } from '@/api/system'

// 搜索表单
const searchForm = reactive({
  username: '',
  nickname: '',
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

// 表单对话框
const formVisible = ref(false)
const formTitle = computed(() => formData.id === null ? '新增用户' : '编辑用户')
const formRef = ref(null)
const formData = reactive({
  id: null,
  username: '',
  nickname: '',
  empId: null,
  password: '',
  status: 1
})

const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
  ]
}

const submitting = ref(false)

// 员工列表
const employeeList = ref([])

// 角色相关
const roleVisible = ref(false)
const currentUser = ref({})
const selectedRoles = ref([])
const allRoles = ref([])

// 加载用户列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getUserList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 加载员工列表
const loadEmployeeList = async () => {
  try {
    const res = await getEmployeeList({ pageSize: 1000 })
    employeeList.value = res.data.list || []
  } catch (error) {
    console.error('加载员工列表失败:', error)
  }
}

// 加载角色列表
const loadRoleList = async () => {
  try {
    const res = await getRoleList({ pageSize: 1000 })
    allRoles.value = res.data.list || []
  } catch (error) {
    console.error('加载角色列表失败:', error)
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
    username: '',
    nickname: '',
    status: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  Object.assign(formData, {
    id: null,
    username: '',
    nickname: '',
    empId: null,
    password: '',
    status: 1
  })
  formVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(formData, {
    id: row.id,
    username: row.username,
    nickname: row.nickname,
    empId: row.empId,
    password: '',
    status: row.status
  })
  formVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      if (formData.id === null) {
        await createUser(formData)
        ElMessage.success('创建成功')
      } else {
        await updateUser(formData)
        ElMessage.success('更新成功')
      }
      formVisible.value = false
      loadTableData()
    } catch (error) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 表单对话框关闭
const handleFormClose = () => {
  formRef.value?.resetFields()
}

// 分配角色
const handleAssignRole = (row) => {
  currentUser.value = row
  selectedRoles.value = row.roles ? row.roles.map(r => r.id) : []
  roleVisible.value = true
}

// 提交分配角色
const handleSubmitAssignRole = async () => {
  submitting.value = true
  try {
    await assignRoles({
      userId: currentUser.value.id,
      roleIds: selectedRoles.value
    })
    ElMessage.success('分配成功')
    roleVisible.value = false
    loadTableData()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置密码
const handleResetPwd = (row) => {
  ElMessageBox.confirm(`确定要重置用户 ${row.username} 的密码吗？重置后密码为：123456`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await resetPassword(row.id)
      ElMessage.success('重置成功，新密码为：123456')
    } catch (error) {
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 ${row.username} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      loadTableData()
    } catch (error) {
      ElMessage.error('操作失败')
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
  loadEmployeeList()
  loadRoleList()
})
</script>

<style scoped lang="css">
.user-list-container {
  padding: 20px;
}

.action-card {
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
