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

    /**
     * 根据ID查询薪资记录
     */
    SysSalary selectById(@Param("id") Long id);

    /**
     * 根据员工ID和月份查询薪资记录
     */
    SysSalary selectByEmpIdAndMonth(@Param("empId") Long empId, @Param("salaryMonth") String salaryMonth);

    /**
     * 查询薪资列表
     */
    List<SysSalary> selectList(SysSalary sysSalary);

    /**
     * 查询我的薪资记录
     */
    List<SysSalary> selectMySalaries(@Param("empId") Long empId);

    /**
     * 新增薪资记录
     */
    int insert(SysSalary sysSalary);

    /**
     * 更新薪资记录
     */
    int update(SysSalary sysSalary);

    /**
     * 删除薪资记录
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新发放状态
     */
    int updatePayStatus(@Param("id") Long id, @Param("status") Integer status);
}
