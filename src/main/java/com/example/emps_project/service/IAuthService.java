package com.example.emps_project.service;

import com.example.emps_project.entity.SysUser;

import java.util.List;

/**
 * 认证服务接口
 */
public interface IAuthService {

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前用户信息
     */
    SysUser getCurrentUser();

    /**
     * 根据Token获取用户信息
     */
    SysUser getUserByToken(String token);

    /**
     * 获取用户的菜单树
     */
    List<?> getMenuTree(Long userId);

    /**
     * 获取用户的权限列表
     */
    List<String> getPermissions(Long userId);

    /**
     * 刷新Token
     */
    String refreshToken(String token);
}
