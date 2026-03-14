package com.example.emps_project.service;

import com.example.emps_project.entity.SysUser;

import java.util.List;

/**
 * 用户服务接口
 */
public interface ISysUserService {

    // 根据用户名查询用户 | MyBatis
    SysUser selectByUsername(String username);

    // 根据ID查询用户 | MyBatis
    SysUser selectById(Long id);

    // 查询用户列表（支持动态条件） | MyBatis动态SQL
    List<SysUser> selectList(SysUser sysUser);

    // 新增用户 | MyBatis、Spring Security密码加密
    int insert(SysUser sysUser);

    // 更新用户 | MyBatis
    int update(SysUser sysUser);

    // 删除用户 | MyBatis
    int deleteById(Long id);

    // 批量删除用户 | MyBatis批量操作
    int deleteByIds(List<Long> ids);

    // 重置用户密码为默认值 | Spring Security密码加密
    int resetPassword(Long id, String newPassword);

    // 修改用户密码 | Spring Security密码验证与加密
    int changePassword(Long id, String oldPassword, String newPassword);

    // 更新用户最后登录信息（IP、时间） | MyBatis
    void updateLastLoginInfo(Long id, String ip);

    // 保存用户角色关联 | MyBatis、事务处理
    void saveUserRoles(Long userId, List<Long> roleIds);

    // 查询用户的角色ID列表 | MyBatis关联查询
    List<Long> selectRoleIdsByUserId(Long userId);
}
