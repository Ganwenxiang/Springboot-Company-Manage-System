package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysAttendance;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.service.IAttendanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/attendance")
@RequireLogin
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    /**
     * 员工签到
     */
    @PostMapping("/check-in")
    public Result<Void> checkIn(@RequestBody Map<String, String> request) {
        try {
            Long empId = LoginUser.getUserId();
            String location = request.get("location");
            attendanceService.checkIn(empId, location);
            return Result.ok("签到成功");
        } catch (Exception e) {
            log.error("签到失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 员工签退
     */
    @PostMapping("/check-out")
    public Result<Void> checkOut(@RequestBody Map<String, String> request) {
        try {
            Long empId = LoginUser.getUserId();
            String location = request.get("location");
            attendanceService.checkOut(empId, location);
            return Result.ok("签退成功");
        } catch (Exception e) {
            log.error("签退失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取今日考勤
     */
    @GetMapping("/my-today")
    public Result<Map<String, Object>> getMyToday() {
        try {
            Long empId = LoginUser.getUserId();
            SysAttendance attendance = attendanceService.selectTodayByEmpId(empId);

            Map<String, Object> data = new HashMap<>();
            if (attendance != null) {
                data.put("id", attendance.getId());
                data.put("checkInTime", attendance.getCheckInTime());
                data.put("checkOutTime", attendance.getCheckOutTime());
                data.put("workHours", attendance.getWorkHours());
                data.put("status", attendance.getStatus());
                data.put("checkInLocation", attendance.getCheckInLocation());
                data.put("checkOutLocation", attendance.getCheckOutLocation());
            } else {
                data.put("hasRecord", false);
            }

            return Result.success(data);
        } catch (Exception e) {
            log.error("获取今日考勤失败: {}", e.getMessage());
            return Result.error("获取今日考勤失败");
        }
    }

    /**
     * 查询月度考勤
     */
    @GetMapping("/monthly")
    public Result<List<SysAttendance>> getMonthly(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().monthValue}") Integer month) {
        try {
            Long empId = LoginUser.getUserId();
            List<SysAttendance> list = attendanceService.selectMonthlyByEmpId(empId, year, month);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询月度考勤失败: {}", e.getMessage());
            return Result.error("查询月度考勤失败");
        }
    }

    /**
     * 管理端查询考勤记录
     */
    @GetMapping
    public Result<PageInfo<SysAttendance>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Long empId,
            String empName,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Integer status) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysAttendance query = new SysAttendance();
            query.setEmpId(empId);
            query.setEmpName(empName);
            query.setAttendanceDate(attendanceDate);
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setStatus(status);
            List<SysAttendance> list = attendanceService.selectList(query);
            PageInfo<SysAttendance> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询考勤记录失败: {}", e.getMessage());
            return Result.error("查询考勤记录失败");
        }
    }

    /**
     * 修正考勤记录
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysAttendance attendance) {
        try {
            attendance.setId(id);
            int result = attendanceService.update(attendance);
            if (result > 0) {
                return Result.ok("修正考勤记录成功");
            }
            return Result.error("修正考勤记录失败");
        } catch (Exception e) {
            log.error("修正考勤记录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 月度考勤汇总
     */
    @GetMapping("/monthly-summary")
    public Result<List<SysAttendance>> monthlySummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") Integer year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().monthValue}") Integer month) {
        try {
            List<SysAttendance> list = attendanceService.selectMonthlySummary(year, month);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询月度考勤汇总失败: {}", e.getMessage());
            return Result.error("查询月度考勤汇总失败");
        }
    }
}
