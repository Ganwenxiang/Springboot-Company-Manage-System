package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.dto.ChangePasswordDTO;
import com.example.emps_project.dto.LoginDTO;
import com.example.emps_project.dto.UpdateProfileDTO;
import com.example.emps_project.dto.UserInfoVO;
import com.example.emps_project.entity.SysRole;
import com.example.emps_project.entity.SysUser;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.service.IAuthService;
import com.example.emps_project.service.ISysRoleService;
import com.example.emps_project.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证授权控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    // 用户登录验证 | Spring MVC、Spring Security JWT认证
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        // 参数校验
        if (!StringUtils.hasText(loginDTO.getUsername()) || !StringUtils.hasText(loginDTO.getPassword())) {
            return Result.error("用户名或密码不能为空");
        }

        try {
            // 登录验证
            String token = authService.login(loginDTO.getUsername(), loginDTO.getPassword());

            // 获取用户ID
            Long userId = LoginUser.getUserId();

            // 更新最后登录信息
            String ip = getClientIp(request);
            sysUserService.updateLastLoginInfo(userId, ip);

            // 返回Token和用户信息
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", userId);

            return Result.success("登录成功", data);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 用户登出 | 自定义RequireLogin注解、Spring Security
    @PostMapping("/logout")
    @RequireLogin
    public Result<Void> logout() {
        authService.logout();
        return Result.ok("登出成功");
    }

    // 获取当前用户信息（角色、权限、菜单树） | Spring Security、Stream API
    @GetMapping("/info")
    @RequireLogin
    public Result<UserInfoVO> getUserInfo() {
        try {
            //用户成功登录后，系统就存入了用户的id和Token
            Long userId = LoginUser.getUserId();

            // 获取用户信息，每次都要验证
            SysUser user = sysUserService.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 获取用户角色
            List<SysRole> roles = sysRoleService.selectRolesByUserId(userId);
//            List<String> roleCodes = roles.stream()
//                    .map(SysRole::getRoleCode)
//                    .collect(Collectors.toList());

            //暂存，用的时候再取
            List<String> roleCodes= new ArrayList<>();
            for ( SysRole role : roles) {
                roleCodes.add(role.getRoleCode());
            }

            // 获取用户权限
            List<String> permissions = authService.getPermissions(userId);

            // 获取用户菜单树
            List<?> menuTree = authService.getMenuTree(userId);

            // 判断是否管理员
            boolean isAdmin = roleCodes.contains("admin");

            // 封装返回数据
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            userInfoVO.setUserId(user.getId());
            userInfoVO.setRoles(roleCodes);
            userInfoVO.setPermissions(permissions);
            userInfoVO.setMenuTree(menuTree);
            userInfoVO.setIsAdmin(isAdmin);

            return Result.success(userInfoVO);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败");
        }
    }

    // 更新个人信息 | MyBatis更新
    @PutMapping("/profile")
    @RequireLogin
    public Result<Void> updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        try {
            Long userId = LoginUser.getUserId();

            SysUser sysUser = new SysUser();
            sysUser.setId(userId);
            sysUser.setNickname(updateProfileDTO.getNickname());
            sysUser.setAvatar(updateProfileDTO.getAvatar());

            int result = sysUserService.update(sysUser);
            if (result > 0) {
                return Result.ok("个人信息更新成功");
            } else {
                return Result.error("个人信息更新失败");
            }
        } catch (Exception e) {
            log.error("更新个人信息失败: {}", e.getMessage());
            return Result.error("更新个人信息失败");
        }
    }

    // 修改用户密码 | Spring Security密码加密
    @PutMapping("/password")
    @RequireLogin
    public Result<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        // 参数校验
        if (!StringUtils.hasText(changePasswordDTO.getOldPassword()) ||
            !StringUtils.hasText(changePasswordDTO.getNewPassword())) {
            return Result.error("密码不能为空");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return Result.error("两次密码输入不一致");
        }

        try {
            Long userId = LoginUser.getUserId();
            int result = sysUserService.changePassword(userId, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());

            if (result > 0) {
                return Result.ok("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 刷新JWT令牌 | Spring Security JWT
    @PostMapping("/refresh")
    @RequireLogin
    public Result<Map<String, String>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
            String newToken = authService.refreshToken(token);

            Map<String, String> data = new HashMap<>();
            data.put("token", newToken);

            return Result.success("Token刷新成功", data);
        } catch (Exception e) {
            log.error("Token刷新失败: {}", e.getMessage());
            return Result.error("Token刷新失败");
        }
    }

    // 获取客户端真实IP（支持代理转发） | HttpServletRequest、Header解析
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
