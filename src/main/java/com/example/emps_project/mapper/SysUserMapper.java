package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface SysUserMapper {

    // 根据用户名查询用户 | MyBatis select
    SysUser selectByUsername(@Param("username") String username);

    // 根据ID查询用户 | MyBatis select
    SysUser selectById(@Param("id") Long id);

    // 查询用户列表 | MyBatis动态SQL
    List<SysUser> selectList(SysUser sysUser);

    // 新增用户 | MyBatis insert
    int insert(SysUser sysUser);

    // 更新用户 | MyBatis update
    int update(SysUser sysUser);

    // 删除用户 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 批量删除用户 | MyBatis批量删除
    int deleteByIds(@Param("ids") List<Long> ids);

    // 查询用户的角色ID列表 | MyBatis关联查询
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    // 保存用户角色关联 | MyBatis insert
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    // 删除用户角色关联 | MyBatis delete
    int deleteUserRoleByUserId(@Param("userId") Long userId);

    // 更新最后登录信息（IP、时间） | MyBatis update
    int updateLastLoginInfo(@Param("id") Long id, @Param("lastLoginTime") String lastLoginTime, @Param("lastLoginIp") String lastLoginIp);

    // 重置密码 | MyBatis update
    int resetPassword(@Param("id") Long id, @Param("password") String password);
}
