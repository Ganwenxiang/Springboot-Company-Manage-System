package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色表实体类
 */
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 角色名称 */
    private String roleName;

    /** 角色编码 */
    private String roleCode;

    /** 角色描述 */
    private String description;

    /** 状态:1正常,0停用 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // ==================== 非数据库字段 ====================

    /** 菜单ID列表（用于前端展示和更新） */
    private Long[] menuIds;
}
