package com.example.emps_project.service;

import com.example.emps_project.entity.SysDept;

import java.util.List;

public interface ISysDeptService {

    /**
     * 获取部门树形结构
     */
    List<SysDept> getDeptTree();

    /**
     * @param dept
     * @return 新增部门
     */
    int insertDept(SysDept dept);

    /**
     * @param id
     * @return 根据id删除部门
     */
    int deleteDeptById(Long id);
}
