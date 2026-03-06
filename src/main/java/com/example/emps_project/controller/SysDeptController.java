package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysDept;
import com.example.emps_project.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/depts")
@RequireLogin
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 获取部门树
     */
    @GetMapping
    public Result<List<SysDept>> getDeptTree() {
        try {
            List<SysDept> tree = sysDeptService.getDeptTree();
            return Result.success(tree);
        } catch (Exception e) {
            log.error("获取部门树失败: {}", e.getMessage());
            return Result.error("获取部门树失败");
        }
    }

    /**
     * 新增部门
     */
    @PostMapping
    public Result<Void> addDept(@RequestBody SysDept dept) {
        try {
            sysDeptService.insertDept(dept);
            return Result.ok("新增部门成功");
        } catch (Exception e) {
            log.error("新增部门失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新部门
     */
    @PutMapping
    public Result<Void> updateDept(@RequestBody SysDept dept) {
        try {
            sysDeptService.updateDept(dept);
            return Result.ok("更新部门成功");
        } catch (Exception e) {
            log.error("更新部门失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDept(@PathVariable Long id) {
        try {
            sysDeptService.deleteDeptById(id);
            return Result.ok("删除部门成功");
        } catch (Exception e) {
            log.error("删除部门失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
