package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysOvertime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 加班Mapper接口
 */
@Mapper
public interface SysOvertimeMapper {

    /**
     * 根据ID查询加班记录
     */
    SysOvertime selectById(@Param("id") Long id);

    /**
     * 查询加班列表
     */
    List<SysOvertime> selectList(SysOvertime sysOvertime);

    /**
     * 查询我的加班记录
     */
    List<SysOvertime> selectMyRecords(@Param("empId") Long empId);

    /**
     * 查询待审批列表
     */
    List<SysOvertime> selectPending();

    /**
     * 新增加班申请
     */
    int insert(SysOvertime sysOvertime);

    /**
     * 更新加班记录
     */
    int update(SysOvertime sysOvertime);

    /**
     * 删除加班记录
     */
    int deleteById(@Param("id") Long id);

    /**
     * 审批加班
     */
    int approve(@Param("id") Long id, @Param("approverId") Long approverId, @Param("status") Integer status, @Param("approveTime") java.util.Date approveTime);

    /**
     * 根据状态统计加班数量
     */
    Integer countByStatus(@Param("status") Integer status);
}
