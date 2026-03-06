package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 员工状态变更记录表实体类
 */
@Data
public class SysEmpStatusLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long empId;

    /** 类型：1入职 2调岗 3离职 4复职 */
    private Integer changeType;

    /** 原部门ID */
    private Long beforeDeptId;

    /** 新部门ID */
    private Long afterDeptId;

    /** 原职位 */
    private String beforePosition;

    /** 新职位 */
    private String afterPosition;

    /** 变更日期 */
    private LocalDate changeDate;

    /** 变更原因 */
    private String reason;

    /** 操作人ID */
    private Long operatorId;

    /** 创建时间 */
    private Date createTime;

    // ==================== 非数据库字段 ====================

    /** 员工姓名（用于查询显示） */
    private String empName;

    /** 原部门名称（用于查询显示） */
    private String beforeDeptName;

    /** 新部门名称（用于查询显示） */
    private String afterDeptName;

    /** 操作人姓名（用于查询显示） */
    private String operatorName;

    /** 变更类型描述 */
    private String changeTypeDesc;
}
