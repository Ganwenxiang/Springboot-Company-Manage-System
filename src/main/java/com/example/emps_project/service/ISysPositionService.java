package com.example.emps_project.service;

import com.example.emps_project.entity.SysPosition;

import java.util.List;

/**
 * 职位服务接口
 */
public interface ISysPositionService {

    /**
     * 根据ID查询职位
     */
    SysPosition selectById(Long id);

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
    int deleteById(Long id);

    /**
     * 批量删除职位
     */
    int deleteByIds(List<Long> ids);
}
