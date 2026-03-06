package com.example.emps_project.service;

import com.example.emps_project.entity.SysAttendance;

import java.time.LocalTime;
import java.util.List;

/**
 * 考勤服务接口
 */
public interface IAttendanceService {

    /**
     * 根据ID查询考勤记录
     */
    SysAttendance selectById(Long id);

    /**
     * 查询考勤列表
     */
    List<SysAttendance> selectList(SysAttendance sysAttendance);

    /**
     * 查询员工今日考勤
     */
    SysAttendance selectTodayByEmpId(Long empId);

    /**
     * 查询员工月度考勤
     */
    List<SysAttendance> selectMonthlyByEmpId(Long empId, Integer year, Integer month);

    /**
     * 员工签到
     */
    void checkIn(Long empId, String location);

    /**
     * 员工签退
     */
    void checkOut(Long empId, String location);

    /**
     * 更新考勤记录
     */
    int update(SysAttendance sysAttendance);

    /**
     * 删除考勤记录
     */
    int deleteById(Long id);

    /**
     * 统计月度考勤汇总
     */
    List<SysAttendance> selectMonthlySummary(Integer year, Integer month);
}
