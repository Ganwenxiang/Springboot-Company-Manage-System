package com.example.emps_project.service;

import com.example.emps_project.entity.SysEmp;
import com.github.pagehelper.PageInfo;

public interface ISysEmpService {
    /**
     * 分页查询
     * @param pageNum 当前页
     * @param pageSize 每页条数
     * @param emp 查询条件
     * @return PageInfo (包含总条数、总页数、当前页数据等)
     */
    PageInfo<SysEmp> selectEmpPage(Integer pageNum, Integer pageSize, SysEmp emp);

    int insertEmp(SysEmp emp);
    int updateEmp(SysEmp emp);
    int deleteEmpById(Long id);
    SysEmp getById(Long id);

    /**
     * 高级搜索员工（支持多条件联合查询，包含部门名称）
     * @param pageNum 当前页
     * @param pageSize 每页条数
     * @param emp 搜索条件对象
     * @return 分页结果（包含部门名称）
     */
    PageInfo<SysEmp> searchEmployees(Integer pageNum, Integer pageSize, SysEmp emp);
}