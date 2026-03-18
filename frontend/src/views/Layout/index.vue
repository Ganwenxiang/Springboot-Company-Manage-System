<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
      <div class="sidebar-logo">
        <div class="logo-icon-small">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <transition name="fade">
          <span v-if="!isCollapse" class="logo-text">员工管理系统</span>
        </transition>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        router
        class="sidebar-menu"
      >
        <template v-for="item in menuList" :key="item.path">
          <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.path">
            <template #title>
              <el-icon v-if="item.meta?.icon">
                <component :is="item.meta.icon" />
              </el-icon>
              <span>{{ item.meta?.title }}</span>
            </template>
            <template v-for="child in item.children" :key="child.path">
              <el-menu-item
                v-if="!child.hidden"
                :index="child.fullPath"
                @click="handleMenuClick(child)"
              >
                <span>{{ child.meta?.title }}</span>
              </el-menu-item>
            </template>
          </el-sub-menu>

          <el-menu-item v-else :index="item.path" @click="handleMenuClick(item)">
            <el-icon v-if="item.meta?.icon">
              <component :is="item.meta.icon" />
            </el-icon>
            <template #title>{{ item.meta?.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container class="main-container">
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleCollapse">
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.path">
              {{ item.meta?.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="36" class="user-avatar">
                {{ (userStore.nickname || userStore.username || 'U').charAt(0).toUpperCase() }}
              </el-avatar>
              <span class="username">{{ userStore.nickname || userStore.username }}</span>
              <el-icon class="arrow-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人信息
                </el-dropdown-item>
                <el-dropdown-item command="password">
                  <el-icon><Lock /></el-icon>
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>

    <!-- 个人信息弹窗 -->
    <el-dialog v-model="profileDialogVisible" title="个人信息" width="500px">
      <el-form :model="profileForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="userStore.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="profileForm.avatar" placeholder="请输入头像URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="profileDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProfile" :loading="profileLoading">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="passwordDialogVisible" title="修改密码" width="500px">
      <el-form :model="passwordForm" label-width="100px" :rules="passwordRules" ref="passwordFormRef">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { User, Lock, SwitchButton, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { changePassword, updateProfile } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

// 个人信息弹窗
const profileDialogVisible = ref(false)
const profileLoading = ref(false)
const profileForm = ref({
  nickname: '',
  avatar: ''
})

// 修改密码弹窗
const passwordDialogVisible = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref(null)
const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码校验规则
const passwordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  return meta.activeMenu || path
})

// 菜单列表
const menuList = computed(() => {
  const routes = router.options.routes.find(r => r.path === '/')?.children || []
  return routes.filter(route => !route.hidden).map(route => {
    // 如果有子路由，需要构建完整的子路由路径
    if (route.children && route.children.length > 0) {
      return {
        ...route,
        children: route.children.map(child => ({
          ...child,
          fullPath: `/${route.path}/${child.path}`
        }))
      }
    }
    return {
      ...route,
      fullPath: `/${route.path}`
    }
  })
})

// 面包屑
const breadcrumbList = computed(() => {
  const matched = route.matched.filter(item => item.meta?.title)
  return matched
})

// 切换侧边栏
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 点击菜单
const handleMenuClick = (item) => {
  const path = item.fullPath || item.path
  router.push(path)
}

// 下拉菜单操作
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      openProfileDialog()
      break
    case 'password':
      openPasswordDialog()
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 打开个人信息弹窗
const openProfileDialog = () => {
  profileForm.value = {
    nickname: userStore.nickname || '',
    avatar: userStore.avatar || ''
  }
  profileDialogVisible.value = true
}

// 更新个人信息
const handleUpdateProfile = async () => {
  profileLoading.value = true
  try {
    await updateProfile(profileForm.value)
    // 更新 store 中的用户信息
    userStore.userInfo = {
      ...userStore.userInfo,
      nickname: profileForm.value.nickname,
      avatar: profileForm.value.avatar
    }
    // 同步更新 localStorage
    localStorage.setItem('userInfo', JSON.stringify(userStore.userInfo))
    ElMessage.success('个人信息更新成功')
    profileDialogVisible.value = false
  } catch (error) {
    ElMessage.error(error.message || '更新失败')
  } finally {
    profileLoading.value = false
  }
}

// 打开修改密码弹窗
const openPasswordDialog = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
  passwordDialogVisible.value = true
}

// 修改密码
const handleChangePassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return

    passwordLoading.value = true
    try {
      await changePassword(passwordForm.value)
      ElMessage.success('密码修改成功，请重新登录')
      passwordDialogVisible.value = false
      // 清除登录状态，跳转到登录页
      await userStore.logout()
      router.push('/login')
    } catch (error) {
      ElMessage.error(error.message || '修改失败')
    } finally {
      passwordLoading.value = false
    }
  })
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logout()
    ElMessage.success('退出成功')
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped lang="css">
.layout-container {
  display: flex;
  width: 100%;
  height: 100vh;
  background: var(--color-bg-tertiary);
}

/* 侧边栏样式 */
.sidebar {
  background: linear-gradient(180deg, #1e3a5f 0%, #0d2137 100%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow-x: hidden;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
  position: relative;
  z-index: 100;
}

.sidebar::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, transparent, rgba(255,255,255,0.1), transparent);
}

.sidebar-logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.logo-icon-small {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #5b8def 0%, #7c3aed 100%);
  border-radius: 10px;
  flex-shrink: 0;
}

.logo-icon-small svg {
  width: 20px;
  height: 20px;
  color: white;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  white-space: nowrap;
  letter-spacing: 1px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.sidebar-menu {
  border: none;
  background: transparent;
  padding: 12px 8px;
}

.sidebar-menu :deep(.el-menu-item),
.sidebar-menu :deep(.el-sub-menu__title) {
  color: rgba(255, 255, 255, 0.75);
  border-radius: 8px;
  margin: 4px 0;
  height: 46px;
  line-height: 46px;
  transition: all 0.25s ease;
}

.sidebar-menu :deep(.el-menu-item:hover),
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.08) !important;
  color: #fff;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(91, 141, 239, 0.9) 0%, rgba(124, 58, 237, 0.9) 100%) !important;
  color: #fff !important;
  box-shadow: 0 4px 12px rgba(91, 141, 239, 0.3);
}

.sidebar-menu :deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  color: #fff !important;
}

.sidebar-menu :deep(.el-sub-menu .el-menu) {
  background: rgba(0, 0, 0, 0.15) !important;
  margin: 4px 0;
  border-radius: 8px;
}

.sidebar-menu :deep(.el-sub-menu .el-menu .el-menu-item) {
  height: 42px;
  line-height: 42px;
  padding-left: 52px !important;
}

/* 子菜单浮层样式 */
.sidebar-menu :deep(.el-menu--popup) {
  background: #1a3a5c !important;
  min-width: 180px !important;
  border-radius: 10px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  padding: 8px;
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item) {
  color: rgba(255, 255, 255, 0.75) !important;
  background: transparent !important;
  border-radius: 6px;
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.1) !important;
  color: #fff !important;
}

.sidebar-menu :deep(.el-menu--popup .el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(91, 141, 239, 0.9) 0%, rgba(124, 58, 237, 0.9) 100%) !important;
  color: #fff !important;
}

/* 主容器 */
.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 头部样式 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--header-bg);
  border-bottom: 1px solid var(--color-border-lighter);
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  position: relative;
  z-index: 99;
}

.header .header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header .header-left .collapse-icon {
  font-size: 20px;
  cursor: pointer;
  color: var(--color-text-secondary);
  padding: 8px;
  border-radius: 8px;
  transition: all 0.25s ease;
}

.header .header-left .collapse-icon:hover {
  color: var(--el-color-primary);
  background: var(--color-bg-secondary);
}

.header .header-right .user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 10px;
  transition: all 0.25s ease;
}

.header .header-right .user-info:hover {
  background: var(--color-bg-secondary);
}

.user-avatar {
  background: linear-gradient(135deg, #5b8def 0%, #7c3aed 100%);
  color: #fff;
  font-weight: 600;
}

.header .header-right .user-info .username {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
}

.header .header-right .user-info .arrow-icon {
  font-size: 12px;
  color: var(--color-text-secondary);
  transition: transform 0.25s ease;
}

.header .header-right .user-info:hover .arrow-icon {
  transform: rotate(180deg);
}

/* 主内容区 */
.main-content {
  flex: 1;
  overflow-y: auto;
  background: var(--color-bg-tertiary);
  padding: 0;
}

/* 页面切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.25s ease;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>