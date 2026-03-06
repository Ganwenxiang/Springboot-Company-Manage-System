package com.example.emps_project.service;

import com.example.emps_project.entity.SysSalary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 薪资服务接口
 */
public interface ISalaryService {

    /**
     * 根据ID查询薪资记录
     */
    SysSalary selectById(Long id);

    /**
     * 查询薪资列表
     */
    List<SysSalary> selectList(SysSalary sysSalary);

    /**
     * 查询我的薪资记录
     */
    List<SysSalary> selectMySalaries(Long empId);

    /**
     * 生成月度薪资
     */
    void calculateMonthlySalary(String salaryMonth);

    /**
     * 更新薪资记录
     */
    int update(SysSalary sysSalary);

    /**
     * 标记已发放
     */
    int markAsPaid(Long id);

    /**
     * 删除薪资记录
     */
    int deleteById(Long id);

    /**
     * 获取薪资汇总统计
     */
    Map<String, Object> getSalarySummary(String salaryMonth);
}
