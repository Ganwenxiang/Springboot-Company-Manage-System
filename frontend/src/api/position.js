import request from '@/utils/request'

/**
 * 获取职位列表（分页）
 */
export function getPositionList(params) {
  return request({
    url: '/positions',
    method: 'get',
    params
  })
}

/**
 * 获取所有职位（不分页）
 */
export function getAllPositions() {
  return request({
    url: '/positions/all',
    method: 'get'
  })
}

/**
 * 新增职位
 */
export function addPosition(data) {
  return request({
    url: '/positions',
    method: 'post',
    data
  })
}

/**
 * 更新职位
 */
export function updatePosition(data) {
  return request({
    url: '/positions',
    method: 'put',
    data
  })
}

/**
 * 删除职位
 */
export function deletePosition(id) {
  return request({
    url: `/positions/${id}`,
    method: 'delete'
  })
}
