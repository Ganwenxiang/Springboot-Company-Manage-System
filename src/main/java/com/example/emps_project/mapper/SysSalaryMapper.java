package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysSalary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 薪资Mapper接口
 */
@Mapper
public interface SysSalaryMapper {

    // 根据ID查询薪资记录 | MyBatis select
    SysSalary selectById(@Param("id") Long id);

    // 根据员工ID和月份查询薪资记录 | MyBatis select
    SysSalary selectByEmpIdAndMonth(@Param("empId") Long empId, @Param("salaryMonth") String salaryMonth);

    // 查询薪资列表 | MyBatis动态SQL
    List<SysSalary> selectList(SysSalary sysSalary);

    // 查询我的薪资记录 | MyBatis select
    List<SysSalary> selectMySalaries(@Param("empId") Long empId);

    // 新增薪资记录 | MyBatis insert
    int insert(SysSalary sysSalary);

    // 更新薪资记录 | MyBatis update
    int update(SysSalary sysSalary);

    // 删除薪资记录 | MyBatis delete
    int deleteById(@Param("id") Long id);

    // 更新发放状态 | MyBatis update
    int updatePayStatus(@Param("id") Long id, @Param("status") Integer status);
}
