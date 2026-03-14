package com.example.emps_project.service;

import com.example.emps_project.entity.SysDept;

import java.util.List;

public interface ISysDeptService {

    // 获取部门树形结构 | 递归查询、MyBatis
    List<SysDept> getDeptTree();

    // 新增部门 | MyBatis
    int insertDept(SysDept dept);

    // 更新部门 | MyBatis
    int updateDept(SysDept dept);

    // 根据id删除部门 | MyBatis
    int deleteDeptById(Long id);
}
