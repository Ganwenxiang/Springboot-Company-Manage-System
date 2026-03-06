package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色菜单关联表实体类
 */
@Data
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;

    /** 创建时间 */
    private Date createTime;
}
