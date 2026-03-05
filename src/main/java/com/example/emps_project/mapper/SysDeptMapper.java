package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysDeptMapper {

    /**
     * 查询所有部门
     */
//    List<SysDept> selectAllDepts();
    List<SysDept> selectAllDepts();
    /**
     * 新增部门
     */
    int insertDept(SysDept dept);

    /**
     * 根据ID删除部门
     */
    int deleteDeptById(Long id);

    /**
     * 检查某个部门下是否有子部门 (用于删除前的校验)
     */
    int checkChildCount(Long deptId);
}