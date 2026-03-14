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

    // 根据ID查询用户角色关联 | MyBatis select
    SysUserRole selectById(@Param("id") Long id);

    // 查询用户的角色列表 | MyBatis select
    List<SysUserRole> selectByUserId(@Param("userId") Long userId);

    // 查询角色的用户列表 | MyBatis select
    List<SysUserRole> selectByRoleId(@Param("roleId") Long roleId);

    // 新增用户角色关联 | MyBatis insert
    int insert(SysUserRole sysUserRole);

    // 批量新增用户角色关联 | MyBatis批量insert
    int insertBatch(@Param("list") List<SysUserRole> list);

    // 删除用户角色关联 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 删除用户的所有角色关联 | MyBatis delete
    int deleteByUserId(@Param("userId") Long userId);

    // 删除角色的所有用户关联 | MyBatis delete
    int deleteByRoleId(@Param("roleId") Long roleId);

    // 统计用户的角色数量 | MyBatis count聚合查询
    int countByUserId(@Param("userId") Long userId);
}
