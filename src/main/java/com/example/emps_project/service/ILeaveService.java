package com.example.emps_project.service;

import com.example.emps_project.entity.SysLeave;

import java.util.List;

/**
 * 请假服务接口
 */
public interface ILeaveService {

    // 根据ID查询请假申请 | MyBatis
    SysLeave selectById(Long id);

    // 查询请假列表（支持动态条件） | MyBatis动态SQL
    List<SysLeave> selectList(SysLeave sysLeave);

    // 查询我的请假申请 | MyBatis
    List<SysLeave> selectMyRequests(Long empId);

    // 查询待审批请假列表 | MyBatis
    List<SysLeave> selectPending();

    // 提交请假申请 | MyBatis
    int submit(SysLeave sysLeave);

    // 撤销请假申请 | MyBatis状态更新
    int cancel(Long id);

    // 审批通过请假申请 | MyBatis状态更新
    int approve(Long id, Long approverId, String remark);

    // 审批拒绝请假申请 | MyBatis状态更新
    int reject(Long id, Long approverId, String remark);

    // 删除请假申请 | MyBatis
    int deleteById(Long id);
}
