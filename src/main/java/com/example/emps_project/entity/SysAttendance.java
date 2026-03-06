package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * 考勤记录表实体类
 */
@Data
public class SysAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    private Long empId;

    /** 考勤日期 */
    private LocalDate attendanceDate;

    /** 签到时间 */
    private LocalTime checkInTime;

    /** 签退时间 */
    private LocalTime checkOutTime;

    /** 工作时长(小时) */
    private BigDecimal workHours;

    /** 状态：1正常 2迟到 3早退 4缺勤 */
    private Integer status;

    /** 签到地点 */
    private String checkInLocation;

    /** 签退地点 */
    private String checkOutLocation;

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

    // ==================== 查询条件字段 (不映射到数据库) ====================

    /** 查询开始日期 */
    private LocalDate startDate;

    /** 查询结束日期 */
    private LocalDate endDate;
}
