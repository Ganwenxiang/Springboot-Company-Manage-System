package com.example.emps_project.controller;

import com.example.emps_project.entity.SysEmp;
import com.example.emps_project.service.ISysEmpService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class SysEmpController {

    @Autowired
    private ISysEmpService sysEmpService;

    /**
     * 分页查询员工列表
     * GET /api/employees?pageNum=1&pageSize=10&empName=张&deptId=2
     */
    @GetMapping
    public ResponseEntity<PageInfo<SysEmp>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysEmp emp) { // SpringMVC 会自动把 URL 参数封装进 emp 对象

        PageInfo<SysEmp> pageInfo = sysEmpService.selectEmpPage(pageNum, pageSize, emp);
        return ResponseEntity.ok(pageInfo);
    }

    /**
     * 高级搜索员工（支持多条件联合查询，包含部门名称）
     * GET /api/employees/search?pageNum=1&pageSize=10&empName=张&jobTitle=工程师&entryDateStart=2023-01-01&entryDateEnd=2023-12-31
     */
    @GetMapping("/search")
    public ResponseEntity<PageInfo<SysEmp>> search(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysEmp emp) {

        PageInfo<SysEmp> pageInfo = sysEmpService.searchEmployees(pageNum, pageSize, emp);
        return ResponseEntity.ok(pageInfo);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public ResponseEntity<String> add(@RequestBody SysEmp emp) {
        sysEmpService.insertEmp(emp);
        return ResponseEntity.ok("新增成功");
    }

    /**
     * 修改员工
     */
    @PutMapping
    public ResponseEntity<String> edit(@RequestBody SysEmp emp) {
        sysEmpService.updateEmp(emp);
        return ResponseEntity.ok("修改成功");
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        sysEmpService.deleteEmpById(id);
        return ResponseEntity.ok("删除成功");
    }
}