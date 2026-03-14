package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 员工表实体类
 */
@Data
public class SysEmp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 部门ID */
    private Long deptId;

    /** 工号 */
    private String empNo;

    /** 姓名 */
    private String empName;

    /** 手机号 */
    private String phone;

    /** 职位 */
    private String jobTitle;

    /** 入职日期 */
    private Date entryDate;

    /** 创建时间 */
    private Date createTime;

    // ==================== 扩展字段 ====================

    /** 状态：1在职 2离职 3停职 */
    private Integer empStatus;

    /** 职位ID */
    private Long positionId;

    /** 邮箱 */
    private String email;

    /** 性别：1男 2女 */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 身份证号 */
    private String idCard;

    /** 居住地址 */
    private String address;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 紧急联系电话 */
    private String emergencyPhone;

    /** 入职状态：1试用 2正式 */
    private Integer entryStatus;

    /** 离职日期 */
    private LocalDate leaveDate;

    /** 离职原因 */
    private String leaveReason;

    // ==================== 非数据库字段 ====================

    /** 部门名称（用于前端显示） */
    private String deptName;

    /** 职位名称（用于前端显示） */
    private String positionName;

    /** 状态描述 */
    private String empStatusDesc;

    /** 入职状态描述 */
    private String entryStatusDesc;

    /** 性别描述 */
    private String genderDesc;

    // ==================== 搜索条件字段 (不映射到数据库) ====================

    /** 入职日期范围搜索 - 开始 */
    private Date entryDateStart;

    /** 入职日期范围搜索 - 结束 */
    private Date entryDateEnd;
}