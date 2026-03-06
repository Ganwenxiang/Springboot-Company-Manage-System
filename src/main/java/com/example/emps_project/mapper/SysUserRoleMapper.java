package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 */
@Mapper
public interface SysUserRoleMapper {

    /**
     * 根据ID查询用户角色关联
     */
    SysUserRole selectById(@Param("id") Long id);

    /**
     * 查询用户的角色列表
     */
    List<SysUserRole> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询角色的用户列表
     */
    List<SysUserRole> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 新增用户角色关联
     */
    int insert(SysUserRole sysUserRole);

    /**
     * 批量新增用户角色关联
     */
    int insertBatch(@Param("list") List<SysUserRole> list);

    /**
     * 删除用户角色关联
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除用户的所有角色关联
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 删除角色的所有用户关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 统计用户的角色数量
     */
    int countByUserId(@Param("userId") Long userId);
}
