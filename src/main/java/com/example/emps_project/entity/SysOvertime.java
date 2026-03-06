package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * 加班记录表实体类
 */
@Data
public class SysOvertime implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long empId;

    /** 加班日期 */
    private LocalDate overtimeDate;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 加班时长(小时) */
    private BigDecimal overtimeHours;

    /** 类型：1工作日 2周末 3节假日 */
    private Integer overtimeType;

    /** 加班原因 */
    private String reason;

    /** 状态：0待审批 1已批准 2已拒绝 */
    private Integer status;

    /** 审批人ID */
    private Long approverId;

    /** 审批时间 */
    private Date approveTime;

    /** 创建时间 */
    private Date createTime;

    // ==================== 非数据库字段 ====================

    /** 员工姓名（用于查询显示） */
    private String empName;

    /** 员工工号（用于查询显示） */
    private String empNo;

    /** 部门名称（用于查询显示） */
    private String deptName;

    /** 审批人姓名（用于查询显示） */
    private String approverName;

    /** 加班类型描述 */
    private String overtimeTypeDesc;

    /** 状态描述 */
    private String statusDesc;

    // ==================== 查询条件字段 (不映射到数据库) ====================

    /** 查询开始日期 */
    private LocalDate startDate;

    /** 查询结束日期 */
    private LocalDate endDate;
}
