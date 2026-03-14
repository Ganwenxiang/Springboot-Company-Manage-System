package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.common.ResultCode;
import com.example.emps_project.entity.SysUser;
import com.example.emps_project.security.JwtUtil;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.security.PasswordEncoder;
import com.example.emps_project.service.IAuthService;
import com.example.emps_project.service.ISysMenuService;
import com.example.emps_project.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private JwtUtil jwtUtil;

    // 用户登录验证，返回JWT Token | Spring Security密码验证、JWT生成
    @Override
    public String login(String username, String password) {
        // 查询用户
        SysUser user = sysUserService.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }

        // 验证密码
        if (!PasswordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException("账号已停用");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        log.info("用户登录成功: username={}, userId={}", username, user.getId());

        return token;
    }

    // 用户登出，清除ThreadLocal | ThreadLocal
    @Override
    public void logout() {
        // 清除ThreadLocal中的用户信息
        LoginUser.clear();
        log.info("用户登出成功");
    }

    // 获取当前登录用户 | SecurityContext、MyBatis
    @Override
    public SysUser getCurrentUser() {
        Long userId = LoginUser.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return sysUserService.selectById(userId);
    }

    // 根据Token解析获取用户信息 | JWT解析、MyBatis
    @Override
    public SysUser getUserByToken(String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            return sysUserService.selectById(userId);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token无效");
        }
    }

    // 获取用户菜单树 | MyBatis关联查询
    @Override
    public List<?> getMenuTree(Long userId) {
        return sysMenuService.selectMenuTreeByUserId(userId);
    }

    // 获取用户权限列表 | MyBatis关联查询
    @Override
    public List<String> getPermissions(Long userId) {
        return sysMenuService.selectPermissionsByUserId(userId);
    }

    // 刷新JWT Token | JWT验证与生成
    @Override
    public String refreshToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);

        return jwtUtil.generateToken(userId, username);
    }
}
