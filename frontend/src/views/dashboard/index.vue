<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon :size="28"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">128</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <el-icon :size="28"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">56</div>
            <div class="stat-label">基金产品</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon :size="28"><Wallet /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">1,024</div>
            <div class="stat-label">交易记录</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon :size="28"><Bell /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">12</div>
            <div class="stat-label">待处理事项</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span>最近活动</span>
          </template>
          <el-table :data="recentActivities" stripe>
            <el-table-column prop="time" label="时间" width="180" />
            <el-table-column prop="user" label="操作人" width="120" />
            <el-table-column prop="action" label="操作" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '成功' ? 'success' : 'warning'" size="small">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>系统信息</span>
          </template>
          <div class="system-info">
            <div class="info-item">
              <span class="info-label">系统版本</span>
              <span class="info-value">1.0.0</span>
            </div>
            <div class="info-item">
              <span class="info-label">前端框架</span>
              <span class="info-value">Vue 3 + Vite</span>
            </div>
            <div class="info-item">
              <span class="info-label">后端框架</span>
              <span class="info-value">Spring Boot 3</span>
            </div>
            <div class="info-item">
              <span class="info-label">数据库</span>
              <span class="info-value">MySQL 8.x</span>
            </div>
            <div class="info-item">
              <span class="info-label">当前用户</span>
              <span class="info-value">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '-' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { User, TrendCharts, Wallet, Bell } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(async () => {
  if (userStore.isLoggedIn && !userStore.userInfo) {
    await userStore.fetchUserInfo()
  }
})

const recentActivities = ref([
  { time: '2024-01-15 10:30', user: 'admin', action: '新增基金产品「稳健成长A」', status: '成功' },
  { time: '2024-01-15 09:15', user: 'admin', action: '修改用户权限配置', status: '成功' },
  { time: '2024-01-14 17:45', user: '张三', action: '提交交易记录审核', status: '待审核' },
  { time: '2024-01-14 14:20', user: 'admin', action: '系统配置更新', status: '成功' },
])
</script>

<style scoped lang="scss">
@use '@/styles/variables' as *;

.stat-card {
  .el-card__body {
    display: flex;
    align-items: center;
    gap: 16px;
  }
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.stat-info {
  .stat-value {
    font-size: 24px;
    font-weight: 600;
    color: $text-primary;
  }

  .stat-label {
    font-size: 13px;
    color: $text-secondary;
    margin-top: 4px;
  }
}

.system-info {
  .info-item {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid $border-color-lighter;

    &:last-child {
      border-bottom: none;
    }

    .info-label {
      color: $text-secondary;
    }

    .info-value {
      color: $text-primary;
      font-weight: 500;
    }
  }
}
</style>