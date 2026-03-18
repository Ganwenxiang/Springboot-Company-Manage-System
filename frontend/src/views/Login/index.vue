<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="login-box">
      <div class="login-header">
        <div class="logo-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <h1>员工管理系统</h1>
        <p>Employee Management System</p>
      </div>

      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
            clearable
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            clearable
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <div class="divider">
          <span>演示账号</span>
        </div>
        <div class="demo-accounts">
          <div class="account-item">
            <span class="role">管理员</span>
            <span class="account">admin / admin123</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部版权 -->
    <div class="copyright">
      <p>© 2024 EMS - 员工管理系统</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: 'admin123'
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await userStore.login(loginForm)
      ElMessage.success('登录成功')

      const redirect = route.query.redirect || '/'
      router.push(redirect)
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped lang="css">
.login-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #1e3a5f 0%, #0d2137 50%, #1a1a2e 100%);
  overflow: hidden;
}

/* 背景装饰圆 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 600px;
  height: 600px;
  background: linear-gradient(135deg, #5b8def, #7c3aed);
  top: -200px;
  right: -100px;
  animation: float 20s ease-in-out infinite;
}

.circle-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #7c3aed, #5b8def);
  bottom: -100px;
  left: -100px;
  animation: float 15s ease-in-out infinite reverse;
}

.circle-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #5b8def, #a855f7);
  top: 50%;
  left: 20%;
  animation: float 12s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(5deg);
  }
}

.login-box {
  position: relative;
  width: 420px;
  padding: 48px 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25),
              0 0 0 1px rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.logo-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, #5b8def 0%, #7c3aed 100%);
  border-radius: 16px;
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba(91, 141, 239, 0.35);
}

.logo-icon svg {
  width: 32px;
  height: 32px;
  color: white;
}

.login-header h1 {
  font-size: 26px;
  font-weight: 700;
  color: #1f1f1f;
  margin: 0 0 8px 0;
  letter-spacing: 1px;
}

.login-header p {
  font-size: 13px;
  color: #8c8c8c;
  margin: 0;
  letter-spacing: 2px;
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }

  :deep(.el-input__wrapper) {
    height: 48px;
    border-radius: 10px !important;
    box-shadow: 0 0 0 1px #e8e8e8 inset !important;
    transition: all 0.25s ease;
    background: #fafafa;
  }

  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #b3cdf9 inset !important;
    background: #fff;
  }

  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 2px rgba(91, 141, 239, 0.15) inset,
                0 0 0 1px #5b8def inset !important;
    background: #fff;
  }

  :deep(.el-input__inner) {
    font-size: 15px;
    color: #1f1f1f;
  }

  :deep(.el-input__inner::placeholder) {
    color: #bfbfbf;
  }
}

.login-button {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 4px;
  border-radius: 10px !important;
  background: linear-gradient(135deg, #5b8def 0%, #7c3aed 100%) !important;
  border: none !important;
  box-shadow: 0 4px 14px rgba(91, 141, 239, 0.4);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(91, 141, 239, 0.5);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  margin-top: 28px;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #e8e8e8, transparent);
}

.divider span {
  padding: 0 16px;
  font-size: 12px;
  color: #8c8c8c;
}

.demo-accounts {
  background: #f5f7fa;
  border-radius: 10px;
  padding: 14px 18px;
}

.account-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.account-item .role {
  font-size: 13px;
  color: #8c8c8c;
}

.account-item .account {
  font-size: 13px;
  color: #5b8def;
  font-weight: 500;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', 'Fira Code', monospace;
}

.copyright {
  position: absolute;
  bottom: 24px;
  text-align: center;
}

.copyright p {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  margin: 0;
}
</style>