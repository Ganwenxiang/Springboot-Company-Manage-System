package com.example.emps_project.service;

import com.example.emps_project.entity.SysLeave;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 请假服务接口
 */
public interface ILeaveService {

    /**
     * 根据ID查询请假申请
     */
    SysLeave selectById(Long id);

    /**
     * 查询请假列表
     */
    List<SysLeave> selectList(SysLeave sysLeave);

    /**
     * 查询我的请假申请
     */
    List<SysLeave> selectMyRequests(Long empId);

    /**
     * 查询待审批列表
     */
    List<SysLeave> selectPending();

    /**
     * 提交请假申请
     */
    int submit(SysLeave sysLeave);

    /**
     * 撤销请假申请
     */
    int cancel(Long id);

    /**
     * 审批通过
     */
    int approve(Long id, Long approverId, String remark);

    /**
     * 审批拒绝
     */
    int reject(Long id, Long approverId, String remark);

    /**
     * 删除请假申请
     */
    int deleteById(Long id);
}
