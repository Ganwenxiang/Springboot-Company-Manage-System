package com.example.emps_project.controller;

import com.example.emps_project.annotation.RequireLogin;
import com.example.emps_project.common.Result;
import com.example.emps_project.entity.SysPosition;
import com.example.emps_project.service.ISysPositionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/positions")
@RequireLogin
public class SysPositionController {

    @Autowired
    private ISysPositionService sysPositionService;

    /**
     * 分页查询职位列表
     */
    @GetMapping
    public Result<PageInfo<SysPosition>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String positionName,
            Integer status) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            SysPosition query = new SysPosition();
            query.setPositionName(positionName);
            query.setStatus(status);
            List<SysPosition> list = sysPositionService.selectList(query);
            PageInfo<SysPosition> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } catch (Exception e) {
            log.error("查询职位列表失败: {}", e.getMessage());
            return Result.error("查询职位列表失败");
        }
    }

    /**
     * 查询所有职位（不分页，用于下拉选择）
     */
    @GetMapping("/all")
    public Result<List<SysPosition>> allPositions() {
        try {
            List<SysPosition> list = sysPositionService.selectAll();
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询所有职位失败: {}", e.getMessage());
            return Result.error("查询所有职位失败");
        }
    }

    /**
     * 根据ID查询职位
     */
    @GetMapping("/{id}")
    public Result<SysPosition> getById(@PathVariable Long id) {
        try {
            SysPosition position = sysPositionService.selectById(id);
            if (position == null) {
                return Result.error("职位不存在");
            }
            return Result.success(position);
        } catch (Exception e) {
            log.error("查询职位失败: {}", e.getMessage());
            return Result.error("查询职位失败");
        }
    }

    /**
     * 新增职位
     */
    @PostMapping
    public Result<Void> add(@RequestBody SysPosition position) {
        try {
            if (!StringUtils.hasText(position.getPositionName())) {
                return Result.error("职位名称不能为空");
            }

            int result = sysPositionService.insert(position);
            if (result > 0) {
                return Result.ok("新增职位成功");
            }
            return Result.error("新增职位失败");
        } catch (Exception e) {
            log.error("新增职位失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新职位
     */
    @PutMapping
    public Result<Void> update(@RequestBody SysPosition position) {
        try {
            if (position.getId() == null) {
                return Result.error("职位ID不能为空");
            }

            int result = sysPositionService.update(position);
            if (result > 0) {
                return Result.ok("更新职位成功");
            }
            return Result.error("更新职位失败");
        } catch (Exception e) {
            log.error("更新职位失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除职位
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            int result = sysPositionService.deleteById(id);
            if (result > 0) {
                return Result.ok("删除职位成功");
            }
            return Result.error("删除职位失败");
        } catch (Exception e) {
            log.error("删除职位失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除职位
     */
    @DeleteMapping
    public Result<Void> deleteBatch(@RequestBody List<Long> ids) {
        try {
            int result = sysPositionService.deleteByIds(ids);
            if (result > 0) {
                return Result.ok("删除职位成功");
            }
            return Result.error("删除职位失败");
        } catch (Exception e) {
            log.error("批量删除职位失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
