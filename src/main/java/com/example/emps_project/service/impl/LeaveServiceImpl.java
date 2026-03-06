package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysLeave;
import com.example.emps_project.mapper.SysLeaveMapper;
import com.example.emps_project.service.ILeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 请假服务实现类
 */
@Slf4j
@Service
public class LeaveServiceImpl implements ILeaveService {

    @Autowired
    private SysLeaveMapper sysLeaveMapper;

    @Override
    public SysLeave selectById(Long id) {
        return sysLeaveMapper.selectById(id);
    }

    @Override
    public List<SysLeave> selectList(SysLeave sysLeave) {
        return sysLeaveMapper.selectList(sysLeave);
    }

    @Override
    public List<SysLeave> selectMyRequests(Long empId) {
        return sysLeaveMapper.selectMyRequests(empId);
    }

    @Override
    public List<SysLeave> selectPending() {
        return sysLeaveMapper.selectPending();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(SysLeave sysLeave) {
        // 设置初始状态为待审批
        sysLeave.setStatus(0);
        return sysLeaveMapper.insert(sysLeave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancel(Long id) {
        SysLeave leave = sysLeaveMapper.selectById(id);
        if (leave == null) {
            throw new BusinessException("请假申请不存在");
        }

        // 只有待审批的申请才能撤销
        if (leave.getStatus() != 0) {
            throw new BusinessException("只有待审批的申请才能撤销");
        }

        leave.setStatus(3); // 已撤销
        return sysLeaveMapper.update(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(Long id, Long approverId, String remark) {
        SysLeave leave = sysLeaveMapper.selectById(id);
        if (leave == null) {
            throw new BusinessException("请假申请不存在");
        }

        // 只有待审批的申请才能审批
        if (leave.getStatus() != 0) {
            throw new BusinessException("该申请已被审批");
        }

        return sysLeaveMapper.approve(id, approverId, 1, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Long id, Long approverId, String remark) {
        SysLeave leave = sysLeaveMapper.selectById(id);
        if (leave == null) {
            throw new BusinessException("请假申请不存在");
        }

        // 只有待审批的申请才能审批
        if (leave.getStatus() != 0) {
            throw new BusinessException("该申请已被审批");
        }

        return sysLeaveMapper.approve(id, approverId, 2, remark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        SysLeave leave = sysLeaveMapper.selectById(id);
        if (leave == null) {
            throw new BusinessException("请假申请不存在");
        }

        // 只有待审批或已拒绝的申请才能删除
        if (leave.getStatus() == 1) {
            throw new BusinessException("已批准的申请不能删除");
        }

        return sysLeaveMapper.deleteById(id);
    }
}
