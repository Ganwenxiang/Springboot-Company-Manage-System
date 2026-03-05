package com.example.emps_project.controller;

import com.example.emps_project.entity.SysDept;
import com.example.emps_project.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depts")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 获取部分树形结构
     * @return
     */
    @GetMapping
    public ResponseEntity<List<SysDept>> getDeptTree(){
        List<SysDept> tree = sysDeptService.getDeptTree();
        return ResponseEntity.ok(tree);
    }

    /**
     * 新增部门
     * @param dept
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addDept(@RequestBody SysDept dept){
        sysDeptService.insertDept(dept);
        return ResponseEntity.ok("新增部门成功");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDept(@PathVariable Long id){
        try{
            sysDeptService.deleteDeptById(id);
            return ResponseEntity.ok("删除成功");
        }catch (RuntimeException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
