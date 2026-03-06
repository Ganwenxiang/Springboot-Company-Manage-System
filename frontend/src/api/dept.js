import request from '@/utils/request'

/**
 * 获取部门树
 */
export function getDeptTree() {
  return request({
    url: '/depts',
    method: 'get'
  })
}

/**
 * 新增部门
 */
export function addDept(data) {
  return request({
    url: '/depts',
    method: 'post',
    data
  })
}

/**
 * 更新部门
 */
export function updateDept(data) {
  return request({
    url: '/depts',
    method: 'put',
    data
  })
}

/**
 * 删除部门
 */
export function deleteDept(id) {
  return request({
    url: `/depts/${id}`,
    method: 'delete'
  })
}
