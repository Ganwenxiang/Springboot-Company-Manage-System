package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 请假申请表实体类
 */
@Data
public class SysLeave implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long empId;

    /** 类型：1年假 2事假 3病假 4调休 */
    private Integer leaveType;

    /** 开始时间 */
    private Date startDate;

    /** 结束时间 */
    private Date endDate;

    /** 请假天数 */
    private BigDecimal leaveDays;

    /** 请假原因 */
    private String reason;

    /** 状态：0待审批 1已批准 2已拒绝 3已撤销 */
    private Integer status;

    /** 审批人ID */
    private Long approverId;

    /** 审批时间 */
    private Date approveTime;

    /** 审批备注 */
    private String approveRemark;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // ==================== 非数据库字段 ====================

    /** 员工姓名（用于查询显示） */
    private String empName;

    /** 员工工号（用于查询显示） */
    private String empNo;

    /** 部门名称（用于查询显示） */
    private String deptName;

    /** 审批人姓名（用于查询显示） */
    private String approverName;

    /** 请假类型描述 */
    private String leaveTypeDesc;

    /** 状态描述 */
    private String statusDesc;

    // ==================== 查询条件字段 (不映射到数据库) ====================

    /** 查询开始日期 */
    private Date startDateQuery;

    /** 查询结束日期 */
    private Date endDateQuery;
}
