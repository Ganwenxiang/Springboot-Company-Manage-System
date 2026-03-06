package com.example.emps_project.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 职位表实体类
 */
@Data
public class SysPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 职位名称 */
    private String positionName;

    /** 职位级别 */
    private Integer positionLevel;

    /** 基本工资 */
    private BigDecimal baseSalary;

    /** 状态:1启用,0停用 */
    private Integer status;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // ==================== 非数据库字段 ====================

    /** 该职位的员工数量（用于统计） */
    private Integer empCount;
}
