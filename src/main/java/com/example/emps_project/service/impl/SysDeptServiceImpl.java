package com.example.emps_project.service.impl;

import com.example.emps_project.entity.SysDept;
import com.example.emps_project.mapper.SysDeptMapper;
import com.example.emps_project.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysDeptServiceImpl implements ISysDeptService {

    //定义缓存key，定义键名
    private static final String CACHE_KEY = "cache:dept:tree";
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;  //导入的redis工具
    @Autowired
    private ObjectMapper objectMapper;  // JSON 转换工具 (Spring自带)

    @Override
    public List<SysDept> getDeptTree() {

        //查询redsi缓存，从redis缓存中获取键名对应的值
        String jsonStr = redisTemplate.opsForValue().get(CACHE_KEY);

        if (StringUtils.hasText(jsonStr)) {  //判断是否为空，即不为null、不为" "，不为其余无效符号
            //命中缓存，直接将JSON转回List
            try {
                return objectMapper.readValue(jsonStr, new TypeReference<List<SysDept>>() { //反序列化
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //缓存未命中，就查询数据库
        List<SysDept> allDepts = sysDeptMapper.selectAllDepts();

        //组装树形结构
        List<SysDept> treeList = buildTree(allDepts);

        //写入redsi缓存，TTL设置为30min
        try {
            String cacheValue = objectMapper.writeValueAsString(treeList);
            redisTemplate.opsForValue().set(CACHE_KEY, cacheValue, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeList;
    }

    @Override
    public int insertDept(SysDept dept) {
        int row = sysDeptMapper.insertDept(dept);
        if (row > 0) {
            //数据变动，删除缓存
            redisTemplate.delete(CACHE_KEY);
        }
        return row;
    }

    @Override
    public int updateDept(SysDept dept) {
        int rows = sysDeptMapper.updateDept(dept);
        if (rows > 0) {
            //数据变动，删除缓存
            redisTemplate.delete(CACHE_KEY);
        }
        return rows;
    }

    @Override
    public int deleteDeptById(Long id) {
        //如果有子部门，不允许删除
        int childCount = sysDeptMapper.checkChildCount(id);
        if (childCount > 0) {
            throw new RuntimeException("存在子部门，不允许删除");
        }
        int rows = sysDeptMapper.deleteDeptById(id);
        if (rows > 0) {
            //数据变动，删除缓存
            redisTemplate.delete(CACHE_KEY);
        }
        return rows;
    }
    /**
     * 核心递归方法：构建树形结构
     */
    private List<SysDept> buildTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        // 1. 遍历所有部门，找到根节点 (ParentId = null 或 0)
        for (SysDept dept : depts) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                // 2. 递归查找该根节点下的子节点
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        return returnList;
    }
    /**
     * 递归查找子节点
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        // 继续递归每一个子节点
        for (SysDept tChild : childList) {
            recursionFn(list, tChild);
        }
    }
    /**
     * 匹配逻辑：判断 list 中的节点 n 是否是节点 t 的子节点
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            // 如果 n 的父ID 等于 t 的 ID，说明 n 是 t 的儿子
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

}
