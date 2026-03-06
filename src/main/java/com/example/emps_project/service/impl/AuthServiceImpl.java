package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.common.ResultCode;
import com.example.emps_project.entity.SysMenu;
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

    @Override
    public void logout() {
        // 清除ThreadLocal中的用户信息
        LoginUser.clear();
        log.info("用户登出成功");
    }

    @Override
    public SysUser getCurrentUser() {
        Long userId = LoginUser.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return sysUserService.selectById(userId);
    }

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

    @Override
    public List<?> getMenuTree(Long userId) {
        return sysMenuService.selectMenuTreeByUserId(userId);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return sysMenuService.selectPermissionsByUserId(userId);
    }

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
