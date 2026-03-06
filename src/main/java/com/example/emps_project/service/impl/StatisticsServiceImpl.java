package com.example.emps_project.service.impl;

import com.example.emps_project.dto.StatisticsDTO;
import com.example.emps_project.mapper.SysAttendanceMapper;
import com.example.emps_project.mapper.SysDeptMapper;
import com.example.emps_project.mapper.SysEmpMapper;
import com.example.emps_project.mapper.SysLeaveMapper;
import com.example.emps_project.mapper.SysOvertimeMapper;
import com.example.emps_project.service.IStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析服务实现
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private SysEmpMapper sysEmpMapper;
    @Autowired
    private SysAttendanceMapper sysAttendanceMapper;
    @Autowired
    private SysLeaveMapper sysLeaveMapper;
    @Autowired
    private SysOvertimeMapper sysOvertimeMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public StatisticsDTO.OverviewData getOverviewData() {
        StatisticsDTO.OverviewData overview = new StatisticsDTO.OverviewData();

        // 总员工数（在职）
        Integer totalEmps = sysEmpMapper.countByStatus(1);
        overview.setTotalEmps(totalEmps != null ? totalEmps : 0);

        // 今日出勤数
        Integer todayAttendance = sysAttendanceMapper.countTodayAttendance();
        overview.setTodayAttendance(todayAttendance != null ? todayAttendance : 0);

        // 待审批请假
        Integer pendingLeave = sysLeaveMapper.countByStatus(0);
        overview.setPendingLeave(pendingLeave != null ? pendingLeave : 0);

        // 待审批加班
        Integer pendingOvertime = sysOvertimeMapper.countByStatus(0);
        overview.setPendingOvertime(pendingOvertime != null ? pendingOvertime : 0);

        return overview;
    }

    @Override
    public List<StatisticsDTO.EmpDistribution> getEmpDistribution() {
        List<StatisticsDTO.EmpDistribution> result = new ArrayList<>();

        // 获取所有部门
        List<Map<String, Object>> depts = sysDeptMapper.selectAllDeptsWithCount();

        for (Map<String, Object> dept : depts) {
            StatisticsDTO.EmpDistribution distribution = new StatisticsDTO.EmpDistribution();
            distribution.setDeptId(((Number) dept.get("id")).longValue());
            distribution.setDeptName((String) dept.get("dept_name"));
            Number count = (Number) dept.get("emp_count");
            distribution.setEmpCount(count != null ? count.intValue() : 0);
            result.add(distribution);
        }

        return result;
    }

    @Override
    public List<StatisticsDTO.AttendanceSummary> getAttendanceSummary(LocalDate startDate, LocalDate endDate) {
        List<StatisticsDTO.AttendanceSummary> result = new ArrayList<>();

        LocalDate current = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!current.isAfter(endDate)) {
            StatisticsDTO.AttendanceSummary summary = new StatisticsDTO.AttendanceSummary();
            summary.setDate(current.format(formatter));

            // 应到人数（在职员工数）
            Integer expected = sysEmpMapper.countByStatus(1);
            summary.setExpected(expected != null ? expected : 0);

            // 获取当日考勤统计
            Map<String, Object> attendanceStats = sysAttendanceMapper.getDailyStats(current);
            if (attendanceStats != null) {
                Number actual = (Number) attendanceStats.get("actual");
                Number late = (Number) attendanceStats.get("late");
                Number earlyLeave = (Number) attendanceStats.get("early_leave");
                Number absent = (Number) attendanceStats.get("absent");

                summary.setActual(actual != null ? actual.intValue() : 0);
                summary.setLate(late != null ? late.intValue() : 0);
                summary.setEarlyLeave(earlyLeave != null ? earlyLeave.intValue() : 0);
                summary.setAbsent(absent != null ? absent.intValue() : 0);
            } else {
                summary.setActual(0);
                summary.setLate(0);
                summary.setEarlyLeave(0);
                summary.setAbsent(summary.getExpected());
            }

            result.add(summary);
            current = current.plusDays(1);
        }

        return result;
    }

    @Override
    public List<StatisticsDTO.DeptAttendance> getDeptAttendance(String month) {
        List<StatisticsDTO.DeptAttendance> result = new ArrayList<>();

        // 获取所有部门
        List<Map<String, Object>> depts = sysDeptMapper.selectAllDeptsWithCount();

        for (Map<String, Object> dept : depts) {
            StatisticsDTO.DeptAttendance attendance = new StatisticsDTO.DeptAttendance();
            attendance.setDeptId(((Number) dept.get("id")).longValue());
            attendance.setDeptName((String) dept.get("dept_name"));

            // 获取该部门当月考勤统计
            Long deptId = ((Number) dept.get("id")).longValue();
            Map<String, Object> stats = sysAttendanceMapper.getMonthlyStatsByDept(deptId, month);

            if (stats != null) {
                Number workingDays = (Number) stats.get("working_days");
                Number expectedCount = (Number) stats.get("expected_count");
                Number actualCount = (Number) stats.get("actual_count");

                if (expectedCount != null && actualCount != null && expectedCount.intValue() > 0) {
                    BigDecimal rate = new BigDecimal(actualCount.toString())
                            .divide(new BigDecimal(expectedCount.toString()), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
                    attendance.setAttendanceRate(rate);
                } else {
                    attendance.setAttendanceRate(BigDecimal.ZERO);
                }
            } else {
                attendance.setAttendanceRate(BigDecimal.ZERO);
            }

            result.add(attendance);
        }

        return result;
    }
}
