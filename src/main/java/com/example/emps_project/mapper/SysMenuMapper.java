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

    // 根据ID查询菜单 | MyBatis select
    SysMenu selectById(@Param("id") Long id);

    // 查询菜单列表 | MyBatis动态SQL
    List<SysMenu> selectList(SysMenu sysMenu);

    // 查询所有菜单 | MyBatis select
    List<SysMenu> selectAll();

    // 查询菜单树 | MyBatis递归查询
    List<SysMenu> selectMenuTree();

    // 查询用户的菜单树 | MyBatis关联查询
    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);

    // 查询角色的菜单列表 | MyBatis关联查询
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Long roleId);

    // 查询用户的权限列表 | MyBatis关联查询
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    // 新增菜单 | MyBatis insert
    int insert(SysMenu sysMenu);

    // 更新菜单 | MyBatis update
    int update(SysMenu sysMenu);

    // 删除菜单 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 查询子菜单数量 | MyBatis count聚合查询
    int countChildren(@Param("parentId") Long parentId);
}
