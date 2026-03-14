package com.example.emps_project.service;

import com.example.emps_project.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface ISysMenuService {

    // 根据ID查询菜单 | MyBatis
    SysMenu selectById(Long id);

    // 查询菜单列表（支持动态条件） | MyBatis动态SQL
    List<SysMenu> selectList(SysMenu sysMenu);

    // 查询所有菜单（不分页） | MyBatis
    List<SysMenu> selectAll();

    // 查询菜单树 | 递归查询、MyBatis
    List<SysMenu> selectMenuTree();

    // 查询用户的菜单树（含权限过滤） | 递归查询、MyBatis关联查询
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    // 查询用户的权限列表 | MyBatis关联查询
    List<String> selectPermissionsByUserId(Long userId);

    // 新增菜单 | MyBatis
    int insert(SysMenu sysMenu);

    // 更新菜单 | MyBatis
    int update(SysMenu sysMenu);

    // 删除菜单 | MyBatis
    int deleteById(Long id);

    // 构建菜单树（递归） | Stream API
    List<SysMenu> buildMenuTree(List<SysMenu> menuList);
}
