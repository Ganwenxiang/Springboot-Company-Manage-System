package com.example.emps_project.service;

import com.example.emps_project.entity.SysOvertime;

import java.util.List;

/**
 * 加班服务接口
 */
public interface IOvertimeService {

    // 根据ID查询加班记录 | MyBatis
    SysOvertime selectById(Long id);

    // 查询加班列表（支持动态条件） | MyBatis动态SQL
    List<SysOvertime> selectList(SysOvertime sysOvertime);

    // 查询我的加班记录 | MyBatis
    List<SysOvertime> selectMyRecords(Long empId);

    // 查询待审批加班列表 | MyBatis
    List<SysOvertime> selectPending();

    // 提交加班申请 | MyBatis
    int submit(SysOvertime sysOvertime);

    // 审批通过加班申请 | MyBatis状态更新
    int approve(Long id, Long approverId);

    // 审批拒绝加班申请 | MyBatis状态更新
    int reject(Long id, Long approverId);

    // 删除加班记录 | MyBatis
    int deleteById(Long id);
}
