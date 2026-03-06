package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单表实体类
 */
@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 父菜单ID：0表示顶级菜单 */
    private Long parentId;

    /** 菜单名称 */
    private String menuName;

    /** 类型：1目录 2菜单 3按钮 */
    private Integer menuType;

    /** 路由路径 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 菜单图标 */
    private String icon;

    /** 权限标识 */
    private String permission;

    /** 显示顺序 */
    private Integer orderNum;

    /** 状态:1正常,0停用 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // ==================== 非数据库字段 ====================

    /** 子菜单列表（用于生成树形结构） */
    private List<SysMenu> children;
}
