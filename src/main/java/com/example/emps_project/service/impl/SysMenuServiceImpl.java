package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysMenu;
import com.example.emps_project.mapper.SysMenuMapper;
import com.example.emps_project.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单服务实现类
 */
@Slf4j
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    // 根据ID查询菜单 | MyBatis
    @Override
    public SysMenu selectById(Long id) {
        return sysMenuMapper.selectById(id);
    }

    // 查询菜单列表 | MyBatis
    @Override
    public List<SysMenu> selectList(SysMenu sysMenu) {
        return sysMenuMapper.selectList(sysMenu);
    }

    // 查询所有菜单 | MyBatis
    @Override
    public List<SysMenu> selectAll() {
        return sysMenuMapper.selectAll();
    }

    // 查询菜单树 | 递归查询
    @Override
    public List<SysMenu> selectMenuTree() {
        List<SysMenu> menuList = sysMenuMapper.selectAll();
        return buildMenuTree(menuList);
    }

    // 查询用户的菜单树（含权限过滤） | 递归查询、MyBatis关联查询
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menuList = sysMenuMapper.selectMenuTreeByUserId(userId);
        return buildMenuTree(menuList);
    }

    // 查询用户的权限列表 | MyBatis关联查询
    @Override
    public List<String> selectPermissionsByUserId(Long userId) {
        return sysMenuMapper.selectPermissionsByUserId(userId);
    }

    // 新增菜单 | MyBatis、事务处理
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysMenu sysMenu) {
        // 检查父菜单是否存在
        if (sysMenu.getParentId() != null && sysMenu.getParentId() != 0L) {
            SysMenu parentMenu = sysMenuMapper.selectById(sysMenu.getParentId());
            if (parentMenu == null) {
                throw new BusinessException("父菜单不存在");
            }
        }
        return sysMenuMapper.insert(sysMenu);
    }

    // 更新菜单 | MyBatis、事务处理
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysMenu sysMenu) {
        // 不允许修改父菜单为自身或其子菜单
        if (sysMenu.getParentId() != null && sysMenu.getParentId().equals(sysMenu.getId())) {
            throw new BusinessException("父菜单不能是自己");
        }
        return sysMenuMapper.update(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        // 检查是否有子菜单
        int count = sysMenuMapper.countChildren(id);
        if (count > 0) {
            throw new BusinessException("存在子菜单，不能删除");
        }
        return sysMenuMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        List<SysMenu> rootMenus = new ArrayList<>();

        // 找出所有顶级菜单（parentId为0或null）
        for (SysMenu menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId() == 0L) {
                rootMenus.add(menu);
            }
        }

        // 递归设置子菜单
        for (SysMenu rootMenu : rootMenus) {
            setChildren(rootMenu, menuList);
        }

        // 按order_num排序
        rootMenus.sort((m1, m2) -> {
            if (m1.getOrderNum() == null) return 1;
            if (m2.getOrderNum() == null) return -1;
            return m1.getOrderNum().compareTo(m2.getOrderNum());
        });

        return rootMenus;
    }

    /**
     * 递归设置子菜单
     */
    private void setChildren(SysMenu parentMenu, List<SysMenu> menuList) {
        List<SysMenu> children = new ArrayList<>();

        for (SysMenu menu : menuList) {
            if (parentMenu.getId().equals(menu.getParentId())) {
                children.add(menu);
            }
        }

        if (!children.isEmpty()) {
            parentMenu.setChildren(children);
            // 递归设置子菜单的子菜单
            for (SysMenu child : children) {
                setChildren(child, menuList);
            }
            // 按order_num排序
            children.sort((m1, m2) -> {
                if (m1.getOrderNum() == null) return 1;
                if (m2.getOrderNum() == null) return -1;
                return m1.getOrderNum().compareTo(m2.getOrderNum());
            });
        }
    }
}
