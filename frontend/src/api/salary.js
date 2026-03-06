import request from '@/utils/request'

/**
 * 获取我的薪资记录
 */
export function getMySalaries() {
  return request({
    url: '/salary/my-salaries',
    method: 'get'
  })
}

/**
 * 生成月度薪资
 */
export function calculateMonthlySalary(data) {
  return request({
    url: '/salary/calculate',
    method: 'post',
    data
  })
}

/**
 * 管理端查询薪资记录
 */
export function getSalaryList(params) {
  return request({
    url: '/salary',
    method: 'get',
    params
  })
}

/**
 * 获取薪资详情
 */
export function getSalaryDetail(id) {
  return request({
    url: `/salary/${id}`,
    method: 'get'
  })
}

/**
 * 调整薪资
 */
export function updateSalary(data) {
  return request({
    url: `/salary/${data.id}`,
    method: 'put',
    data
  })
}

/**
 * 标记已发放
 */
export function markAsPaid(id) {
  return request({
    url: `/salary/${id}/pay`,
    method: 'put'
  })
}

/**
 * 删除薪资记录
 */
export function deleteSalary(id) {
  return request({
    url: `/salary/${id}`,
    method: 'delete'
  })
}

/**
 * 薪资汇总统计
 */
export function getSalarySummary(salaryMonth) {
  return request({
    url: '/salary/summary',
    method: 'get',
    params: { salaryMonth }
  })
}
