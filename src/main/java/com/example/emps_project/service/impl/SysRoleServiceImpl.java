package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysRole;
import com.example.emps_project.mapper.SysRoleMapper;
import com.example.emps_project.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 角色服务实现类
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    // 根据ID查询角色 | MyBatis
    @Override
    public SysRole selectById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    // 根据角色编码查询角色 | MyBatis
    @Override
    public SysRole selectByRoleCode(String roleCode) {
        return sysRoleMapper.selectByRoleCode(roleCode);
    }

    // 查询角色列表 | MyBatis
    @Override
    public List<SysRole> selectList(SysRole sysRole) {
        return sysRoleMapper.selectList(sysRole);
    }

    // 查询所有角色 | MyBatis
    @Override
    public List<SysRole> selectAll() {
        return sysRoleMapper.selectAll();
    }

    // 新增角色 | MyBatis、事务处理
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysRole sysRole) {
        // 检查角色编码是否已存在
        if (StringUtils.hasText(sysRole.getRoleCode())) {
            SysRole existRole = sysRoleMapper.selectByRoleCode(sysRole.getRoleCode());
            if (existRole != null) {
                throw new BusinessException("角色编码已存在");
            }
        }
        return sysRoleMapper.insert(sysRole);
    }

    // 更新角色 | MyBatis、事务处理
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysRole sysRole) {
        // 不允许修改角色编码
        sysRole.setRoleCode(null);
        return sysRoleMapper.update(sysRole);
    }

    // 删除角色 | MyBatis、事务处理
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        // 不允许删除管理员角色
        if (id == 1L) {
            throw new BusinessException("不允许删除管理员角色");
        }
        // 删除角色前先删除角色菜单关联
        sysRoleMapper.deleteRoleMenuByRoleId(id);
        return sysRoleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> ids) {
        // 不允许删除管理员角色
        if (ids.contains(1L)) {
            throw new BusinessException("不允许删除管理员角色");
        }
        for (Long id : ids) {
            sysRoleMapper.deleteRoleMenuByRoleId(id);
        }
        return sysRoleMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        // 先删除角色的所有菜单关联
        sysRoleMapper.deleteRoleMenuByRoleId(roleId);

        // 保存新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                sysRoleMapper.insertRoleMenu(roleId, menuId);
            }
        }
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return sysRoleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }
}
