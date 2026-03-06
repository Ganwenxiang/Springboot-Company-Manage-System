import request from '@/utils/request'

/**
 * 获取我的加班记录
 */
export function getMyOvertimeRecords() {
  return request({
    url: '/overtime/my-records',
    method: 'get'
  })
}

/**
 * 提交加班申请
 */
export function submitOvertime(data) {
  return request({
    url: '/overtime',
    method: 'post',
    data
  })
}

/**
 * 获取待审批列表
 */
export function getPendingOvertime() {
  return request({
    url: '/overtime/pending',
    method: 'get'
  })
}

/**
 * 审批通过
 */
export function approveOvertime(id) {
  return request({
    url: `/overtime/${id}/approve`,
    method: 'put'
  })
}

/**
 * 审批拒绝
 */
export function rejectOvertime(id) {
  return request({
    url: `/overtime/${id}/reject`,
    method: 'put'
  })
}

/**
 * 管理端查询加班记录
 */
export function getOvertimeList(params) {
  return request({
    url: '/overtime',
    method: 'get',
    params
  })
}

/**
 * 删除加班记录
 */
export function deleteOvertime(id) {
  return request({
    url: `/overtime/${id}`,
    method: 'delete'
  })
}
