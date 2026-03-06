import request from '@/utils/request'

/**
 * 获取今日考勤
 */
export function getMyToday() {
  return request({
    url: '/attendance/my-today',
    method: 'get'
  })
}

/**
 * 获取我的月度考勤记录
 */
export function getMyMonthly(params) {
  return request({
    url: '/attendance/monthly',
    method: 'get',
    params
  })
}

/**
 * 员工签到
 */
export function checkIn(data) {
  return request({
    url: '/attendance/check-in',
    method: 'post',
    data
  })
}

/**
 * 员工签退
 */
export function checkOut(data) {
  return request({
    url: '/attendance/check-out',
    method: 'post',
    data
  })
}

/**
 * 获取月度考勤汇总
 */
export function getMonthlyAttendance(year, month) {
  return request({
    url: '/attendance/monthly',
    method: 'get',
    params: { year, month }
  })
}

/**
 * 管理端查询考勤记录
 */
export function getAttendanceList(params) {
  return request({
    url: '/attendance',
    method: 'get',
    params
  })
}

/**
 * 修正考勤记录
 */
export function updateAttendance(data) {
  return request({
    url: `/attendance/${data.id}`,
    method: 'put',
    data
  })
}
