package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 薪资记录表实体类
 */
@Data
public class SysSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long empId;

    /** 薪资月份 yyyy-MM */
    private String salaryMonth;

    /** 基本工资 */
    private BigDecimal baseSalary;

    /** 岗位工资 */
    private BigDecimal positionSalary;

    /** 加班费 */
    private BigDecimal overtimePay;

    /** 奖金 */
    private BigDecimal bonus;

    /** 补贴 */
    private BigDecimal subsidy;

    /** 考勤扣款 */
    private BigDecimal attendanceDeduction;

    /** 社保 */
    private BigDecimal socialSecurity;

    /** 个税 */
    private BigDecimal tax;

    /** 应发工资 */
    private BigDecimal totalSalary;

    /** 实发工资 */
    private BigDecimal actualSalary;

    /** 状态：0未发放 1已发放 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    // ==================== 非数据库字段 ====================

    /** 员工姓名（用于查询显示） */
    private String empName;

    /** 员工工号（用于查询显示） */
    private String empNo;

    /** 部门名称（用于查询显示） */
    private String deptName;

    /** 状态描述 */
    private String statusDesc;
}
