package com.example.emps_project.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新个人信息DTO
 */
@Data
public class UpdateProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;
}