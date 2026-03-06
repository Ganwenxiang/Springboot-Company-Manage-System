package com.example.emps_project.mapper;

import com.example.emps_project.entity.SysPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职位Mapper接口
 */
@Mapper
public interface SysPositionMapper {

    /**
     * 根据ID查询职位
     */
    SysPosition selectById(@Param("id") Long id);

    /**
     * 查询职位列表
     */
    List<SysPosition> selectList(SysPosition sysPosition);

    /**
     * 查询所有职位（不分页）
     */
    List<SysPosition> selectAll();

    /**
     * 新增职位
     */
    int insert(SysPosition sysPosition);

    /**
     * 更新职位
     */
    int update(SysPosition sysPosition);

    /**
     * 删除职位
     */
    int deleteById(@Param("id") Long id);

    /**
     * 批量删除职位
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 统计职位下的员工数量
     */
    int countEmps(@Param("positionId") Long positionId);
}
