package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysAttendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 考勤Mapper接口
 */
@Mapper
public interface SysAttendanceMapper {

    // 根据ID查询考勤记录 | MyBatis select
    SysAttendance selectById(@Param("id") Long id);

    // 根据员工ID和日期查询考勤记录 | MyBatis select
    SysAttendance selectByEmpIdAndDate(@Param("empId") Long empId, @Param("attendanceDate") LocalDate attendanceDate);

    // 查询考勤列表 | MyBatis动态SQL
    List<SysAttendance> selectList(SysAttendance sysAttendance);

    // 查询员工今日考勤 | MyBatis select
    SysAttendance selectTodayByEmpId(@Param("empId") Long empId);

    // 查询员工月度考勤 | MyBatis select
    List<SysAttendance> selectMonthlyByEmpId(@Param("empId") Long empId, @Param("year") Integer year, @Param("month") Integer month);

    // 新增考勤记录 | MyBatis insert
    int insert(SysAttendance sysAttendance);

    // 更新考勤记录 | MyBatis update
    int update(SysAttendance sysAttendance);

    // 删除考勤记录 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 统计月度考勤汇总 | MyBatis聚合查询
    List<SysAttendance> selectMonthlySummary(@Param("year") Integer year, @Param("month") Integer month);

    // 统计今日出勤人数 | MyBatis count聚合查询
    Integer countTodayAttendance();

    // 获取指定日期的考勤统计 | MyBatis聚合查询
    Map<String, Object> getDailyStats(@Param("date") LocalDate date);

    // 获取指定部门月份考勤统计 | MyBatis聚合查询
    Map<String, Object> getMonthlyStatsByDept(@Param("deptId") Long deptId, @Param("month") String month);
}
