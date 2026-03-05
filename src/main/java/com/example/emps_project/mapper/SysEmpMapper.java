package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysEmp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysEmpMapper {

    /**
     * 查询员工列表 (支持动态条件：姓名、部门)
     * 注意：不需要手动传分页参数，PageHelper 会自动处理
     */
    List<SysEmp> selectEmpList(SysEmp emp);

    /**
     * 新增员工
     */
    int insertEmp(SysEmp emp);

    /**
     * 根据ID查询
     */
    SysEmp selectEmpById(Long id);

    /**
     * 修改员工
     */
    int updateEmp(SysEmp emp);

    /**
     * 删除员工
     */
    int deleteEmpById(Long id);

    /**
     * 高级搜索员工（支持多条件联合查询）
     * @param emp 搜索条件对象
     * @return 员工列表（包含部门名称）
     */
    List<SysEmp> searchEmployees(SysEmp emp);
}