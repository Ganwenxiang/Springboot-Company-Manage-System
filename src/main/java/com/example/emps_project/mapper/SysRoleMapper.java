package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 */
@Mapper
public interface SysRoleMapper {

    // 根据ID查询角色 | MyBatis select
    SysRole selectById(@Param("id") Long id);

    // 根据角色编码查询角色 | MyBatis select
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    // 查询角色列表 | MyBatis动态SQL
    List<SysRole> selectList(SysRole sysRole);

    // 查询所有角色 | MyBatis select
    List<SysRole> selectAll();

    // 新增角色 | MyBatis insert
    int insert(SysRole sysRole);

    // 更新角色 | MyBatis update
    int update(SysRole sysRole);

    // 删除角色 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 批量删除角色 | MyBatis批量删除
    int deleteByIds(@Param("ids") List<Long> ids);

    // 查询角色的菜单ID列表 | MyBatis关联查询
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    // 保存角色菜单关联 | MyBatis insert
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    // 删除角色菜单关联 | MyBatis delete
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    // 查询用户的所有角色 | MyBatis关联查询
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
