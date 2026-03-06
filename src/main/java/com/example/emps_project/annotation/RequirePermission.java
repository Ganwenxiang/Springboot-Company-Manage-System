package com.example.emps_project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要权限注解
 * 标记在需要特定权限才能访问的接口或方法上
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * 需要的权限标识，可以有多个
     */
    String[] value() default {};

    /**
     * 权限验证逻辑：AND表示需要所有权限，OR表示有任一权限即可
     */
    LogicalType logicalType() default LogicalType.AND;

    /**
     * 逻辑类型枚举
     */
    enum LogicalType {
        AND, // 需要所有权限
        OR   // 有任一权限即可
    }
}
