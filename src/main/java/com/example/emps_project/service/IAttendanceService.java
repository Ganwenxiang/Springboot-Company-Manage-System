package com.example.emps_project.service;

import com.example.emps_project.entity.SysAttendance;

import java.util.List;

/**
 * 考勤服务接口
 */
public interface IAttendanceService {

    // 根据ID查询考勤记录 | MyBatis
    SysAttendance selectById(Long id);

    // 查询考勤列表（支持动态条件） | MyBatis动态SQL
    List<SysAttendance> selectList(SysAttendance sysAttendance);

    // 查询员工今日考勤记录 | MyBatis
    SysAttendance selectTodayByEmpId(Long empId);

    // 查询员工月度考勤记录 | MyBatis
    List<SysAttendance> selectMonthlyByEmpId(Long empId, Integer year, Integer month);

    // 员工签到打卡 | MyBatis插入、时间记录
    void checkIn(Long empId, String location);

    // 员工签退打卡 | MyBatis更新、工时计算
    void checkOut(Long empId, String location);

    // 更新考勤记录 | MyBatis
    int update(SysAttendance sysAttendance);

    // 删除考勤记录 | MyBatis
    int deleteById(Long id);

    // 统计月度考勤汇总 | MyBatis聚合查询
    List<SysAttendance> selectMonthlySummary(Integer year, Integer month);
}
