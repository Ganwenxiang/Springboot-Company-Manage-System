package com.example.emps_project.service;

import com.example.emps_project.entity.SysEmp;
import com.github.pagehelper.PageInfo;

public interface ISysEmpService {
    // 分页查询员工信息 | PageHelper分页、MyBatis动态SQL
    PageInfo<SysEmp> selectEmpPage(Integer pageNum, Integer pageSize, SysEmp emp);

// 新增员工 | MyBatis
    int insertEmp(SysEmp emp);
    // 更新员工信息 | MyBatis
    int updateEmp(SysEmp emp);
    // 删除员工 | MyBatis
    int deleteEmpById(Long id);
    // 根据ID查询员工 | MyBatis
    SysEmp getById(Long id);

    // 高级搜索员工（支持多条件联合查询，包含部门名称） | PageHelper分页、MyBatis关联查询
    PageInfo<SysEmp> searchEmployees(Integer pageNum, Integer pageSize, SysEmp emp);
}