package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper接口
 */
@Mapper
public interface SysRoleMenuMapper {

    // 根据ID查询角色菜单关联 | MyBatis select
    SysRoleMenu selectById(@Param("id") Long id);

    // 查询角色的菜单列表 | MyBatis select
    List<SysRoleMenu> selectByRoleId(@Param("roleId") Long roleId);

    // 查询菜单的角色列表 | MyBatis select
    List<SysRoleMenu> selectByMenuId(@Param("menuId") Long menuId);

    // 新增角色菜单关联 | MyBatis insert
    int insert(SysRoleMenu sysRoleMenu);

    // 批量新增角色菜单关联 | MyBatis批量insert
    int insertBatch(@Param("list") List<SysRoleMenu> list);

    // 删除角色菜单关联 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 删除角色的所有菜单关联 | MyBatis delete
    int deleteByRoleId(@Param("roleId") Long roleId);

    // 删除菜单的所有角色关联 | MyBatis delete
    int deleteByMenuId(@Param("menuId") Long menuId);

    // 统计角色的菜单数量 | MyBatis count聚合查询
    int countByRoleId(@Param("roleId") Long roleId);
}
