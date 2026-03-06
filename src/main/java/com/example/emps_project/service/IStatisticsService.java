package com.example.emps_project.service;

import com.example.emps_project.dto.StatisticsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计分析服务接口
 */
public interface IStatisticsService {

    /**
     * 获取首页概览数据
     */
    StatisticsDTO.OverviewData getOverviewData();

    /**
     * 获取员工分布统计
     */
    List<StatisticsDTO.EmpDistribution> getEmpDistribution();

    /**
     * 获取考勤汇总统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<StatisticsDTO.AttendanceSummary> getAttendanceSummary(LocalDate startDate, LocalDate endDate);

    /**
     * 获取部门考勤对比
     * @param month 月份 (yyyy-MM)
     */
    List<StatisticsDTO.DeptAttendance> getDeptAttendance(String month);
}
