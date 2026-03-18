package com.example.emps_project.controller;

import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysEmp;
import com.example.emps_project.service.ISysEmpService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class SysEmpController {

    @Autowired
    private ISysEmpService sysEmpService;

    // 分页查询员工列表 | PageHelper分页、Spring MVC参数绑定
    @GetMapping
    public Result<PageInfo<SysEmp>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysEmp emp) { // SpringMVC 会自动把 URL 参数封装进 emp 对象，传入emo对象既可以做分页查询，又可以做条件查询，一次满足多个用法

        PageInfo<SysEmp> pageInfo = sysEmpService.selectEmpPage(pageNum, pageSize, emp);
        return Result.success(pageInfo);
    }

    // 高级搜索员工（支持多条件联合查询，包含部门名称） | PageHelper分页、MyBatis动态SQL
    @GetMapping("/search")
    public Result<PageInfo<SysEmp>> search(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysEmp emp) {

        PageInfo<SysEmp> pageInfo = sysEmpService.searchEmployees(pageNum, pageSize, emp);
        return Result.success(pageInfo);
    }

    // 获取员工详情 | MyBatis
    @GetMapping("/{id}")
    public Result<SysEmp> getById(@PathVariable Long id) {
        SysEmp emp = sysEmpService.getById(id);
        return Result.success(emp);
    }

    // 新增员工 | MyBatis
    @PostMapping
    public Result<Void> add(@RequestBody SysEmp emp) {
        sysEmpService.insertEmp(emp);
        return Result.ok("新增成功");
    }

    // 修改员工信息 | MyBatis
    @PutMapping
    public Result<Void> edit(@RequestBody SysEmp emp) {
        sysEmpService.updateEmp(emp);
        return Result.ok("修改成功");
    }

    // 删除员工 | MyBatis
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sysEmpService.deleteEmpById(id);
        return Result.ok("删除成功");
    }
}