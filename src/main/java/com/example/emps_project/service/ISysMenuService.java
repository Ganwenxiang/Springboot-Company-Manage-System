package com.example.emps_project.service;

import com.example.emps_project.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface ISysMenuService {

    /**
     * 根据ID查询菜单
     */
    SysMenu selectById(Long id);

    /**
     * 查询菜单列表
     */
    List<SysMenu> selectList(SysMenu sysMenu);

    /**
     * 查询所有菜单（不分页）
     */
    List<SysMenu> selectAll();

    /**
     * 查询菜单树
     */
    List<SysMenu> selectMenuTree();

    /**
     * 查询用户的菜单树
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 查询用户的权限列表
     */
    List<String> selectPermissionsByUserId(Long userId);

    /**
     * 新增菜单
     */
    int insert(SysMenu sysMenu);

    /**
     * 更新菜单
     */
    int update(SysMenu sysMenu);

    /**
     * 删除菜单
     */
    int deleteById(Long id);

    /**
     * 构建菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menuList);
}
