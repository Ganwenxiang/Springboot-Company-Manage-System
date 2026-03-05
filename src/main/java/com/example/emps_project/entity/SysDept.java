package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门表实体类
 */
@Data // Lombok 注解：自动生成 Getter, Setter, toString 等
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 父部门ID */
    private Long parentId;

    /** 部门名称 */
    private String deptName;

    /** 显示顺序 */
    private Integer orderNum;

    /** 状态:1正常,0停用 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // 下面是业务字段，数据库中不存在，但返回给前端时需要

    /** 子部门列表 (用于生成树形结构) */
    private List<SysDept> children;
}