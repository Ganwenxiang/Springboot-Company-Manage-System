package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysEmp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysEmpMapper {

    // 查询员工列表（支持动态条件：姓名、部门） | MyBatis动态SQL
    List<SysEmp> selectEmpList(SysEmp emp);

    // 新增员工 | MyBatis insert
    int insertEmp(SysEmp emp);

    // 根据ID查询员工 | MyBatis select
    SysEmp selectEmpById(Long id);

    // 修改员工信息 | MyBatis update
    int updateEmp(SysEmp emp);

    // 删除员工 | MyBatis delete
    int deleteEmpById(Long id);

    // 高级搜索员工（支持多条件联合查询，包含部门名称） | MyBatis关联查询、动态SQL
    List<SysEmp> searchEmployees(SysEmp emp);

    // 根据状态统计员工数量 | MyBatis count聚合查询
    Integer countByStatus(Integer status);
}