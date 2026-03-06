package com.example.emps_project.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息VO
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 关联员工ID
     */
    private Long empId;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 菜单树
     */
    private List<?> menuTree;

    /**
     * 是否管理员
     */
    private Boolean isAdmin;
}
