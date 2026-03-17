package com.example.emps_project.security;

/**
 * 登录用户上下文工具类
 *
 * <p>使用 ThreadLocal 存储当前请求的用户信息，每个线程独立，互不干扰。
 * 在 Web 应用中，每个 HTTP 请求由独立线程处理，因此可安全存储用户信息。
 *
 * <h3>工作流程：</h3>
 * <pre>
 * 请求到达 → 拦截器解析token并调用set方法存入 → 业务代码通过get方法获取 → 请求结束调用clear清理
 * </pre>
 *
 * <h3>为什么用静态类而非依赖注入？</h3>
 * <ul>
 *   <li>依赖注入：需要Spring管理生命周期，适合有复杂依赖的Bean</li>
 *   <li>静态ThreadLocal：可在任意位置快速获取上下文，避免层层传参</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 登录成功后设置
 * LoginUser.setUserId(user.getId());
 * LoginUser.setUsername(user.getUsername());
 *
 * // 业务代码中获取
 * Long currentUserId = LoginUser.getUserId();
 *
 * // 请求结束时清理（防止内存泄漏）
 * LoginUser.clear();
 * </pre>
 *
 * <p><b>注意：</b>必须在请求结束时调用 clear() 清理，否则会导致内存泄漏。
 */
public class LoginUser {

    // 用户ID、用户名、Token的线程本地存储
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
     *
     * <p>必须在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
        TOKEN.remove();
    }
}