import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToken } from '@/utils/auth'

// 布局组件
const Layout = () => import('@/views/Layout/index.vue')

// 静态路由
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard/index.vue'),
        meta: { title: '首页概览', icon: 'Dashboard', affix: true }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('@/views/Employee/index.vue'),
        meta: { title: '员工管理', icon: 'User' }
      },
      {
        path: 'employee/detail/:id',
        name: 'EmployeeDetail',
        component: () => import('@/views/Employee/detail.vue'),
        meta: { title: '员工详情', activeMenu: '/employee' },
        hidden: true
      },
      {
        path: 'dept',
        name: 'Dept',
        component: () => import('@/views/Dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'attendance',
        name: 'Attendance',
        component: () => import('@/views/Attendance/index.vue'),
        redirect: '/attendance/my',
        meta: { title: '考勤管理', icon: 'Clock' },
        children: [
          {
            path: 'my',
            name: 'AttendanceMy',
            component: () => import('@/views/Attendance/MyToday.vue'),
            meta: { title: '今日考勤' }
          },
          {
            path: 'record',
            name: 'AttendanceRecord',
            component: () => import('@/views/Attendance/Record.vue'),
            meta: { title: '考勤记录' }
          },
          {
            path: 'manage',
            name: 'AttendanceManage',
            component: () => import('@/views/Attendance/Manage.vue'),
            meta: { title: '考勤管理', permission: 'attendance:manage' }
          }
        ]
      },
      {
        path: 'leave',
        name: 'Leave',
        component: () => import('@/views/Leave/index.vue'),
        redirect: '/leave/my',
        meta: { title: '请假管理', icon: 'Document' },
        children: [
          {
            path: 'my',
            name: 'LeaveMy',
            component: () => import('@/views/Leave/MyList.vue'),
            meta: { title: '我的请假' }
          },
          {
            path: 'apply',
            name: 'LeaveApply',
            component: () => import('@/views/Leave/Apply.vue'),
            meta: { title: '请假申请' }
          },
          {
            path: 'approve',
            name: 'LeaveApprove',
            component: () => import('@/views/Leave/Approve.vue'),
            meta: { title: '请假审批', permission: 'leave:approve' }
          }
        ]
      },
      {
        path: 'overtime',
        name: 'Overtime',
        component: () => import('@/views/Overtime/index.vue'),
        redirect: '/overtime/my',
        meta: { title: '加班管理', icon: 'Timer' },
        children: [
          {
            path: 'my',
            name: 'OvertimeMy',
            component: () => import('@/views/Overtime/MyList.vue'),
            meta: { title: '我的加班' }
          },
          {
            path: 'apply',
            name: 'OvertimeApply',
            component: () => import('@/views/Overtime/Apply.vue'),
            meta: { title: '加班申请' }
          },
          {
            path: 'approve',
            name: 'OvertimeApprove',
            component: () => import('@/views/Overtime/Approve.vue'),
            meta: { title: '加班审批', permission: 'overtime:approve' }
          }
        ]
      },
      {
        path: 'salary',
        name: 'Salary',
        component: () => import('@/views/Salary/index.vue'),
        redirect: '/salary/my',
        meta: { title: '薪资管理', icon: 'Money' },
        children: [
          {
            path: 'my',
            name: 'SalaryMy',
            component: () => import('@/views/Salary/MyList.vue'),
            meta: { title: '我的薪资' }
          },
          {
            path: 'manage',
            name: 'SalaryManage',
            component: () => import('@/views/Salary/Manage.vue'),
            meta: { title: '薪资管理', permission: 'salary:manage' }
          }
        ]
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics/index.vue'),
        redirect: '/statistics/overview',
        meta: { title: '统计报表', icon: 'DataAnalysis' },
        children: [
          {
            path: 'overview',
            name: 'StatisticsOverview',
            component: () => import('@/views/Statistics/Overview.vue'),
            meta: { title: '数据概览' }
          },
          {
            path: 'attendance',
            name: 'StatisticsAttendance',
            component: () => import('@/views/Statistics/Attendance.vue'),
            meta: { title: '考勤统计' }
          }
        ]
      },
      {
        path: 'system',
        name: 'System',
        component: () => import('@/views/System/index.vue'),
        redirect: '/system/user',
        meta: { title: '系统管理', icon: 'Setting' },
        children: [
          {
            path: 'user',
            name: 'SystemUser',
            component: () => import('@/views/System/UserList.vue'),
            meta: { title: '用户管理', permission: 'system:user' }
          },
          {
            path: 'role',
            name: 'SystemRole',
            component: () => import('@/views/System/RoleList.vue'),
            meta: { title: '角色管理', permission: 'system:role' }
          },
          {
            path: 'menu',
            name: 'SystemMenu',
            component: () => import('@/views/System/MenuList.vue'),
            meta: { title: '菜单管理', permission: 'system:menu' }
          }
        ]
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: { title: '404', requiresAuth: false }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    }
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const token = getToken()

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 员工管理系统`
  }

  // 不需要认证的页面直接放行
  if (to.meta.requiresAuth === false) {
    if (to.path === '/login' && token) {
      next('/')
    } else {
      next()
    }
    return
  }

  // 没有 token，跳转到登录页
  if (!token) {
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
    return
  }

  // 有 token 但没有用户信息，先获取用户信息
  if (!userStore.userInfo) {
    try {
      await userStore.getUserInfo()
      next({ ...to, replace: true })
    } catch (error) {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
    }
    return
  }

  // 权限验证
  if (to.meta.permission) {
    if (userStore.hasPermission(to.meta.permission)) {
      next()
    } else {
      // 无权限，跳转到首页
      next('/')
    }
    return
  }

  next()
})

export default router
