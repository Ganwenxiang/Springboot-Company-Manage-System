<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-message">页面不存在</div>
      <div class="error-description">
        抱歉，您访问的页面不存在或已被删除。
      </div>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped lang="css">
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
}

.not-found-content {
  text-align: center;

  .error-code {
    font-size: 120px;
    font-weight: 700;
    color: var(--el-color-primary);
    line-height: 1;
    margin-bottom: 20px;
    background: linear-gradient(135deg, var(--el-color-primary), #67C23A);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .error-message {
    font-size: 24px;
    font-weight: 500;
    color: var(--color-text-primary);
    margin-bottom: 12px;
  }

  .error-description {
    font-size: 16px;
    color: var(--color-text-secondary);
    margin-bottom: 32px;
  }
}
</style>
