package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SysEmp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 部门ID */
    private Long deptId;

    /** 工号 */
    private String empNo;

    /** 姓名 */
    private String empName;

    /** 手机号 */
    private String phone;

    /** 职位 */
    private String jobTitle;

    /** 入职日期 */
    private Date entryDate;

    private Date createTime;

    // ----------- 扩展字段 (用于前端显示部门名称) -----------
    // 数据库表中没有这个字段，但在列表展示时很有用
    // 需要在 Mapper XML 中通过 JOIN 查询赋值，这里先预留
    private String deptName;

    // ----------- 搜索条件字段 (不映射到数据库) -----------
    // 用于日期范围搜索
    private Date entryDateStart;
    private Date entryDateEnd;
}