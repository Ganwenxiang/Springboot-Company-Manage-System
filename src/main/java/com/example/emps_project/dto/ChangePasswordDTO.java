package com.example.emps_project.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码DTO
 */
@Data
public class ChangePasswordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String confirmPassword;
}
