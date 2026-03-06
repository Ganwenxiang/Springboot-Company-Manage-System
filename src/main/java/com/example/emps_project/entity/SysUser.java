package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表实体类
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联员工ID */
    private Long empId;

    /** 登录账号 */
    private String username;

    /** 登录密码（加密） */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 状态:1正常,0停用 */
    private Integer status;

    /** 最后登录时间 */
    private Date lastLoginTime;

    /** 最后登录IP */
    private String lastLoginIp;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // ==================== 非数据库字段 ====================

    /** 角色ID列表（用于前端展示和更新） */
    private Long[] roleIds;

    /** 角色名称列表（查询时返回） */
    private String[] roleNames;
}
