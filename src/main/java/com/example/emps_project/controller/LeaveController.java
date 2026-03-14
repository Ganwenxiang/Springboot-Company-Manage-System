package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysLeave;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.service.ILeaveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 请假管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/leave")
@RequireLogin
public class LeaveController {

    @Autowired
    private ILeaveService leaveService;

    // 查询当前用户的请假申请 | Spring Security、MyBatis
    @GetMapping("/my-requests")
    public Result<List<SysLeave>> getMyRequests() {
        try {
            Long empId = LoginUser.getUserId();
            List<SysLeave> list = leaveService.selectMyRequests(empId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询我的请假申请失败: {}", e.getMessage());
            return Result.error("查询我的请假申请失败");
        }
    }

    // 提交请假申请 | Spring Security、MyBatis
    @PostMapping
    public Result<Void> submit(@RequestBody SysLeave leave) {
        try {
            leave.setEmpId(LoginUser.getUserId());
            int result = leaveService.submit(leave);
            if (result > 0) {
                return Result.ok("提交请假申请成功");
            }
            return Result.error("提交请假申请失败");
        } catch (Exception e) {
            log.error("提交请假申请失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 撤销请假申请 | MyBatis
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        try {
            int result = leaveService.cancel(id);
            if (result > 0) {
                return Result.ok("撤销请假申请成功");
            }
            return Result.error("撤销请假申请失败");
        } catch (Exception e) {
            log.error("撤销请假申请失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 查询待审批请假列表 | MyBatis
    @GetMapping("/pending")
    public Result<List<SysLeave>> getPending() {
        try {
            List<SysLeave> list = leaveService.selectPending();
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询待审批列表失败: {}", e.getMessage());
            return Result.error("查询待审批列表失败");
        }
    }

    // 审批通过请假申请 | Spring Security、MyBatis
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Long approverId = LoginUser.getUserId();
            String remark = request.get("remark");
            int result = leaveService.approve(id, approverId, remark);
            if (result > 0) {
                return Result.ok("审批通过");
            }
            return Result.error("审批失败");
        } catch (Exception e) {
            log.error("审批失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 审批拒绝请假申请 | Spring Security、MyBatis
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Long approverId = LoginUser.getUserId();
            String remark = request.get("remark");
            int result = leaveService.reject(id, approverId, remark);
            if (result > 0) {
                return Result.ok("已拒绝");
            }
            return Result.error("操作失败");
        } catch (Exception e) {
            log.error("审批拒绝失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 管理端分页查询请假记录 | PageHelper分页、动态条件查询
    @GetMapping
    public Result<PageInfo<SysLeave>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Long empId,
            String empName,
            Integer leaveType,
            Integer status,
            Date startDate,
            Date endDate) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysLeave query = new SysLeave();
            query.setEmpId(empId);
            query.setEmpName(empName);
            query.setLeaveType(leaveType);
            query.setStatus(status);
            query.setStartDateQuery(startDate);
            query.setEndDateQuery(endDate);
            List<SysLeave> list = leaveService.selectList(query);
            PageInfo<SysLeave> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询请假记录失败: {}", e.getMessage());
            return Result.error("查询请假记录失败");
        }
    }

    // 删除请假申请 | MyBatis
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            int result = leaveService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除请假申请失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
