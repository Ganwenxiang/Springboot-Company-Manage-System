package com.example.emps_project.service;

import com.example.emps_project.entity.SysUser;

import java.util.List;

/**
 * 认证服务接口
 */
public interface IAuthService {

    // 用户登录验证，返回JWT Token | Spring Security JWT
    String login(String username, String password);

    // 用户登出，清除Token | Spring Security
    void logout();

    // 获取当前登录用户信息 | Spring Security SecurityContext
    SysUser getCurrentUser();

    // 根据Token解析获取用户信息 | JWT解析
    SysUser getUserByToken(String token);

    // 获取用户菜单树（含权限过滤） | 递归查询、MyBatis
    List<?> getMenuTree(Long userId);

    // 获取用户权限列表 | MyBatis关联查询
    List<String> getPermissions(Long userId);

    // 刷新JWT Token | JWT生成
    String refreshToken(String token);
}
