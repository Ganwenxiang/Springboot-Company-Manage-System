package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色关联表实体类
 */
@Data
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

    /** 创建时间 */
    private Date createTime;
}
