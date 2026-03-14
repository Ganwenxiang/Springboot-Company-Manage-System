package com.example.emps_project.dto;

import lombok.Data;
import java.math.BigDecimal;


/**
 * 统计数据DTO
 */
@Data
public class StatisticsDTO {

    /**
     * 首页概览数据
     */
    @Data
    public static class OverviewData {
        /** 总员工数 */
        private Integer totalEmps;
        /** 今日出勤数 */
        private Integer todayAttendance;
        /** 待审批请假 */
        private Integer pendingLeave;
        /** 待审批加班 */
        private Integer pendingOvertime;
    }

    /**
     * 员工分布数据
     */
    @Data
    public static class EmpDistribution {
        /** 部门ID */
        private Long deptId;
        /** 部门名称 */
        private String deptName;
        /** 员工数量 */
        private Integer empCount;
    }

    /**
     * 考勤汇总数据
     */
    @Data
    public static class AttendanceSummary {
        /** 日期 */
        private String date;
        /** 应到人数 */
        private Integer expected;
        /** 实到人数 */
        private Integer actual;
        /** 迟到人数 */
        private Integer late;
        /** 早退人数 */
        private Integer earlyLeave;
        /** 缺勤人数 */
        private Integer absent;
    }

    /**
     * 部门考勤对比
     */
    @Data
    public static class DeptAttendance {
        /** 部门ID */
        private Long deptId;
        /** 部门名称 */
        private String deptName;
        /** 出勤率 */
        private BigDecimal attendanceRate;
    }
}
