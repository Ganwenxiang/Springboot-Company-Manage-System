-- =====================================================
-- 模拟数据 - 约30名员工
-- =====================================================

-- =====================================================
-- 1. 部门数据
-- =====================================================
INSERT INTO `sys_dept` (`parent_id`, `dept_name`, `order_num`, `status`) VALUES
(0, '技术部', 1, 1),
(0, '产品部', 2, 1),
(0, '市场部', 3, 1),
(0, '人事部', 4, 1),
(0, '财务部', 5, 1),
(0, '运营部', 6, 1)
ON DUPLICATE KEY UPDATE dept_name=VALUES(dept_name);

-- =====================================================
-- 2. 职位数据
-- =====================================================
INSERT INTO `sys_position` (`position_name`, `position_level`, `base_salary`, `status`) VALUES
('软件工程师', 1, 12000.00, 1),
('高级软件工程师', 2, 18000.00, 1),
('技术主管', 3, 25000.00, 1),
('产品经理', 2, 16000.00, 1),
('高级产品经理', 3, 22000.00, 1),
('市场专员', 1, 8000.00, 1),
('市场经理', 3, 20000.00, 1),
('人事专员', 1, 7000.00, 1),
('人事经理', 3, 18000.00, 1),
('会计', 1, 8000.00, 1),
('财务经理', 3, 20000.00, 1),
('运营专员', 1, 7500.00, 1),
('运营经理', 3, 18000.00, 1)
ON DUPLICATE KEY UPDATE position_name=VALUES(position_name);

-- =====================================================
-- 3. 员工数据 (30人)
-- =====================================================
INSERT INTO `sys_emp` (`dept_id`, `emp_no`, `emp_name`, `phone`, `job_title`, `entry_date`, `emp_status`, `position_id`, `email`, `gender`, `entry_status`) VALUES
-- 技术部 (8人)
(1, 'EMP001', '张伟', '13800138001', '技术总监', '2020-01-15', 1, 3, 'zhangwei@company.com', 1, 2),
(1, 'EMP002', '李娜', '13800138002', '高级软件工程师', '2020-03-01', 1, 2, 'lina@company.com', 2, 2),
(1, 'EMP003', '王强', '13800138003', '高级软件工程师', '2020-06-15', 1, 2, 'wangqiang@company.com', 1, 2),
(1, 'EMP004', '刘洋', '13800138004', '软件工程师', '2021-01-10', 1, 1, 'liuyang@company.com', 1, 2),
(1, 'EMP005', '陈静', '13800138005', '软件工程师', '2021-03-20', 1, 1, 'chenjing@company.com', 2, 2),
(1, 'EMP006', '赵明', '13800138006', '软件工程师', '2021-07-01', 1, 1, 'zhaoming@company.com', 1, 2),
(1, 'EMP007', '孙丽', '13800138007', '软件工程师', '2022-01-15', 1, 1, 'sunli@company.com', 2, 1),
(1, 'EMP008', '周杰', '13800138008', '软件工程师', '2022-06-01', 1, 1, 'zhoujie@company.com', 1, 1),
-- 产品部 (5人)
(2, 'EMP009', '吴芳', '13800138009', '产品总监', '2019-06-01', 1, 3, 'wufang@company.com', 2, 2),
(2, 'EMP010', '郑涛', '13800138010', '高级产品经理', '2020-08-15', 1, 3, 'zhengtao@company.com', 1, 2),
(2, 'EMP011', '冯雪', '13800138011', '产品经理', '2021-04-01', 1, 2, 'fengxue@company.com', 2, 2),
(2, 'EMP012', '蒋磊', '13800138012', '产品经理', '2021-09-10', 1, 2, 'jianglei@company.com', 1, 2),
(2, 'EMP013', '韩梅', '13800138013', '产品助理', '2022-03-01', 1, 1, 'hanmei@company.com', 2, 1),
-- 市场部 (5人)
(3, 'EMP014', '杨帆', '13800138014', '市场总监', '2019-01-01', 1, 3, 'yangfan@company.com', 1, 2),
(3, 'EMP015', '朱琳', '13800138015', '市场经理', '2020-02-15', 1, 3, 'zhulin@company.com', 2, 2),
(3, 'EMP016', '许浩', '13800138016', '市场专员', '2021-05-01', 1, 1, 'xuhao@company.com', 1, 2),
(3, 'EMP017', '何燕', '13800138017', '市场专员', '2021-10-20', 1, 1, 'heyan@company.com', 2, 2),
(3, 'EMP018', '罗勇', '13800138018', '市场专员', '2022-04-15', 1, 1, 'luoyong@company.com', 1, 1),
-- 人事部 (4人)
(4, 'EMP019', '梁敏', '13800138019', '人事总监', '2018-06-01', 1, 3, 'liangmin@company.com', 2, 2),
(4, 'EMP020', '谢婷', '13800138020', '人事经理', '2020-04-01', 1, 3, 'xieting@company.com', 2, 2),
(4, 'EMP021', '邓超', '13800138021', '人事专员', '2021-06-15', 1, 1, 'dengchao@company.com', 1, 2),
(4, 'EMP022', '彭丽', '13800138022', '人事专员', '2022-02-01', 1, 1, 'pengli@company.com', 2, 1),
-- 财务部 (4人)
(5, 'EMP023', '曹峰', '13800138023', '财务总监', '2018-01-01', 1, 3, 'caofeng@company.com', 1, 2),
(5, 'EMP024', '袁华', '13800138024', '财务经理', '2019-09-01', 1, 3, 'yuanhua@company.com', 2, 2),
(5, 'EMP025', '邹静', '13800138025', '会计', '2020-11-15', 1, 1, 'zoujing@company.com', 2, 2),
(5, 'EMP026', '潘杰', '13800138026', '会计', '2021-08-01', 1, 1, 'panjie@company.com', 1, 2),
-- 运营部 (4人)
(6, 'EMP027', '范冰', '13800138027', '运营总监', '2019-04-01', 1, 3, 'fanbing@company.com', 2, 2),
(6, 'EMP028', '姜涛', '13800138028', '运营经理', '2020-07-15', 1, 3, 'jiangtao@company.com', 1, 2),
(6, 'EMP029', '白露', '13800138029', '运营专员', '2021-11-01', 1, 1, 'bailu@company.com', 2, 2),
(6, 'EMP030', '钱程', '13800138030', '运营专员', '2022-05-10', 1, 1, 'qiancheng@company.com', 1, 1)
ON DUPLICATE KEY UPDATE emp_name=VALUES(emp_name);

-- =====================================================
-- 4. 考勤数据 (最近20个工作日)
-- =====================================================
-- 为前10名员工生成考勤数据
INSERT INTO `sys_attendance` (`emp_id`, `attendance_date`, `check_in_time`, `check_out_time`, `work_hours`, `status`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:10:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:50:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 3 DAY), '09:15:00', '18:20:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP002'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:58:00', '18:30:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP002'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:45:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP002'), DATE_SUB(CURDATE(), INTERVAL 3 DAY), '08:55:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP003'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:10:00', '18:00:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP003'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:50:00', '18:10:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP004'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '17:50:00', 8.5, 3),
((SELECT id FROM sys_emp WHERE emp_no='EMP004'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '09:20:00', '18:00:00', 8.0, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP005'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:48:00', '18:15:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP005'), DATE_SUB(CURDATE(), INTERVAL 3 DAY), '08:55:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP006'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:05:00', '18:20:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP007'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:52:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP008'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:25:00', '18:30:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:40:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:55:00', '18:10:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP010'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:58:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP011'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:08:00', '17:45:00', 8.0, 3),
((SELECT id FROM sys_emp WHERE emp_no='EMP012'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:50:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP014'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:20:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP015'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:12:00', '18:00:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP016'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:48:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP019'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:45:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP020'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '09:05:00', '18:10:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP023'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:50:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP024'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP027'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP028'), DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:58:00', '18:15:00', 9.0, 1)
ON DUPLICATE KEY UPDATE check_in_time=VALUES(check_in_time);

-- =====================================================
-- 5. 请假数据
-- =====================================================
INSERT INTO `sys_leave` (`emp_id`, `leave_type`, `start_date`, `end_date`, `leave_days`, `reason`, `status`, `approver_id`, `approve_time`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP004'), 2, DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(CURDATE(), INTERVAL 24 DAY), 2, '家中有事需要处理', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 26 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP007'), 3, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 18 DAY), 3, '身体不适需要休息', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 21 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP013'), 2, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 1, '参加朋友婚礼', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 16 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP018'), 3, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 9 DAY), 2, '感冒发烧', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP014'), DATE_SUB(CURDATE(), INTERVAL 11 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP022'), 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), 1, '办理个人事务', 0, NULL, NULL),
((SELECT id FROM sys_emp WHERE emp_no='EMP026'), 1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 8 DAY), 4, '年假旅游', 0, NULL, NULL),
((SELECT id FROM sys_emp WHERE emp_no='EMP030'), 2, DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 1, '处理房屋事宜', 0, NULL, NULL);

-- =====================================================
-- 6. 加班数据
-- =====================================================
INSERT INTO `sys_overtime` (`emp_id`, `overtime_date`, `start_time`, `end_time`, `overtime_hours`, `overtime_type`, `reason`, `status`, `approver_id`, `approve_time`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP002'), DATE_SUB(CURDATE(), INTERVAL 20 DAY), '18:30:00', '21:30:00', 3, 1, '项目紧急上线', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 20 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP003'), DATE_SUB(CURDATE(), INTERVAL 18 DAY), '18:30:00', '22:00:00', 3.5, 1, '修复线上bug', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 18 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP004'), DATE_SUB(CURDATE(), INTERVAL 15 DAY), '09:00:00', '18:00:00', 8, 2, '周末版本发布', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 15 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP005'), DATE_SUB(CURDATE(), INTERVAL 12 DAY), '18:30:00', '20:30:00', 2, 1, '完成开发任务', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 12 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP006'), DATE_SUB(CURDATE(), INTERVAL 10 DAY), '18:30:00', '21:00:00', 2.5, 1, '代码优化', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 10 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP011'), DATE_SUB(CURDATE(), INTERVAL 8 DAY), '18:30:00', '20:00:00', 1.5, 1, '需求评审', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 8 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP002'), DATE_SUB(CURDATE(), INTERVAL 5 DAY), '18:30:00', '22:30:00', 4, 1, '紧急需求开发', 0, NULL, NULL),
((SELECT id FROM sys_emp WHERE emp_no='EMP003'), DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', '18:00:00', 8, 2, '周末加班赶进度', 0, NULL, NULL);

-- =====================================================
-- 7. 薪资数据 (最近2个月)
-- =====================================================
INSERT INTO `sys_salary` (`emp_id`, `salary_month`, `base_salary`, `position_salary`, `overtime_pay`, `bonus`, `subsidy`, `attendance_deduction`, `social_security`, `tax`, `total_salary`, `actual_salary`, `status`)
SELECT
    e.id,
    DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m'),
    COALESCE(p.base_salary, 8000),
    CASE WHEN e.job_title LIKE '%总监%' THEN 5000 WHEN e.job_title LIKE '%经理%' THEN 3000 ELSE 1000 END,
    500,
    CASE WHEN RAND() > 0.7 THEN 1000 ELSE 0 END,
    500,
    50,
    1500,
    800,
    0,
    0,
    1
FROM `sys_emp` e
LEFT JOIN `sys_position` p ON e.position_id = p.id
WHERE e.emp_status = 1
ON DUPLICATE KEY UPDATE actual_salary=VALUES(actual_salary);

-- 更新薪资计算
UPDATE `sys_salary` SET
    total_salary = base_salary + position_salary + overtime_pay + bonus + subsidy,
    actual_salary = base_salary + position_salary + overtime_pay + bonus + subsidy - attendance_deduction - social_security - tax;

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '模拟数据导入完成！' AS message;
