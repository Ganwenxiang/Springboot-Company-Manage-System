import request from '@/utils/request'

/**
 * 获取首页概览数据
 */
export function getOverviewData() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

/**
 * 获取员工分布统计
 */
export function getEmpDistribution() {
  return request({
    url: '/statistics/emp-distribution',
    method: 'get'
  })
}

/**
 * 获取考勤汇总统计
 */
export function getAttendanceSummary(params) {
  return request({
    url: '/statistics/attendance-summary',
    method: 'get',
    params
  })
}

/**
 * 获取部门考勤对比
 */
export function getDeptAttendance(params) {
  return request({
    url: '/statistics/dept-attendance',
    method: 'get',
    params
  })
}
