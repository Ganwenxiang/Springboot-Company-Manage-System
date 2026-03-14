package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysOvertime;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.service.IOvertimeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 加班管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/overtime")
@RequireLogin
public class OvertimeController {

    @Autowired
    private IOvertimeService overtimeService;

    // 查询当前用户的加班记录 | Spring Security、MyBatis
    @GetMapping("/my-records")
    public Result<List<SysOvertime>> getMyRecords() {
        try {
            Long empId = LoginUser.getUserId();
            List<SysOvertime> list = overtimeService.selectMyRecords(empId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询我的加班记录失败: {}", e.getMessage());
            return Result.error("查询我的加班记录失败");
        }
    }

    // 提交加班申请 | Spring Security、MyBatis
    @PostMapping
    public Result<Void> submit(@RequestBody SysOvertime overtime) {
        try {
            overtime.setEmpId(LoginUser.getUserId());
            int result = overtimeService.submit(overtime);
            if (result > 0) {
                return Result.ok("提交加班申请成功");
            }
            return Result.error("提交加班申请失败");
        } catch (Exception e) {
            log.error("提交加班申请失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 查询待审批加班列表 | MyBatis
    @GetMapping("/pending")
    public Result<List<SysOvertime>> getPending() {
        try {
            List<SysOvertime> list = overtimeService.selectPending();
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询待审批列表失败: {}", e.getMessage());
            return Result.error("查询待审批列表失败");
        }
    }

    // 审批通过加班申请 | Spring Security、MyBatis
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        try {
            Long approverId = LoginUser.getUserId();
            int result = overtimeService.approve(id, approverId);
            if (result > 0) {
                return Result.ok("审批通过");
            }
            return Result.error("审批失败");
        } catch (Exception e) {
            log.error("审批失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 审批拒绝加班申请 | Spring Security、MyBatis
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        try {
            Long approverId = LoginUser.getUserId();
            int result = overtimeService.reject(id, approverId);
            if (result > 0) {
                return Result.ok("已拒绝");
            }
            return Result.error("操作失败");
        } catch (Exception e) {
            log.error("审批拒绝失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 管理端分页查询加班记录 | PageHelper分页、动态条件查询
    @GetMapping
    public Result<PageInfo<SysOvertime>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Long empId,
            String empName,
            Integer overtimeType,
            Integer status,
            LocalDate startDate,
            LocalDate endDate) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysOvertime query = new SysOvertime();
            query.setEmpId(empId);
            query.setEmpName(empName);
            query.setOvertimeType(overtimeType);
            query.setStatus(status);
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            List<SysOvertime> list = overtimeService.selectList(query);
            PageInfo<SysOvertime> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询加班记录失败: {}", e.getMessage());
            return Result.error("查询加班记录失败");
        }
    }

    // 删除加班记录 | MyBatis
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            int result = overtimeService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除加班记录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
