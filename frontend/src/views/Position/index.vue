<template>
  <div class="position-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="职位名称">
          <el-input v-model="searchForm.positionName" placeholder="请输入职位名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
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
            新增职位
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
        <el-table-column prop="positionName" label="职位名称" width="150" />
        <el-table-column prop="positionLevel" label="职位级别" width="120" align="center">
          <template #default="{ row }">
            <el-tag v-for="n in row.positionLevel" :key="n" type="warning" size="small" style="margin-right: 2px">★</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="baseSalary" label="基本工资" width="120" align="right">
          <template #default="{ row }">
            ¥{{ row.baseSalary?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="info">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="职位名称" prop="positionName">
          <el-input v-model="formData.positionName" placeholder="请输入职位名称" />
        </el-form-item>

        <el-form-item label="职位级别" prop="positionLevel">
          <el-input-number v-model="formData.positionLevel" :min="1" :max="10" controls-position="right" style="width: 200px" />
          <span style="margin-left: 10px; color: #909399">级别范围：1-10</span>
        </el-form-item>

        <el-form-item label="基本工资" prop="baseSalary">
          <el-input-number v-model="formData.baseSalary" :min="0" :precision="2" :step="100" controls-position="right" style="width: 200px" />
          <span style="margin-left: 10px; color: #909399">元</span>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
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
import { getPositionList, addPosition, updatePosition, deletePosition } from '@/api/position'

// 搜索表单
const searchForm = reactive({
  positionName: '',
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

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增职位')
const submitting = ref(false)

// 表单引用
const formRef = ref(null)

// 表单数据
const formData = reactive({
  id: null,
  positionName: '',
  positionLevel: 1,
  baseSalary: 0,
  status: 1
})

// 表单验证规则
const formRules = {
  positionName: [
    { required: true, message: '请输入职位名称', trigger: 'blur' }
  ],
  positionLevel: [
    { required: true, message: '请输入职位级别', trigger: 'blur' }
  ],
  baseSalary: [
    { required: true, message: '请输入基本工资', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 加载职位列表
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getPositionList(params)
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载职位列表失败')
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
    positionName: '',
    status: null
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增职位'
  Object.assign(formData, {
    id: null,
    positionName: '',
    positionLevel: 1,
    baseSalary: 0,
    status: 1
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑职位'
  Object.assign(formData, {
    id: row.id,
    positionName: row.positionName,
    positionLevel: row.positionLevel,
    baseSalary: row.baseSalary,
    status: row.status
  })
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除职位 "${row.positionName}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePosition(row.id)
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
        await updatePosition(formData)
        ElMessage.success('更新成功')
      } else {
        await addPosition(formData)
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
  loadTableData()
})
</script>

<style scoped lang="css">
.position-container {
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
