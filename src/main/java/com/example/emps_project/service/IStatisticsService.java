package com.example.emps_project.service;

import com.example.emps_project.dto.StatisticsDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 统计分析服务接口
 */
public interface IStatisticsService {

    // 获取首页概览数据（员工数、考勤率等） | MyBatis聚合查询
    StatisticsDTO.OverviewData getOverviewData();

    // 获取员工分布统计（按部门） | MyBatis分组统计
    List<StatisticsDTO.EmpDistribution> getEmpDistribution();

    // 获取考勤汇总统计 | MyBatis分组统计、日期范围查询
    List<StatisticsDTO.AttendanceSummary> getAttendanceSummary(LocalDate startDate, LocalDate endDate);

    // 获取部门考勤对比数据 | MyBatis分组统计、日期查询
    List<StatisticsDTO.DeptAttendance> getDeptAttendance(String month);
}
