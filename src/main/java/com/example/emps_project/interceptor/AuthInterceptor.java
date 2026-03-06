package com.example.emps_project.interceptor;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.BusinessException;
import com.example.emps_project.common.ResultCode;
import com.example.emps_project.security.JwtUtil;
import com.example.emps_project.security.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 验证用户登录状态
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception {
        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 获取方法或类上的 @RequireLogin 注解
        RequireLogin requireLogin = handlerMethod.getMethodAnnotation(RequireLogin.class);
        if (requireLogin == null) {
            requireLogin = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
        }

        // 如果没有注解或注解的value为false，放行
        if (requireLogin == null || !requireLogin.value()) {
            return true;
        }

        // 从Header获取Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        // 验证Token格式
        if (!token.startsWith("Bearer ")) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token格式错误");
        }

        // 去掉 "Bearer " 前缀
        token = token.substring(7);

        // 验证Token有效性
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token已过期或无效");
        }

        // 解析Token，获取用户信息
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            // 存入ThreadLocal供后续使用
            LoginUser.setUserId(userId);
            LoginUser.setUsername(username);
            LoginUser.setToken(token);

            log.debug("用户认证成功: userId={}, username={}", userId, username);
        } catch (Exception e) {
            log.error("Token解析失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token解析失败");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception ex) throws Exception {
        // 清除ThreadLocal，防止内存泄漏
        LoginUser.clear();
    }
}
