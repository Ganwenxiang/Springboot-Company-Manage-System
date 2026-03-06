import request from '@/utils/request'

// ==================== 用户管理 ====================

/**
 * 获取用户列表
 */
export function getUserList(params) {
  return request({
    url: '/system/users',
    method: 'get',
    params
  })
}

/**
 * 新增用户
 */
export function addUser(data) {
  return request({
    url: '/system/users',
    method: 'post',
    data
  })
}

/**
 * 创建用户（别名）
 */
export const createUser = addUser

/**
 * 更新用户
 */
export function updateUser(data) {
  return request({
    url: '/system/users',
    method: 'put',
    data
  })
}

/**
 * 删除用户
 */
export function deleteUser(id) {
  return request({
    url: `/system/users/${id}`,
    method: 'delete'
  })
}

/**
 * 重置密码
 */
export function resetPassword(id, password) {
  return request({
    url: `/system/users/${id}/reset-pwd`,
    method: 'put',
    data: password ? { password } : {}
  })
}

/**
 * 分配用户角色
 */
export function assignRoles(data) {
  return request({
    url: '/system/users/assign-roles',
    method: 'put',
    data
  })
}

// ==================== 角色管理 ====================

/**
 * 获取角色列表
 */
export function getRoleList(params) {
  return request({
    url: '/system/roles',
    method: 'get',
    params
  })
}

/**
 * 新增角色
 */
export function addRole(data) {
  return request({
    url: '/system/roles',
    method: 'post',
    data
  })
}

/**
 * 创建角色（别名）
 */
export const createRole = addRole

/**
 * 更新角色
 */
export function updateRole(data) {
  return request({
    url: '/system/roles',
    method: 'put',
    data
  })
}

/**
 * 删除角色
 */
export function deleteRole(id) {
  return request({
    url: `/system/roles/${id}`,
    method: 'delete'
  })
}

// ==================== 菜单管理 ====================

/**
 * 获取菜单树（当前用户的可访问菜单）
 */
export function getMenuTree() {
  return request({
    url: '/system/menus',
    method: 'get'
  })
}

/**
 * 获取所有菜单（管理端）
 */
export function getAllMenus() {
  return request({
    url: '/system/menus/all',
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export function addMenu(data) {
  return request({
    url: '/system/menus',
    method: 'post',
    data
  })
}

/**
 * 创建菜单（别名）
 */
export const createMenu = addMenu

/**
 * 更新菜单
 */
export function updateMenu(data) {
  return request({
    url: '/system/menus',
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 */
export function deleteMenu(id) {
  return request({
    url: `/system/menus/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色拥有的菜单权限
 */
export function getRoleMenus(roleId) {
  return request({
    url: `/system/role/${roleId}/menus`,
    method: 'get'
  })
}

/**
 * 分配角色菜单权限
 */
export function assignRoleMenus(roleId, menuIds) {
  let data = {}
  if (typeof roleId === 'object') {
    data = roleId
  } else {
    data = { roleId, menuIds }
  }
  return request({
    url: `/system/role/${typeof roleId === 'object' ? roleId.roleId : roleId}/menus`,
    method: 'put',
    data: typeof roleId === 'object' ? { menuIds: roleId.menuIds } : { menuIds }
  })
}
