package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysOvertime;
import com.example.emps_project.mapper.SysOvertimeMapper;
import com.example.emps_project.service.IOvertimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 加班服务实现类
 */
@Slf4j
@Service
public class OvertimeServiceImpl implements IOvertimeService {

    @Autowired
    private SysOvertimeMapper sysOvertimeMapper;

    @Override
    public SysOvertime selectById(Long id) {
        return sysOvertimeMapper.selectById(id);
    }

    @Override
    public List<SysOvertime> selectList(SysOvertime sysOvertime) {
        return sysOvertimeMapper.selectList(sysOvertime);
    }

    @Override
    public List<SysOvertime> selectMyRecords(Long empId) {
        return sysOvertimeMapper.selectMyRecords(empId);
    }

    @Override
    public List<SysOvertime> selectPending() {
        return sysOvertimeMapper.selectPending();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(SysOvertime sysOvertime) {
        // 设置初始状态为待审批
        sysOvertime.setStatus(0);
        return sysOvertimeMapper.insert(sysOvertime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(Long id, Long approverId) {
        SysOvertime overtime = sysOvertimeMapper.selectById(id);
        if (overtime == null) {
            throw new BusinessException("加班记录不存在");
        }

        // 只有待审批的记录才能审批
        if (overtime.getStatus() != 0) {
            throw new BusinessException("该记录已被审批");
        }

        return sysOvertimeMapper.approve(id, approverId, 1, new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Long id, Long approverId) {
        SysOvertime overtime = sysOvertimeMapper.selectById(id);
        if (overtime == null) {
            throw new BusinessException("加班记录不存在");
        }

        // 只有待审批的记录才能审批
        if (overtime.getStatus() != 0) {
            throw new BusinessException("该记录已被审批");
        }

        return sysOvertimeMapper.approve(id, approverId, 2, new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        SysOvertime overtime = sysOvertimeMapper.selectById(id);
        if (overtime == null) {
            throw new BusinessException("加班记录不存在");
        }

        // 只有待审批或已拒绝的记录才能删除
        if (overtime.getStatus() == 1) {
            throw new BusinessException("已批准的记录不能删除");
        }

        return sysOvertimeMapper.deleteById(id);
    }
}
