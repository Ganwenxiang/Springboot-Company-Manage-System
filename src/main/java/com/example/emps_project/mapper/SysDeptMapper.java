package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysDept;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface SysDeptMapper {

    // 查询所有部门 | MyBatis select
    List<SysDept> selectAllDepts();
    // 新增部门 | MyBatis insert
    int insertDept(SysDept dept);

    // 根据ID删除部门 | MyBatis delete
    int deleteDeptById(Long id);

    // 检查某个部门下是否有子部门（删除前校验） | MyBatis count聚合查询
    int checkChildCount(Long deptId);

    // 查询所有部门及员工数量 | MyBatis关联查询、聚合查询
    // @MapKey 指定用 id 字段作为 Map 的 key，返回 Map<Long, Map<String, Object>>
    @MapKey("id")
    Map<Long, Map<String, Object>> selectAllDeptsWithCount();

    // 更新部门 | MyBatis update
    int updateDept(SysDept dept);
}