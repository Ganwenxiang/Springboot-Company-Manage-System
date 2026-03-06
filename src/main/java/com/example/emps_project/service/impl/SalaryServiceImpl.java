package com.example.emps_project.service.impl;

import com.example.emps_project.common.BusinessException;
import com.example.emps_project.entity.SysEmp;
import com.example.emps_project.entity.SysPosition;
import com.example.emps_project.entity.SysSalary;
import com.example.emps_project.mapper.SysAttendanceMapper;
import com.example.emps_project.mapper.SysEmpMapper;
import com.example.emps_project.mapper.SysOvertimeMapper;
import com.example.emps_project.mapper.SysPositionMapper;
import com.example.emps_project.mapper.SysSalaryMapper;
import com.example.emps_project.service.ISalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪资服务实现类
 */
@Slf4j
@Service
public class SalaryServiceImpl implements ISalaryService {

    @Autowired
    private SysSalaryMapper sysSalaryMapper;

    @Autowired
    private SysEmpMapper sysEmpMapper;

    @Autowired
    private SysPositionMapper sysPositionMapper;

    @Autowired
    private SysAttendanceMapper sysAttendanceMapper;

    @Autowired
    private SysOvertimeMapper sysOvertimeMapper;

    /** 社保缴纳比例 */
    private static final BigDecimal SOCIAL_SECURITY_RATE = new BigDecimal("0.08");

    /** 个税起征点 */
    private static final BigDecimal TAX_THRESHOLD = new BigDecimal("5000");

    @Override
    public SysSalary selectById(Long id) {
        return sysSalaryMapper.selectById(id);
    }

    @Override
    public List<SysSalary> selectList(SysSalary sysSalary) {
        return sysSalaryMapper.selectList(sysSalary);
    }

    @Override
    public List<SysSalary> selectMySalaries(Long empId) {
        return sysSalaryMapper.selectMySalaries(empId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateMonthlySalary(String salaryMonth) {
        // 解析年月
        String[] parts = salaryMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        // 获取该月所有在职员工
        SysEmp query = new SysEmp();
        query.setEmpStatus(1); // 在职
        List<SysEmp> empList = sysEmpMapper.selectEmpList(query);

        for (SysEmp emp : empList) {
            try {
                // 检查是否已生成薪资
                SysSalary existSalary = sysSalaryMapper.selectByEmpIdAndMonth(emp.getId(), salaryMonth);
                if (existSalary != null) {
                    continue;
                }

                // 创建薪资记录
                SysSalary salary = new SysSalary();
                salary.setEmpId(emp.getId());
                salary.setSalaryMonth(salaryMonth);

                // 计算基本工资和岗位工资
                BigDecimal baseSalary = BigDecimal.ZERO;
                BigDecimal positionSalary = BigDecimal.ZERO;

                if (emp.getPositionId() != null) {
                    SysPosition position = sysPositionMapper.selectById(emp.getPositionId());
                    if (position != null) {
                        baseSalary = position.getBaseSalary() != null ? position.getBaseSalary() : BigDecimal.ZERO;
                    }
                }

                salary.setBaseSalary(baseSalary);
                salary.setPositionSalary(positionSalary);

                // 计算加班费
                BigDecimal overtimePay = calculateOvertimePay(emp.getId(), year, month);
                salary.setOvertimePay(overtimePay);

                // 默认奖金和补贴为0
                salary.setBonus(BigDecimal.ZERO);
                salary.setSubsidy(BigDecimal.ZERO);

                // 计算考勤扣款
                BigDecimal attendanceDeduction = calculateAttendanceDeduction(emp.getId(), year, month, baseSalary);
                salary.setAttendanceDeduction(attendanceDeduction);

                // 计算社保
                BigDecimal totalBeforeDeduction = baseSalary.add(positionSalary).add(overtimePay);
                BigDecimal socialSecurity = totalBeforeDeduction.multiply(SOCIAL_SECURITY_RATE).setScale(2, RoundingMode.HALF_UP);
                salary.setSocialSecurity(socialSecurity);

                // 计算应发工资
                BigDecimal totalSalary = totalBeforeDeduction
                        .add(salary.getBonus())
                        .add(salary.getSubsidy())
                        .subtract(attendanceDeduction);
                salary.setTotalSalary(totalSalary);

                // 计算个税
                BigDecimal taxableIncome = totalSalary.subtract(socialSecurity).subtract(TAX_THRESHOLD);
                BigDecimal tax = BigDecimal.ZERO;
                if (taxableIncome.compareTo(BigDecimal.ZERO) > 0) {
                    tax = calculateTax(taxableIncome);
                }
                salary.setTax(tax);

                // 计算实发工资
                BigDecimal actualSalary = totalSalary.subtract(socialSecurity).subtract(tax);
                salary.setActualSalary(actualSalary);

                // 设置状态为未发放
                salary.setStatus(0);

                sysSalaryMapper.insert(salary);

                log.info("生成员工薪资成功: empId={}, month={}, totalSalary={}", emp.getId(), salaryMonth, totalSalary);
            } catch (Exception e) {
                log.error("生成员工薪资失败: empId={}, error={}", emp.getId(), e.getMessage());
            }
        }
    }

    /**
     * 计算加班费
     */
    private BigDecimal calculateOvertimePay(Long empId, int year, int month) {
        // TODO: 查询该月已审批通过的加班记录，计算加班费
        // 加班费 = 加班时长 * (基本工资 / 21.75 / 8) * 倍数
        // 工作日加班：1.5倍，周末：2倍，节假日：3倍
        return BigDecimal.ZERO;
    }

    /**
     * 计算考勤扣款
     */
    private BigDecimal calculateAttendanceDeduction(Long empId, int year, int month, BigDecimal baseSalary) {
        // TODO: 查询该月考勤记录，计算缺勤和迟到早退扣款
        // 日工资 = 基本工资 / 21.75
        return BigDecimal.ZERO;
    }

    /**
     * 计算个税（简化版）
     */
    private BigDecimal calculateTax(BigDecimal taxableIncome) {
        // 使用超额累进税率（简化版）
        if (taxableIncome.compareTo(new BigDecimal("3000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.03"));
        } else if (taxableIncome.compareTo(new BigDecimal("12000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.1")).subtract(new BigDecimal("210"));
        } else if (taxableIncome.compareTo(new BigDecimal("25000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.2")).subtract(new BigDecimal("1410"));
        } else if (taxableIncome.compareTo(new BigDecimal("35000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.25")).subtract(new BigDecimal("2660"));
        } else if (taxableIncome.compareTo(new BigDecimal("55000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.3")).subtract(new BigDecimal("4410"));
        } else if (taxableIncome.compareTo(new BigDecimal("80000")) <= 0) {
            return taxableIncome.multiply(new BigDecimal("0.35")).subtract(new BigDecimal("7160"));
        } else {
            return taxableIncome.multiply(new BigDecimal("0.45")).subtract(new BigDecimal("15160"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SysSalary sysSalary) {
        // 重新计算应发工资和实发工资
        BigDecimal totalSalary = sysSalary.getBaseSalary()
                .add(sysSalary.getPositionSalary())
                .add(sysSalary.getOvertimePay())
                .add(sysSalary.getBonus())
                .add(sysSalary.getSubsidy())
                .subtract(sysSalary.getAttendanceDeduction());

        BigDecimal actualSalary = totalSalary
                .subtract(sysSalary.getSocialSecurity())
                .subtract(sysSalary.getTax());

        sysSalary.setTotalSalary(totalSalary);
        sysSalary.setActualSalary(actualSalary);

        return sysSalaryMapper.update(sysSalary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markAsPaid(Long id) {
        return sysSalaryMapper.updatePayStatus(id, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        SysSalary salary = sysSalaryMapper.selectById(id);
        if (salary == null) {
            throw new BusinessException("薪资记录不存在");
        }

        // 已发放的薪资不能删除
        if (salary.getStatus() == 1) {
            throw new BusinessException("已发放的薪资不能删除");
        }

        return sysSalaryMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> getSalarySummary(String salaryMonth) {
        Map<String, Object> summary = new HashMap<>();

        List<SysSalary> list = sysSalaryMapper.selectList(new SysSalary());

        BigDecimal totalSalary = BigDecimal.ZERO;
        BigDecimal actualSalary = BigDecimal.ZERO;
        int totalCount = 0;

        for (SysSalary salary : list) {
            if (salaryMonth.equals(salary.getSalaryMonth())) {
                totalSalary = totalSalary.add(salary.getTotalSalary());
                actualSalary = actualSalary.add(salary.getActualSalary());
                totalCount++;
            }
        }

        summary.put("totalCount", totalCount);
        summary.put("totalSalary", totalSalary);
        summary.put("actualSalary", actualSalary);
        summary.put("avgSalary", totalCount > 0 ? totalSalary.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        return summary;
    }
}
