package com.example.emps_project.service;

import com.example.emps_project.entity.SysPosition;

import java.util.List;

/**
 * 职位服务接口
 */
public interface ISysPositionService {

    // 根据ID查询职位 | MyBatis
    SysPosition selectById(Long id);

    // 查询职位列表（支持动态条件） | MyBatis动态SQL
    List<SysPosition> selectList(SysPosition sysPosition);

    // 查询所有职位（不分页） | MyBatis
    List<SysPosition> selectAll();

    // 新增职位 | MyBatis
    int insert(SysPosition sysPosition);

    // 更新职位 | MyBatis
    int update(SysPosition sysPosition);

    // 删除职位 | MyBatis
    int deleteById(Long id);

    // 批量删除职位 | MyBatis批量操作
    int deleteByIds(List<Long> ids);
}
