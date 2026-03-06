package com.example.emps_project.service;

import com.example.emps_project.entity.SysOvertime;

import java.util.List;

/**
 * 加班服务接口
 */
public interface IOvertimeService {

    /**
     * 根据ID查询加班记录
     */
    SysOvertime selectById(Long id);

    /**
     * 查询加班列表
     */
    List<SysOvertime> selectList(SysOvertime sysOvertime);

    /**
     * 查询我的加班记录
     */
    List<SysOvertime> selectMyRecords(Long empId);

    /**
     * 查询待审批列表
     */
    List<SysOvertime> selectPending();

    /**
     * 提交加班申请
     */
    int submit(SysOvertime sysOvertime);

    /**
     * 审批通过
     */
    int approve(Long id, Long approverId);

    /**
     * 审批拒绝
     */
    int reject(Long id, Long approverId);

    /**
     * 删除加班记录
     */
    int deleteById(Long id);
}
