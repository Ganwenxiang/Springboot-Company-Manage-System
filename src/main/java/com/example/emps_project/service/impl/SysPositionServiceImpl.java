package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysPosition;
import com.example.emps_project.mapper.SysPositionMapper;
import com.example.emps_project.service.ISysPositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 职位服务实现类
 */
@Slf4j
@Service
public class SysPositionServiceImpl implements ISysPositionService {

    @Autowired
    private SysPositionMapper sysPositionMapper;

    @Override
    public SysPosition selectById(Long id) {
        return sysPositionMapper.selectById(id);
    }

    @Override
    public List<SysPosition> selectList(SysPosition sysPosition) {
        return sysPositionMapper.selectList(sysPosition);
    }

    @Override
    public List<SysPosition> selectAll() {
        return sysPositionMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysPosition sysPosition) {
        return sysPositionMapper.insert(sysPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysPosition sysPosition) {
        return sysPositionMapper.update(sysPosition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        // 检查是否有员工使用该职位
        int count = sysPositionMapper.countEmps(id);
        if (count > 0) {
            throw new BusinessException("该职位下还有员工，不能删除");
        }
        return sysPositionMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> ids) {
        // 检查是否有员工使用这些职位
        for (Long id : ids) {
            int count = sysPositionMapper.countEmps(id);
            if (count > 0) {
                SysPosition position = sysPositionMapper.selectById(id);
                throw new BusinessException("职位【" + position.getPositionName() + "】下还有员工，不能删除");
            }
        }
        return sysPositionMapper.deleteByIds(ids);
    }
}
