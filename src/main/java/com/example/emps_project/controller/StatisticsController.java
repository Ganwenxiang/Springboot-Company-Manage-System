package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.dto.StatisticsDTO;
import com.example.emps_project.service.IStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 统计分析控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequireLogin
public class StatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    /**
     * 获取首页概览数据
     */
    @GetMapping("/overview")
    public Result<StatisticsDTO.OverviewData> getOverview() {
        try {
            StatisticsDTO.OverviewData overview = statisticsService.getOverviewData();
            return Result.success(overview);
        } catch (Exception e) {
            log.error("获取首页概览数据失败: {}", e.getMessage());
            return Result.error("获取首页概览数据失败");
        }
    }

    /**
     * 获取员工分布统计
     */
    @GetMapping("/emp-distribution")
    public Result<List<StatisticsDTO.EmpDistribution>> getEmpDistribution() {
        try {
            List<StatisticsDTO.EmpDistribution> distribution = statisticsService.getEmpDistribution();
            return Result.success(distribution);
        } catch (Exception e) {
            log.error("获取员工分布统计失败: {}", e.getMessage());
            return Result.error("获取员工分布统计失败");
        }
    }

    /**
     * 获取考勤汇总统计
     */
    @GetMapping("/attendance-summary")
    public Result<List<StatisticsDTO.AttendanceSummary>> getAttendanceSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                return Result.error("开始日期不能晚于结束日期");
            }
            List<StatisticsDTO.AttendanceSummary> summary = statisticsService.getAttendanceSummary(startDate, endDate);
            return Result.success(summary);
        } catch (Exception e) {
            log.error("获取考勤汇总统计失败: {}", e.getMessage());
            return Result.error("获取考勤汇总统计失败");
        }
    }

    /**
     * 获取部门考勤对比
     */
    @GetMapping("/dept-attendance")
    public Result<List<StatisticsDTO.DeptAttendance>> getDeptAttendance(
            @RequestParam String month) {
        try {
            if (month == null || month.isEmpty()) {
                return Result.error("月份不能为空");
            }
            List<StatisticsDTO.DeptAttendance> attendance = statisticsService.getDeptAttendance(month);
            return Result.success(attendance);
        } catch (Exception e) {
            log.error("获取部门考勤对比失败: {}", e.getMessage());
            return Result.error("获取部门考勤对比失败");
        }
    }
}
