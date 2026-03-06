package com.example.emps_project.security;

/**
 * 登录用户上下文
 * 使用ThreadLocal存储当前请求的用户信息
 */
public class LoginUser {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    /**
     * 设置用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置用户名
     */
    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /**
     * 设置Token
     */
    public static void setToken(String token) {
        TOKEN.set(token);
    }

    /**
     * 获取Token
     */
    public static String getToken() {
        return TOKEN.get();
    }

    /**
     * 清除当前用户信息
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        TOKEN.remove();
    }
}
