import { defineStore } from 'pinia'
import { authApi, type UserInfoVO } from '@/api/auth'
import { config } from '@/config'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem(config.tokenKey) || '',
    userInfo: null as UserInfoVO | null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
  },

  actions: {
    async login(username: string, password: string, captchaCode: string, captchaKey: string) {
      const res = await authApi.login({
        username,
        password,
        captchaCode,
        captchaKey,
      })
      this.token = res.data.token
      this.userInfo = res.data.userInfo
      localStorage.setItem(config.tokenKey, this.token)
      return res.data
    },

    async fetchUserInfo() {
      const res = await authApi.getCurrentUser()
      this.userInfo = res.data
      return res.data
    },

    logout() {
      authApi.logout().catch(() => {})
      this.clearAuth()
    },

    clearAuth() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem(config.tokenKey)
    },
  },
})