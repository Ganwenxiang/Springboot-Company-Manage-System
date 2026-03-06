import request from '@/utils/request'

/**
 * 获取员工列表（分页）
 */
export function getEmployeeList(params) {
  return request({
    url: '/employees',
    method: 'get',
    params
  })
}

/**
 * 获取员工详情
 */
export function getEmployeeDetail(id) {
  return request({
    url: `/employees/${id}`,
    method: 'get'
  })
}

/**
 * 新增员工
 */
export function addEmployee(data) {
  return request({
    url: '/employees',
    method: 'post',
    data
  })
}

/**
 * 更新员工信息
 */
export function updateEmployee(data) {
  return request({
    url: '/employees',
    method: 'put',
    data
  })
}

/**
 * 删除员工
 */
export function deleteEmployee(id) {
  return request({
    url: `/employees/${id}`,
    method: 'delete'
  })
}

/**
 * 员工调岗
 */
export function transferEmployee(data) {
  return request({
    url: `/employees/${data.id}/transfer`,
    method: 'put',
    data
  })
}

/**
 * 员工离职
 */
export function resignEmployee(data) {
  return request({
    url: `/employees/${data.id}/resign`,
    method: 'put',
    data
  })
}

/**
 * 员工复职
 */
export function reinstateEmployee(id) {
  return request({
    url: `/employees/${id}/reinstate`,
    method: 'put'
  })
}

/**
 * 获取员工状态变更记录
 */
export function getEmployeeStatusLogs(empId) {
  return request({
    url: `/employees/status-logs/${empId}`,
    method: 'get'
  })
}
