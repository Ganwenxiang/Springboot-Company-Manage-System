import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/auth'
import { setToken, removeToken, setUserInfo, removeUserInfo, getUserInfo as getLocalUserInfo } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: getLocalUserInfo(),
    permissions: [],
    menuTree: []
  }),

  getters: {
    // 是否已登录
    isLoggedIn: (state) => !!state.token,

    // 是否是管理员
    isAdmin: (state) => state.userInfo?.isAdmin || false,

    // 获取用户头像
    avatar: (state) => state.userInfo?.avatar || '',

    // 获取用户名
    username: (state) => state.userInfo?.username || '',

    // 获取昵称
    nickname: (state) => state.userInfo?.nickname || ''
  },

  actions: {
    // 登录
    async login(loginData) {
      try {
        const res = await login(loginData)
        this.token = res.data.token
        setToken(this.token)

        // 获取用户信息
        await this.getUserInfo()

        return Promise.resolve()
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const res = await getUserInfo()
        this.userInfo = res.data
        this.permissions = res.data.permissions || []
        this.menuTree = res.data.menuTree || []

        setUserInfo(this.userInfo)
        return Promise.resolve()
      } catch (error) {
        // 获取用户信息失败，清除登录状态
        this.logout()
        return Promise.reject(error)
      }
    },

    // 登出
    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('登出失败:', error)
      } finally {
        this.token = ''
        this.userInfo = null
        this.permissions = []
        this.menuTree = []

        removeToken()
        removeUserInfo()
      }
    },

    // 检查权限
    hasPermission(permission) {
      if (this.isAdmin) {
        return true
      }
      return this.permissions.includes(permission)
    }
  }
})
