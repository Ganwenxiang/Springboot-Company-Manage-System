package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.common.ResultCode;
import com.example.emps_project.entity.SysUser;
import com.example.emps_project.mapper.SysUserMapper;
import com.example.emps_project.service.ISysUserService;
import com.example.emps_project.security.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser selectByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public SysUser selectById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysUser> selectList(SysUser sysUser) {
        return sysUserMapper.selectList(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SysUser sysUser) {
        // 检查用户名是否已存在
        SysUser existUser = sysUserMapper.selectByUsername(sysUser.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 加密密码
        if (StringUtils.hasText(sysUser.getPassword())) {
            sysUser.setPassword(PasswordEncoder.encode(sysUser.getPassword()));
        } else {
            // 默认密码
            sysUser.setPassword(PasswordEncoder.encode("123456"));
        }

        return sysUserMapper.insert(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysUser sysUser) {
        // 不允许修改用户名
        sysUser.setUsername(null);
        // 密码单独处理
        sysUser.setPassword(null);
        return sysUserMapper.update(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        // 不允许删除管理员
        if (id == 1L) {
            throw new BusinessException("不允许删除管理员用户");
        }
        return sysUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<Long> ids) {
        // 不允许删除管理员
        if (ids.contains(1L)) {
            throw new BusinessException("不允许删除管理员用户");
        }
        return sysUserMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassword(Long id, String newPassword) {
        if (!StringUtils.hasText(newPassword)) {
            newPassword = "123456"; // 默认密码
        }
        String encodedPassword = PasswordEncoder.encode(newPassword);
        return sysUserMapper.resetPassword(id, encodedPassword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 验证旧密码
        if (!PasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 设置新密码
        String encodedPassword = PasswordEncoder.encode(newPassword);
        return sysUserMapper.resetPassword(id, encodedPassword);
    }

    @Override
    public void updateLastLoginInfo(Long id, String ip) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        sysUserMapper.updateLastLoginInfo(id, now, ip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        // 先删除用户的所有角色关联
        sysUserMapper.deleteUserRoleByUserId(userId);

        // 保存新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                sysUserMapper.insertUserRole(userId, roleId);
            }
        }
    }

    @Override
    public List<Long> selectRoleIdsByUserId(Long userId) {
        return sysUserMapper.selectRoleIdsByUserId(userId);
    }
}
