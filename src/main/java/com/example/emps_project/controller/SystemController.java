package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysMenu;
import com.example.emps_project.entity.SysRole;
import com.example.emps_project.entity.SysUser;
import com.example.emps_project.service.ISysMenuService;
import com.example.emps_project.service.ISysRoleService;
import com.example.emps_project.service.ISysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/system")
@RequireLogin
public class SystemController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService sysMenuService;

    // ==================== 用户管理 ====================

    // 分页查询用户列表 | PageHelper分页、动态条件查询
    @GetMapping("/users")
    public Result<PageInfo<SysUser>> userList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username,
            String nickname,
            Integer status) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysUser query = new SysUser();
            query.setUsername(username);
            query.setNickname(nickname);
            query.setStatus(status);
            List<SysUser> list = sysUserService.selectList(query);

            // 查询用户的角色ID
            for (SysUser user : list) {
                List<Long> roleIds = sysUserService.selectRoleIdsByUserId(user.getId());
                user.setRoleIds(roleIds.toArray(new Long[0]));
            }

            PageInfo<SysUser> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage());
            return Result.error("查询用户列表失败");
        }
    }

    // 根据ID查询用户（含角色） | MyBatis
    @GetMapping("/users/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        try {
            SysUser user = sysUserService.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 查询用户的角色ID
            List<Long> roleIds = sysUserService.selectRoleIdsByUserId(id);
            user.setRoleIds(roleIds.toArray(new Long[0]));

            // 隐藏密码
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            log.error("查询用户失败: {}", e.getMessage());
            return Result.error("查询用户失败");
        }
    }

    // 新增用户（含角色关联） | MyBatis、事务处理
    @PostMapping("/users")
    public Result<Void> addUser(@RequestBody SysUser user) {
        try {
            int result = sysUserService.insert(user);
            if (result > 0) {
                // 保存用户角色关联
                if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
                    sysUserService.saveUserRoles(user.getId(), List.of(user.getRoleIds()));
                }
                return Result.ok("新增用户成功");
            }
            return Result.error("新增用户失败");
        } catch (Exception e) {
            log.error("新增用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 更新用户信息（含角色关联） | MyBatis
    @PutMapping("/users")
    public Result<Void> updateUser(@RequestBody SysUser user) {
        try {
            int result = sysUserService.update(user);
            if (result > 0) {
                // 更新用户角色关联
                if (user.getRoleIds() != null) {
                    sysUserService.saveUserRoles(user.getId(), List.of(user.getRoleIds()));
                }
                return Result.ok("更新用户成功");
            }
            return Result.error("更新用户失败");
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 删除用户 | MyBatis
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        try {
            int result = sysUserService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除用户成功");
            }
            return Result.error("删除用户失败");
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 重置用户密码为默认值 | Spring Security密码加密
    @PutMapping("/users/{id}/reset-pwd")
    public Result<Void> resetPassword(@PathVariable Long id) {
        try {
            int result = sysUserService.resetPassword(id, null);
            if (result > 0) {
                return Result.ok("密码重置成功，新密码为：123456");
            }
            return Result.error("密码重置失败");
        } catch (Exception e) {
            log.error("重置密码失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // ==================== 角色管理 ====================

    // 分页查询角色列表 | PageHelper分页、动态条件查询
    @GetMapping("/roles")
    public Result<Map<String, Object>> roleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String roleName,
            Integer status) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysRole query = new SysRole();
            query.setRoleName(roleName);
            query.setStatus(status);
            List<SysRole> list = sysRoleService.selectList(query);
            PageInfo<SysRole> pageInfo = new PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("list", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            return Result.success(data);
        } catch (Exception e) {
            log.error("查询角色列表失败: {}", e.getMessage());
            return Result.error("查询角色列表失败");
        }
    }

    // 根据ID查询角色（含菜单权限） | MyBatis
    @GetMapping("/roles/{id}")
    public Result<Map<String, Object>> getRoleById(@PathVariable Long id) {
        try {
            SysRole role = sysRoleService.selectById(id);
            if (role == null) {
                return Result.error("角色不存在");
            }

            // 查询角色的菜单ID
            List<Long> menuIds = sysRoleService.selectMenuIdsByRoleId(id);
            role.setMenuIds(menuIds.toArray(new Long[0]));

            Map<String, Object> data = new HashMap<>();
            data.put("role", role);
            data.put("menuIds", menuIds);

            return Result.success(data);
        } catch (Exception e) {
            log.error("查询角色失败: {}", e.getMessage());
            return Result.error("查询角色失败");
        }
    }

    // 新增角色（含菜单权限关联） | MyBatis、事务处理
    @PostMapping("/roles")
    public Result<Void> addRole(@RequestBody SysRole role) {
        try {
            int result = sysRoleService.insert(role);
            if (result > 0) {
                // 保存角色菜单关联
                if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
                    sysRoleService.saveRoleMenus(role.getId(), List.of(role.getMenuIds()));
                }
                return Result.ok("新增角色成功");
            }
            return Result.error("新增角色失败");
        } catch (Exception e) {
            log.error("新增角色失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 更新角色（含菜单权限关联） | MyBatis
    @PutMapping("/roles")
    public Result<Void> updateRole(@RequestBody SysRole role) {
        try {
            int result = sysRoleService.update(role);
            if (result > 0) {
                // 更新角色菜单关联
                if (role.getMenuIds() != null) {
                    sysRoleService.saveRoleMenus(role.getId(), List.of(role.getMenuIds()));
                }
                return Result.ok("更新角色成功");
            }
            return Result.error("更新角色失败");
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 删除角色 | MyBatis
    @DeleteMapping("/roles/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        try {
            int result = sysRoleService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除角色成功");
            }
            return Result.error("删除角色失败");
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // ==================== 菜单管理 ====================

    // 查询当前用户的菜单树 | Spring Security、递归查询
    @GetMapping("/menus")
    public Result<List<?>> menuTree() {
        try {
            Long userId = com.example.emps_project.security.LoginUser.getUserId();
            List<?> menuTree = sysMenuService.selectMenuTreeByUserId(userId);
            return Result.success(menuTree);
        } catch (Exception e) {
            log.error("查询菜单树失败: {}", e.getMessage());
            return Result.error("查询菜单树失败");
        }
    }

    // 查询所有菜单（管理端） | 递归查询
    @GetMapping("/menus/all")
    public Result<List<?>> allMenus() {
        try {
            List<?> menuTree = sysMenuService.selectMenuTree();
            return Result.success(menuTree);
        } catch (Exception e) {
            log.error("查询所有菜单失败: {}", e.getMessage());
            return Result.error("查询所有菜单失败");
        }
    }

    // 根据ID查询菜单 | MyBatis
    @GetMapping("/menus/{id}")
    public Result<SysMenu> getMenuById(@PathVariable Long id) {
        try {
            SysMenu menu = sysMenuService.selectById(id);
            if (menu == null) {
                return Result.error("菜单不存在");
            }
            return Result.success(menu);
        } catch (Exception e) {
            log.error("查询菜单失败: {}", e.getMessage());
            return Result.error("查询菜单失败");
        }
    }

    // 新增菜单 | MyBatis
    @PostMapping("/menus")
    public Result<Void> addMenu(@RequestBody SysMenu menu) {
        try {
            int result = sysMenuService.insert(menu);
            if (result > 0) {
                return Result.ok("新增菜单成功");
            }
            return Result.error("新增菜单失败");
        } catch (Exception e) {
            log.error("新增菜单失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 更新菜单 | MyBatis
    @PutMapping("/menus")
    public Result<Void> updateMenu(@RequestBody SysMenu menu) {
        try {
            int result = sysMenuService.update(menu);
            if (result > 0) {
                return Result.ok("更新菜单成功");
            }
            return Result.error("更新菜单失败");
        } catch (Exception e) {
            log.error("更新菜单失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 删除菜单 | MyBatis
    @DeleteMapping("/menus/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        try {
            int result = sysMenuService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除菜单成功");
            }
            return Result.error("删除菜单失败");
        } catch (Exception e) {
            log.error("删除菜单失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 查询角色的菜单权限 | MyBatis
    @GetMapping("/role/{roleId}/menus")
    public Result<List<Long>> getRoleMenus(@PathVariable Long roleId) {
        try {
            List<Long> menuIds = sysRoleService.selectMenuIdsByRoleId(roleId);
            return Result.success(menuIds);
        } catch (Exception e) {
            log.error("查询角色菜单失败: {}", e.getMessage());
            return Result.error("查询角色菜单失败");
        }
    }

    // 分配角色菜单权限 | MyBatis、事务处理
    @PutMapping("/role/{roleId}/menus")
    public Result<Void> assignRoleMenus(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        try {
            sysRoleService.saveRoleMenus(roleId, menuIds);
            return Result.ok("分配权限成功");
        } catch (Exception e) {
            log.error("分配权限失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
