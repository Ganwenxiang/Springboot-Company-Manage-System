package com.example.emps_project.service;

import com.example.emps_project.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface ISysRoleService {

    // 根据ID查询角色 | MyBatis
    SysRole selectById(Long id);

    // 根据角色编码查询角色 | MyBatis
    SysRole selectByRoleCode(String roleCode);

    // 查询角色列表（支持动态条件） | MyBatis动态SQL
    List<SysRole> selectList(SysRole sysRole);

    // 查询所有角色（不分页） | MyBatis
    List<SysRole> selectAll();

    // 新增角色 | MyBatis
    int insert(SysRole sysRole);

    // 更新角色 | MyBatis
    int update(SysRole sysRole);

    // 删除角色 | MyBatis
    int deleteById(Long id);

    // 批量删除角色 | MyBatis批量操作
    int deleteByIds(List<Long> ids);

    // 保存角色菜单关联 | MyBatis、事务处理
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    // 查询角色的菜单ID列表 | MyBatis关联查询
    List<Long> selectMenuIdsByRoleId(Long roleId);

    // 查询用户的所有角色 | MyBatis关联查询
    List<SysRole> selectRolesByUserId(Long userId);
}
