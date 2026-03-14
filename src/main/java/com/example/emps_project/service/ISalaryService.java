package com.example.emps_project.service;

import com.example.emps_project.entity.SysSalary;

import java.util.List;
import java.util.Map;

/**
 * 薪资服务接口
 */
public interface ISalaryService {

    // 根据ID查询薪资记录 | MyBatis
    SysSalary selectById(Long id);

    // 查询薪资列表（支持动态条件） | MyBatis动态SQL
    List<SysSalary> selectList(SysSalary sysSalary);

    // 查询我的薪资记录 | MyBatis
    List<SysSalary> selectMySalaries(Long empId);

    // 生成月度薪资（考勤、加班、请假综合计算） | MyBatis聚合计算、事务处理
    void calculateMonthlySalary(String salaryMonth);

    // 更新薪资记录 | MyBatis
    int update(SysSalary sysSalary);

    // 标记薪资已发放 | MyBatis状态更新
    int markAsPaid(Long id);

    // 删除薪资记录 | MyBatis
    int deleteById(Long id);

    // 获取薪资汇总统计 | MyBatis聚合查询
    Map<String, Object> getSalarySummary(String salaryMonth);
}
