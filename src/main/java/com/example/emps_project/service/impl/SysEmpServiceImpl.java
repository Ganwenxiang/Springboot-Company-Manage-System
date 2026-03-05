package com.example.emps_project.service.impl;

import com.example.emps_project.entity.SysEmp;
import com.example.emps_project.mapper.SysEmpMapper;
import com.example.emps_project.service.ISysEmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysEmpServiceImpl implements ISysEmpService {

    @Autowired
    private SysEmpMapper sysEmpMapper;

    @Override
    public PageInfo<SysEmp> selectEmpPage(Integer pageNum, Integer pageSize, SysEmp emp) {
        // 1. 设置分页参数 (必须在查询方法调用的紧挨着的一行)
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行查询 (看似查所有，实际已被 PageHelper 拦截变成了 LIMIT 查询)
        List<SysEmp> list = sysEmpMapper.selectEmpList(emp);

        // 3. 封装成 PageInfo 对象返回 (它包含了 total, pages 等丰富信息)
        return new PageInfo<>(list);
    }

    @Override
    public int insertEmp(SysEmp emp) {
        return sysEmpMapper.insertEmp(emp);
    }

    @Override
    public int updateEmp(SysEmp emp) {
        return sysEmpMapper.updateEmp(emp);
    }

    @Override
    public int deleteEmpById(Long id) {
        return sysEmpMapper.deleteEmpById(id);
    }

    @Override
    public SysEmp getById(Long id) {
        return sysEmpMapper.selectEmpById(id);
    }

    @Override
    public PageInfo<SysEmp> searchEmployees(Integer pageNum, Integer pageSize, SysEmp emp) {
        // 1. 设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行高级搜索查询
        List<SysEmp> list = sysEmpMapper.searchEmployees(emp);

        // 3. 封装成 PageInfo 对象返回
        return new PageInfo<>(list);
    }
}