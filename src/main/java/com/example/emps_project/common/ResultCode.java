package com.example.emps_project.common;

import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    ERROR(500, "系统错误"),
    PARAM_ERROR(400, "参数错误"),
    BUSINESS_ERROR(600, "业务异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
