package com.example.emps_project.service;

import com.example.emps_project.entity.SysUser;

import java.util.List;

/**
 * 用户服务接口
 */
public interface ISysUserService {

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(String username);

    /**
     * 根据ID查询用户
     */
    SysUser selectById(Long id);

    /**
     * 查询用户列表
     */
    List<SysUser> selectList(SysUser sysUser);

    /**
     * 新增用户
     */
    int insert(SysUser sysUser);

    /**
     * 更新用户
     */
    int update(SysUser sysUser);

    /**
     * 删除用户
     */
    int deleteById(Long id);

    /**
     * 批量删除用户
     */
    int deleteByIds(List<Long> ids);

    /**
     * 重置密码
     */
    int resetPassword(Long id, String newPassword);

    /**
     * 修改密码
     */
    int changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 更新最后登录信息
     */
    void updateLastLoginInfo(Long id, String ip);

    /**
     * 保存用户角色关联
     */
    void saveUserRoles(Long userId, List<Long> roleIds);

    /**
     * 查询用户的角色ID列表
     */
    List<Long> selectRoleIdsByUserId(Long userId);
}
