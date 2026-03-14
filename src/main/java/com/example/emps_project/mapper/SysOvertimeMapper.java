package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysOvertime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加班Mapper接口
 */
@Mapper
public interface SysOvertimeMapper {

    // 根据ID查询加班记录 | MyBatis select
    SysOvertime selectById(@Param("id") Long id);

    // 查询加班列表 | MyBatis动态SQL
    List<SysOvertime> selectList(SysOvertime sysOvertime);

    // 查询我的加班记录 | MyBatis select
    List<SysOvertime> selectMyRecords(@Param("empId") Long empId);

    // 查询待审批列表 | MyBatis select
    List<SysOvertime> selectPending();

    // 新增加班申请 | MyBatis insert
    int insert(SysOvertime sysOvertime);

    // 更新加班记录 | MyBatis update
    int update(SysOvertime sysOvertime);

    // 删除加班记录 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 审批加班 | MyBatis update
    int approve(@Param("id") Long id, @Param("approverId") Long approverId, @Param("status") Integer status, @Param("approveTime") java.util.Date approveTime);

    // 根据状态统计加班数量 | MyBatis count聚合查询
    Integer countByStatus(@Param("status") Integer status);
}
