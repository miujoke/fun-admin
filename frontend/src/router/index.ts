import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false },
  },
  {
    path: '/',
    component: DefaultLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '仪表盘', requiresAuth: true },
      },
      {
        path: 'system/user',
        name: 'UserManage',
        component: () => import('@/views/dashboard/index.vue'), // 占位，后续扩展
        meta: { title: '用户管理', requiresAuth: true },
      },
      {
        path: 'system/role',
        name: 'RoleManage',
        component: () => import('@/views/dashboard/index.vue'), // 占位
        meta: { title: '角色管理', requiresAuth: true },
      },
      {
        path: 'system/menu',
        name: 'MenuManage',
        component: () => import('@/views/dashboard/index.vue'), // 占位
        meta: { title: '菜单管理', requiresAuth: true },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('fund_admin_token')

  if (to.meta.requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    document.title = `${to.meta.title || ''} - 基金管理系统`
    next()
  }
})

export default router