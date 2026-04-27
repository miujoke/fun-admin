<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '210px'" class="layout-aside">
      <div class="logo">
        <img src="/vite.svg" alt="logo" class="logo-img" />
        <span v-show="!isCollapse" class="logo-text">基金管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <template #title>仪表盘</template>
        </el-menu-item>
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/menu">菜单管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><UserFilled /></el-icon>
              {{ userStore.userInfo?.nickname || userStore.userInfo?.username || '用户' }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { DataBoard, Setting, Expand, Fold, UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped lang="scss">
@use '@/styles/variables' as *;

.layout-container {
  height: 100vh;
}

.layout-aside {
  background: $sidebar-bg;
  transition: width 0.28s;
  overflow: hidden;

  .logo {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #2b2f3a;

    .logo-img {
      width: 32px;
      height: 32px;
    }

    .logo-text {
      color: #fff;
      font-size: 16px;
      font-weight: 600;
      margin-left: 10px;
      white-space: nowrap;
    }
  }
}

.layout-header {
  height: $header-height;
  background: $header-bg;
  border-bottom: 1px solid $border-color-lighter;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;

  .collapse-btn {
    cursor: pointer;
    font-size: 20px;
  }

  .user-info {
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.layout-main {
  background: $bg-color-page;
  padding: 20px;
}
</style>