<template>
  <div class="dept-container">
    <el-card shadow="never" class="tree-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">部门管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增部门
          </el-button>
        </div>
      </template>

      <el-table
        :data="deptTree"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        v-loading="loading"
        :empty-text="emptyText"
        default-expand-all
      >
        <el-table-column prop="deptName" label="部门名称" min-width="200" />
        <el-table-column prop="orderNum" label="排序" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="info">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleAddChild(row)">
              新增子部门
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
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="deptTreeOptions"
            :props="{ label: 'deptName', value: 'id' }"
            placeholder="请选择上级部门（不选则为顶级部门）"
            clearable
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称" />
        </el-form-item>

        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="formData.orderNum" :min="0" :max="9999" controls-position="right" style="width: 100%" />
        </el-form-item>

        <el-form-item label="部门状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
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
import { getDeptTree, addDept, updateDept, deleteDept } from '@/api/dept'

// 部门树数据
const deptTree = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const submitting = ref(false)

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  id: null,
  parentId: null,
  deptName: '',
  orderNum: 0,
  status: 1
})

// 表单验证规则
const formRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入排序号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择部门状态', trigger: 'change' }
  ]
}

// 树形选择器数据（排除当前编辑节点及其子节点）
const deptTreeOptions = ref([])

// 加载部门树
const loadDeptTree = async () => {
  loading.value = true
  try {
    const res = await getDeptTree()
    deptTree.value = res.data || []
    deptTreeOptions.value = buildTreeOptions(res.data || [])
  } catch (error) {
    ElMessage.error('加载部门树失败')
  } finally {
    loading.value = false
  }
}

// 构建树形选择器选项
const buildTreeOptions = (tree) => {
  const buildNode = (node) => {
    const option = {
      id: node.id,
      deptName: node.deptName,
      children: node.children && node.children.length > 0
        ? node.children.map(child => buildNode(child))
        : undefined
    }
    return option
  }

  return tree.map(node => buildNode(node))
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增部门'
  Object.assign(formData, {
    id: null,
    parentId: null,
    deptName: '',
    orderNum: 0,
    status: 1
  })
  deptTreeOptions.value = buildTreeOptions(deptTree.value)
  dialogVisible.value = true
}

// 新增子部门
const handleAddChild = (row) => {
  dialogTitle.value = '新增子部门'
  Object.assign(formData, {
    id: null,
    parentId: row.id,
    deptName: '',
    orderNum: 0,
    status: 1
  })
  deptTreeOptions.value = buildTreeOptions(deptTree.value)
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  Object.assign(formData, {
    id: row.id,
    parentId: row.parentId,
    deptName: row.deptName,
    orderNum: row.orderNum,
    status: row.status
  })
  // 排除当前节点及其子节点作为上级部门选项
  deptTreeOptions.value = excludeCurrentAndChildren(deptTree.value, row.id)
  dialogVisible.value = true
}

// 排除当前节点及其子节点
const excludeCurrentAndChildren = (tree, excludeId) => {
  const filterNode = (node) => {
    if (node.id === excludeId) {
      return null
    }
    const filtered = {
      id: node.id,
      deptName: node.deptName
    }
    if (node.children && node.children.length > 0) {
      const children = node.children
        .map(child => filterNode(child))
        .filter(child => child !== null)
      if (children.length > 0) {
        filtered.children = children
      }
    }
    return filtered
  }

  return tree
    .map(node => filterNode(node))
    .filter(node => node !== null)
}

// 删除
const handleDelete = (row) => {
  // 检查是否有子部门
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该部门下有子部门，不能删除')
    return
  }

  ElMessageBox.confirm(`确定要删除部门 "${row.deptName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id)
      ElMessage.success('删除成功')
      loadDeptTree()
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
        await updateDept(formData)
        ElMessage.success('更新成功')
      } else {
        await addDept(formData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadDeptTree()
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

onMounted(() => {
  loadDeptTree()
})
</script>

<style scoped lang="css">
.dept-container {
  padding: 20px;
}

.tree-card {
  min-height: calc(100vh - 120px);
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

/* 树表格样式优化 */
:deep(.el-table__row--level-1 .el-table__cell) {
  padding-left: 20px !important;
}

:deep(.el-table__row--level-2 .el-table__cell) {
  padding-left: 40px !important;
}

:deep(.el-table__row--level-3 .el-table__cell) {
  padding-left: 60px !important;
}

:deep(.el-table__expand-icon) {
  color: var(--el-color-primary);
}
</style>
