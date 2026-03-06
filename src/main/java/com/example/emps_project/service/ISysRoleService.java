package com.example.emps_project.service;

import com.example.emps_project.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface ISysRoleService {

    /**
     * 根据ID查询角色
     */
    SysRole selectById(Long id);

    /**
     * 根据角色编码查询角色
     */
    SysRole selectByRoleCode(String roleCode);

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
    int deleteById(Long id);

    /**
     * 批量删除角色
     */
    int deleteByIds(List<Long> ids);

    /**
     * 保存角色菜单关联
     */
    void saveRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 查询用户的所有角色
     */
    List<SysRole> selectRolesByUserId(Long userId);
}
