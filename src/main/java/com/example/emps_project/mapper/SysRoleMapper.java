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

    /**
     * 根据ID查询角色
     */
    SysRole selectById(@Param("id") Long id);

    /**
     * 根据角色编码查询角色
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色列表
     */
    List<SysRole> selectList(SysRole sysRole);

    /**
     * 查询所有角色（不分页）
     */
    List<SysRole> selectAll();

    /**
     * 新增角色
     */
    int insert(SysRole sysRole);

    /**
     * 更新角色
     */
    int update(SysRole sysRole);

    /**
     * 删除角色
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除角色
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 保存角色菜单关联
     */
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    /**
     * 删除角色菜单关联
     */
    int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询用户的所有角色
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
