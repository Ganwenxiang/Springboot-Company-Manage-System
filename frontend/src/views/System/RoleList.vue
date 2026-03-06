<template>
  <div class="role-list-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="action-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" clearable style="width: 150px" />
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
            新增角色
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
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="primary" @click="handleAssignMenu(row)">
              分配权限
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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input
            v-model="formData.roleCode"
            placeholder="请输入角色编码（英文）"
            :disabled="formData.id !== null"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            placeholder="请输入角色描述"
            :rows="3"
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

    <!-- 分配权限对话框 -->
    <el-dialog
      v-model="menuVisible"
      title="分配权限"
      width="600px"
    >
      <el-form label-width="80px">
        <el-form-item label="角色">
          {{ currentRole.roleName }} ({{ currentRole.roleCode }})
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            ref="menuTreeRef"
            :data="menuTreeData"
            :props="{ children: 'children', label: 'menuName' }"
            node-key="id"
            show-checkbox
            default-expand-all
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="menuVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAssignMenu" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, createRole, updateRole, deleteRole, assignRoleMenus, getRoleMenus } from '@/api/system'
import { getAllMenus } from '@/api/system'

// 搜索表单
const searchForm = reactive({
  roleName: '',
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
const formTitle = computed(() => formData.id === null ? '新增角色' : '编辑角色')
const formRef = ref(null)
const formData = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
})

const formRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '角色编码只能包含字母、数字和下划线，且不能以数字开头', trigger: 'blur' }
  ]
}

const submitting = ref(false)

// 权限相关
const menuVisible = ref(false)
const currentRole = ref({})
const menuTreeData = ref([])
const menuTreeRef = ref(null)

// 加载角色列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getRoleList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

// 加载菜单树
const loadMenuTree = async () => {
  try {
    const res = await getAllMenus()
    menuTreeData.value = buildMenuTree(res.data || [])
  } catch (error) {
    console.error('加载菜单树失败:', error)
  }
}

// 构建菜单树
const buildMenuTree = (menus) => {
  const map = new Map()
  const roots = []

  // 先创建所有节点的映射
  menus.forEach(menu => {
    map.set(menu.id, { ...menu, children: [] })
  })

  // 构建树形结构
  menus.forEach(menu => {
    const node = map.get(menu.id)
    if (menu.parentId === 0 || !map.has(menu.parentId)) {
      roots.push(node)
    } else {
      map.get(menu.parentId).children.push(node)
    }
  })

  return roots
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    roleName: '',
    status: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  Object.assign(formData, {
    id: null,
    roleName: '',
    roleCode: '',
    description: '',
    status: 1
  })
  formVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(formData, {
    id: row.id,
    roleName: row.roleName,
    roleCode: row.roleCode,
    description: row.description,
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
        await createRole(formData)
        ElMessage.success('创建成功')
      } else {
        await updateRole(formData)
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

// 分配权限
const handleAssignMenu = async (row) => {
  currentRole.value = row
  menuVisible.value = true

  // 加载角色已有的权限
  await nextTick()
  try {
    const res = await getRoleMenus(row.id)
    const menuIds = res.data || []
    menuTreeRef.value?.setCheckedKeys(menuIds)
  } catch (error) {
    console.error('加载角色权限失败:', error)
  }
}

// 提交分配权限
const handleSubmitAssignMenu = async () => {
  submitting.value = true
  try {
    const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
    const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
    const allMenuIds = [...checkedKeys, ...halfCheckedKeys]

    await assignRoleMenus({
      roleId: currentRole.value.id,
      menuIds: allMenuIds
    })
    ElMessage.success('分配成功')
    menuVisible.value = false
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除角色 ${row.roleName} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
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
  loadMenuTree()
})
</script>

<style scoped lang="css">
.role-list-container {
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
