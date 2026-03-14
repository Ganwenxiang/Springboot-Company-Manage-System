package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysSalary;
import com.example.emps_project.security.LoginUser;
import com.example.emps_project.service.ISalaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 薪资管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/salary")
@RequireLogin
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    // 查询当前用户的薪资记录 | Spring Security、MyBatis
    @GetMapping("/my-salaries")
    public Result<List<SysSalary>> getMySalaries() {
        try {
            Long empId = LoginUser.getUserId();
            List<SysSalary> list = salaryService.selectMySalaries(empId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询我的薪资记录失败: {}", e.getMessage());
            return Result.error("查询我的薪资记录失败");
        }
    }

    // 生成月度薪资（考勤、加班、请假综合计算） | MyBatis聚合计算
    @PostMapping("/calculate")
    public Result<Void> calculate(@RequestBody Map<String, String> request) {
        try {
            String salaryMonth = request.get("salaryMonth");
            if (salaryMonth == null || salaryMonth.isEmpty()) {
                return Result.error("薪资月份不能为空");
            }

            salaryService.calculateMonthlySalary(salaryMonth);
            return Result.ok("生成月度薪资成功");
        } catch (Exception e) {
            log.error("生成月度薪资失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 管理端分页查询薪资记录 | PageHelper分页、动态条件查询
    @GetMapping
    public Result<PageInfo<SysSalary>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Long empId,
            String empName,
            String salaryMonth,
            Integer status) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysSalary query = new SysSalary();
            query.setEmpId(empId);
            query.setEmpName(empName);
            query.setSalaryMonth(salaryMonth);
            query.setStatus(status);
            List<SysSalary> list = salaryService.selectList(query);
            PageInfo<SysSalary> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询薪资记录失败: {}", e.getMessage());
            return Result.error("查询薪资记录失败");
        }
    }

    // 查询薪资详情 | MyBatis
    @GetMapping("/{id}")
    public Result<SysSalary> getById(@PathVariable Long id) {
        try {
            SysSalary salary = salaryService.selectById(id);
            if (salary == null) {
                return Result.error("薪资记录不存在");
            }
            return Result.success(salary);
        } catch (Exception e) {
            log.error("查询薪资详情失败: {}", e.getMessage());
            return Result.error("查询薪资详情失败");
        }
    }

    // 调整薪资 | MyBatis
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysSalary salary) {
        try {
            salary.setId(id);
            int result = salaryService.update(salary);
            if (result > 0) {
                return Result.ok("调整薪资成功");
            }
            return Result.error("调整薪资失败");
        } catch (Exception e) {
            log.error("调整薪资失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 标记薪资已发放 | MyBatis
    @PutMapping("/{id}/pay")
    public Result<Void> markAsPaid(@PathVariable Long id) {
        try {
            int result = salaryService.markAsPaid(id);
            if (result > 0) {
                return Result.ok("标记发放成功");
            }
            return Result.error("标记发放失败");
        } catch (Exception e) {
            log.error("标记发放失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 删除薪资记录 | MyBatis
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            int result = salaryService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除薪资记录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    // 薪资汇总统计 | MyBatis聚合查询
    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary(@RequestParam String salaryMonth) {
        try {
            Map<String, Object> summary = salaryService.getSalarySummary(salaryMonth);
            return Result.success(summary);
        } catch (Exception e) {
            log.error("查询薪资汇总失败: {}", e.getMessage());
            return Result.error("查询薪资汇总失败");
        }
    }
}
