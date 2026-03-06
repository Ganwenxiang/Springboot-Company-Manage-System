import request from '@/utils/request'

/**
 * 获取我的请假申请
 */
export function getMyLeaveRequests() {
  return request({
    url: '/leave/my-requests',
    method: 'get'
  })
}

/**
 * 提交请假申请
 */
export function submitLeave(data) {
  return request({
    url: '/leave',
    method: 'post',
    data
  })
}

/**
 * 撤销请假申请
 */
export function cancelLeave(id) {
  return request({
    url: `/leave/${id}/cancel`,
    method: 'put'
  })
}

/**
 * 获取待审批列表
 */
export function getPendingLeave() {
  return request({
    url: '/leave/pending',
    method: 'get'
  })
}

/**
 * 审批通过
 */
export function approveLeave(id) {
  return request({
    url: `/leave/${id}/approve`,
    method: 'put'
  })
}

/**
 * 审批拒绝
 */
export function rejectLeave(id, remark) {
  return request({
    url: `/leave/${id}/reject`,
    method: 'put',
    data: { remark }
  })
}

/**
 * 管理端查询请假记录
 */
export function getLeaveList(params) {
  return request({
    url: '/leave',
    method: 'get',
    params
  })
}
