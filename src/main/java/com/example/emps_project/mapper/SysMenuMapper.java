package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper接口
 */
@Mapper
public interface SysMenuMapper {

    /**
     * 根据ID查询菜单
     */
    SysMenu selectById(@Param("id") Long id);

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
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);

    /**
     * 查询角色的菜单列表
     */
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询用户的权限列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

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
    int deleteById(@Param("id") Long id);

    /**
     * 查询子菜单数量
     */
    int countChildren(@Param("parentId") Long parentId);
}
