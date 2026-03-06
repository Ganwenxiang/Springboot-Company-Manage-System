package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysLeave;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 请假Mapper接口
 */
@Mapper
public interface SysLeaveMapper {

    /**
     * 根据ID查询请假申请
     */
    SysLeave selectById(@Param("id") Long id);

    /**
     * 查询请假列表
     */
    List<SysLeave> selectList(SysLeave sysLeave);

    /**
     * 查询我的请假申请
     */
    List<SysLeave> selectMyRequests(@Param("empId") Long empId);

    /**
     * 查询待审批列表
     */
    List<SysLeave> selectPending();

    /**
     * 新增请假申请
     */
    int insert(SysLeave sysLeave);

    /**
     * 更新请假申请
     */
    int update(SysLeave sysLeave);

    /**
     * 删除请假申请
     */
    int deleteById(@Param("id") Long id);

    /**
     * 审批请假
     */
    int approve(@Param("id") Long id, @Param("approverId") Long approverId, @Param("status") Integer status, @Param("remark") String remark);

    /**
     * 根据状态统计请假数量
     */
    Integer countByStatus(@Param("status") Integer status);
}
