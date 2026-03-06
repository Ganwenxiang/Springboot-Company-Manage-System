<template>
  <div class="menu-list-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="action-card">
      <el-form :inline="true" class="search-form">
        <el-form-item>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增菜单
          </el-button>
          <el-button @click="handleExpandAll">
            <el-icon><DCaret /></el-icon>
            展开全部
          </el-button>
          <el-button @click="handleCollapseAll">
            <el-icon><CaretTop /></el-icon>
            折叠全部
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
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
        ref="tableRef"
      >
        <el-table-column prop="menuName" label="菜单名称" width="200" />
        <el-table-column prop="icon" label="图标" width="80" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.menuType === 1" type="primary">目录</el-tag>
            <el-tag v-else-if="row.menuType === 2" type="success">菜单</el-tag>
            <el-tag v-else-if="row.menuType === 3" type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" min-width="150" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="permission" label="权限标识" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.permission || '--' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="primary" @click="handleAddChild(row)">
              新增子项
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
      v-model="formVisible"
      :title="formTitle"
      width="700px"
      @close="handleFormClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级菜单">
              <el-tree-select
                v-model="formData.parentId"
                :data="menuTreeOptions"
                :props="{ label: 'menuName', value: 'id' }"
                placeholder="选择上级菜单（不选则为顶级菜单）"
                clearable
                check-strictly
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="formData.menuType">
                <el-radio :value="1">目录</el-radio>
                <el-radio :value="2">菜单</el-radio>
                <el-radio :value="3">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="formData.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序">
              <el-input-number v-model="formData.orderNum" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="formData.menuType !== 3">
          <el-col :span="12">
            <el-form-item label="路由路径">
              <el-input v-model="formData.path" placeholder="请输入路由路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组件路径">
              <el-input v-model="formData.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="权限标识">
              <el-input v-model="formData.permission" placeholder="如：employee:list" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单图标">
              <el-input v-model="formData.icon" placeholder="图标名称">
                <template #prepend>
                  <el-icon v-if="formData.icon"><component :is="formData.icon" /></el-icon>
                  <el-icon v-else><Picture /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="formData.status">
                <el-radio :value="1">正常</el-radio>
                <el-radio :value="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllMenus, createMenu, updateMenu, deleteMenu } from '@/api/system'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const emptyText = ref('暂无数据')
const tableRef = ref(null)

// 表单对话框
const formVisible = ref(false)
const formTitle = computed(() => formData.id === null ? '新增菜单' : '编辑菜单')
const formRef = ref(null)
const formData = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  menuType: 2,
  path: '',
  component: '',
  icon: '',
  permission: '',
  orderNum: 0,
  status: 1
})

const formRules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ]
}

const submitting = ref(false)

// 菜单树选项（用于上级菜单选择）
const menuTreeOptions = computed(() => {
  return buildMenuTreeOptions(tableData.value)
})

// 构建菜单树选项（排除当前编辑的节点及其子节点）
const buildMenuTreeOptions = (menus) => {
  const buildNode = (menu, level = 0) => {
    const prefix = '　'.repeat(level)
    const node = {
      id: menu.id,
      menuName: prefix + menu.menuName,
      children: []
    }
    if (menu.children && menu.children.length > 0) {
      menu.children.forEach(child => {
        // 排除当前编辑的节点
        if (child.id !== formData.id) {
          node.children.push(buildNode(child, level + 1))
        }
      })
    }
    return node
  }

  return menus
    .filter(menu => menu.id !== formData.id)
    .map(menu => buildNode(menu))
}

// 加载菜单列表
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await getAllMenus()
    tableData.value = buildMenuTree(res.data || [])
  } catch (error) {
    ElMessage.error('加载菜单列表失败')
  } finally {
    loading.value = false
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

  // 按排序字段排序
  const sortByOrder = (nodes) => {
    nodes.sort((a, b) => (a.orderNum || 0) - (b.orderNum || 0))
    nodes.forEach(node => {
      if (node.children && node.children.length > 0) {
        sortByOrder(node.children)
      }
    })
  }

  sortByOrder(roots)
  return roots
}

// 展开全部
const handleExpandAll = () => {
  const expandRows = (rows) => {
    rows.forEach(row => {
      tableRef.value?.toggleRowExpansion(row, true)
      if (row.children && row.children.length > 0) {
        expandRows(row.children)
      }
    })
  }
  expandRows(tableData.value)
}

// 折叠全部
const handleCollapseAll = () => {
  const collapseRows = (rows) => {
    rows.forEach(row => {
      tableRef.value?.toggleRowExpansion(row, false)
      if (row.children && row.children.length > 0) {
        collapseRows(row.children)
      }
    })
  }
  collapseRows(tableData.value)
}

// 新增
const handleAdd = () => {
  Object.assign(formData, {
    id: null,
    parentId: 0,
    menuName: '',
    menuType: 2,
    path: '',
    component: '',
    icon: '',
    permission: '',
    orderNum: 0,
    status: 1
  })
  formVisible.value = true
}

// 新增子项
const handleAddChild = (row) => {
  Object.assign(formData, {
    id: null,
    parentId: row.id,
    menuName: '',
    menuType: row.menuType === 1 ? 2 : 3,
    path: '',
    component: '',
    icon: '',
    permission: '',
    orderNum: 0,
    status: 1
  })
  formVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  Object.assign(formData, {
    id: row.id,
    parentId: row.parentId,
    menuName: row.menuName,
    menuType: row.menuType,
    path: row.path,
    component: row.component,
    icon: row.icon,
    permission: row.permission,
    orderNum: row.orderNum,
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
        await createMenu(formData)
        ElMessage.success('创建成功')
      } else {
        await updateMenu(formData)
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

// 删除
const handleDelete = (row) => {
  const hasChildren = row.children && row.children.length > 0
  const message = hasChildren
    ? `该菜单包含 ${row.children.length} 个子菜单，确定要删除吗？`
    : `确定要删除菜单 ${row.menuName} 吗？`

  ElMessageBox.confirm(message, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      loadTableData()
    } catch (error) {
      ElMessage.error('操作失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  loadTableData()
})
</script>

<style scoped lang="css">
.menu-list-container {
  padding: 20px;
}

.action-card {
  margin-bottom: 20px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.table-card {
  overflow-x: auto;
}
</style>
