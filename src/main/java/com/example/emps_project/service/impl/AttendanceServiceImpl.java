package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysAttendance;
import com.example.emps_project.mapper.SysAttendanceMapper;
import com.example.emps_project.service.IAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 考勤服务实现类
 */
@Slf4j
@Service
public class AttendanceServiceImpl implements IAttendanceService {

    @Autowired
    private SysAttendanceMapper sysAttendanceMapper;

    /** 上班时间 */
    private static final LocalTime WORK_START_TIME = LocalTime.of(9, 0);

    /** 下班时间 */
    private static final LocalTime WORK_END_TIME = LocalTime.of(18, 0);

    @Override
    public SysAttendance selectById(Long id) {
        return sysAttendanceMapper.selectById(id);
    }

    @Override
    public List<SysAttendance> selectList(SysAttendance sysAttendance) {
        return sysAttendanceMapper.selectList(sysAttendance);
    }

    @Override
    public SysAttendance selectTodayByEmpId(Long empId) {
        return sysAttendanceMapper.selectTodayByEmpId(empId);
    }

    @Override
    public List<SysAttendance> selectMonthlyByEmpId(Long empId, Integer year, Integer month) {
        return sysAttendanceMapper.selectMonthlyByEmpId(empId, year, month);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Long empId, String location) {
        LocalDate today = LocalDate.now();

        // 查询今日是否已签到
        SysAttendance existAttendance = sysAttendanceMapper.selectByEmpIdAndDate(empId, today);
        if (existAttendance != null && existAttendance.getCheckInTime() != null) {
            throw new BusinessException("今日已签到");
        }

        LocalTime now = LocalTime.now();

        // 判断是否迟到
        int status = 1; // 正常
        if (now.isAfter(WORK_START_TIME)) {
            status = 2; // 迟到
        }

        SysAttendance attendance;
        if (existAttendance == null) {
            attendance = new SysAttendance();
            attendance.setEmpId(empId);
            attendance.setAttendanceDate(today);
            attendance.setCheckInTime(now);
            attendance.setCheckInLocation(location);
            attendance.setStatus(status);
            sysAttendanceMapper.insert(attendance);
        } else {
            existAttendance.setCheckInTime(now);
            existAttendance.setCheckInLocation(location);
            existAttendance.setStatus(status);
            sysAttendanceMapper.update(existAttendance);
        }

        log.info("员工签到成功: empId={}, time={}, status={}", empId, now, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long empId, String location) {
        LocalDate today = LocalDate.now();

        // 查询今日考勤记录
        SysAttendance attendance = sysAttendanceMapper.selectByEmpIdAndDate(empId, today);
        if (attendance == null || attendance.getCheckInTime() == null) {
            throw new BusinessException("请先签到");
        }

        if (attendance.getCheckOutTime() != null) {
            throw new BusinessException("今日已签退");
        }

        LocalTime now = LocalTime.now();

        // 判断是否早退
        if (now.isBefore(WORK_END_TIME)) {
            if (attendance.getStatus() == 2) {
                attendance.setStatus(2); // 已迟到，保持迟到
            } else {
                attendance.setStatus(3); // 早退
            }
        }

        // 计算工作时长
        long minutes = ChronoUnit.MINUTES.between(attendance.getCheckInTime(), now);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        attendance.setWorkHours(hours);

        attendance.setCheckOutTime(now);
        attendance.setCheckOutLocation(location);
        sysAttendanceMapper.update(attendance);

        log.info("员工签退成功: empId={}, time={}, workHours={}", empId, now, hours);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysAttendance sysAttendance) {
        return sysAttendanceMapper.update(sysAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return sysAttendanceMapper.deleteById(id);
    }

    @Override
    public List<SysAttendance> selectMonthlySummary(Integer year, Integer month) {
        return sysAttendanceMapper.selectMonthlySummary(year, month);
    }
}
