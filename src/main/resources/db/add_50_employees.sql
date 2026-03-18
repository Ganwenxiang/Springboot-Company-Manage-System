-- =====================================================
-- 新增50条员工模拟数据
-- =====================================================

INSERT INTO `sys_emp` (`dept_id`, `emp_no`, `emp_name`, `phone`, `job_title`, `entry_date`, `emp_status`, `position_id`, `email`, `gender`, `birth_date`, `id_card`, `address`, `emergency_contact`, `emergency_phone`, `entry_status`) VALUES
-- 技术部新增 (12人)
(1, 'EMP031', '林子轩', '13900139001', '高级软件工程师', '2021-02-15', 1, 2, 'linzixuan@company.com', 1, '1992-05-18', '310101199205181234', '上海市浦东新区张江高科技园区', '林父', '13900139002', 2),
(1, 'EMP032', '苏雨晴', '13900139003', '软件工程师', '2021-06-01', 1, 1, 'suyuqing@company.com', 2, '1995-08-22', '310101199508221234', '上海市徐汇区漕河泾开发区', '苏母', '13900139004', 2),
(1, 'EMP033', '马俊杰', '13900139005', '软件工程师', '2021-09-15', 1, 1, 'majunjie@company.com', 1, '1994-03-10', '310101199403101234', '上海市闵行区莘庄镇', '马父', '13900139006', 2),
(1, 'EMP034', '徐婉婷', '13900139007', '软件工程师', '2022-01-10', 1, 1, 'xuwanting@company.com', 2, '1997-11-25', '310101199711251234', '上海市嘉定区安亭镇', '徐母', '13900139008', 1),
(1, 'EMP035', '高志远', '13900139009', '软件工程师', '2022-03-20', 1, 1, 'gaozhiyuan@company.com', 1, '1996-07-08', '310101199607081234', '上海市松江区大学城', '高父', '13900139010', 1),
(1, 'EMP036', '沈梦瑶', '13900139011', '软件工程师', '2022-07-01', 1, 1, 'shenmengyao@company.com', 2, '1998-01-15', '310101199801151234', '上海市宝山区共康路', '沈母', '13900139012', 1),
(1, 'EMP037', '罗浩然', '13900139013', '高级软件工程师', '2020-11-01', 1, 2, 'luohaoran@company.com', 1, '1990-12-20', '310101199012201234', '上海市杨浦区五角场', '罗父', '13900139014', 2),
(1, 'EMP038', '唐雅琪', '13900139015', '软件工程师', '2022-09-15', 1, 1, 'tangyaqi@company.com', 2, '1997-04-12', '310101199704121234', '上海市普陀区长寿路', '唐母', '13900139016', 1),
(1, 'EMP039', '蔡文博', '13900139017', '软件工程师', '2023-01-10', 1, 1, 'caiwenbo@company.com', 1, '1999-02-28', '310101199902281234', '上海市虹口区四川北路', '蔡父', '13900139018', 1),
(1, 'EMP040', '贾欣怡', '13900139019', '软件工程师', '2023-03-01', 1, 1, 'jiaxinyi@company.com', 2, '2000-06-15', '310101200006151234', '上海市静安区南京西路', '贾母', '13900139020', 1),
(1, 'EMP041', '夏宇航', '13900139021', '软件工程师', '2023-06-15', 1, 1, 'xiayuhang@company.com', 1, '1998-09-10', '310101199809101234', '上海市长宁区虹桥路', '夏父', '13900139022', 1),
(1, 'EMP042', '汪思涵', '13900139023', '软件工程师', '2023-08-01', 1, 1, 'wangsihan@company.com', 2, '1999-12-05', '310101199912051234', '上海市黄浦区淮海中路', '汪母', '13900139024', 1),

-- 产品部新增 (8人)
(2, 'EMP043', '熊梓涵', '13900139025', '产品经理', '2021-07-15', 1, 2, 'xiongzihan@company.com', 1, '1993-10-18', '310101199310181234', '上海市浦东新区陆家嘴', '熊父', '13900139026', 2),
(2, 'EMP044', '白诗涵', '13900139027', '产品经理', '2022-02-01', 1, 2, 'baishihan@company.com', 2, '1995-03-22', '310101199503221234', '上海市徐汇区衡山路', '白母', '13900139028', 2),
(2, 'EMP045', '邓子轩', '13900139029', '产品助理', '2022-08-15', 1, 1, 'dengzixuan@company.com', 1, '1998-07-08', '310101199807081234', '上海市闵行区七宝镇', '邓父', '13900139030', 1),
(2, 'EMP046', '贺雨萱', '13900139031', '产品助理', '2023-01-15', 1, 1, 'heyuxuan@company.com', 2, '1999-11-20', '310101199911201234', '上海市嘉定区南翔镇', '贺母', '13900139032', 1),
(2, 'EMP047', '龚俊杰', '13900139033', '产品经理', '2021-11-01', 1, 2, 'gongjunjie@company.com', 1, '1994-05-15', '310101199405151234', '上海市松江区泗泾镇', '龚父', '13900139034', 2),
(2, 'EMP048', '孟雅婷', '13900139035', '高级产品经理', '2020-05-15', 1, 3, 'mengyating@company.com', 2, '1991-08-28', '310101199108281234', '上海市宝山区大华路', '孟母', '13900139036', 2),
(2, 'EMP049', '龙志强', '13900139037', '产品助理', '2023-04-01', 1, 1, 'longzhiqiang@company.com', 1, '2000-01-10', '310101200001101234', '上海市杨浦区控江路', '龙父', '13900139038', 1),
(2, 'EMP050', '史婉如', '13900139039', '产品经理', '2022-05-01', 1, 2, 'shiwanru@company.com', 2, '1996-09-25', '310101199609251234', '上海市普陀区真如镇', '史母', '13900139040', 2),

-- 市场部新增 (8人)
(3, 'EMP051', '侯俊杰', '13900139041', '市场专员', '2021-04-15', 1, 1, 'houjunjie@company.com', 1, '1995-06-12', '310101199506121234', '上海市虹口区凉城路', '侯父', '13900139042', 2),
(3, 'EMP052', '邵雨欣', '13900139043', '市场专员', '2022-01-20', 1, 1, 'shaoyuxin@company.com', 2, '1997-12-08', '310101199712081234', '上海市静安区曹家渡', '邵母', '13900139044', 1),
(3, 'EMP053', '孟浩宇', '13900139045', '市场经理', '2020-03-01', 1, 3, 'menghaoyu@company.com', 1, '1989-04-20', '310101198904201234', '上海市长宁区中山公园', '孟父', '13900139046', 2),
(3, 'EMP054', '尹诗琪', '13900139047', '市场专员', '2022-06-15', 1, 1, 'yinshiqi@company.com', 2, '1998-02-14', '310101199802141234', '上海市黄浦区人民广场', '尹母', '13900139048', 1),
(3, 'EMP055', '祁志豪', '13900139049', '市场专员', '2022-11-01', 1, 1, 'qizhihao@company.com', 1, '1997-08-30', '310101199708301234', '上海市浦东新区世纪公园', '祁父', '13900139050', 1),
(3, 'EMP056', '乔雨桐', '13900139051', '市场专员', '2023-02-15', 1, 1, 'qiaoyutong@company.com', 2, '1999-05-18', '310101199905181234', '上海市徐汇区上海南站', '乔母', '13900139052', 1),
(3, 'EMP057', '晏子豪', '13900139053', '市场经理', '2019-09-15', 1, 3, 'yanzihao@company.com', 1, '1990-11-22', '310101199011221234', '上海市闵行区虹桥枢纽', '晏父', '13900139054', 2),
(3, 'EMP058', '秦欣妍', '13900139055', '市场专员', '2023-05-01', 1, 1, 'qinxinyan@company.com', 2, '2000-03-08', '310101200003081234', '上海市嘉定区嘉定新城', '秦母', '13900139056', 1),

-- 人事部新增 (6人)
(4, 'EMP059', '尤志伟', '13900139057', '人事专员', '2021-08-01', 1, 1, 'youzhiwei@company.com', 1, '1994-10-15', '310101199410151234', '上海市松江区松江新城', '尤父', '13900139058', 2),
(4, 'EMP060', '姜雨涵', '13900139059', '人事专员', '2022-04-15', 1, 1, 'jiangyuhan@company.com', 2, '1996-07-28', '310101199607281234', '上海市宝山区顾村镇', '姜母', '13900139060', 1),
(4, 'EMP061', '邢志明', '13900139061', '人事经理', '2019-11-01', 1, 3, 'xingzhiming@company.com', 1, '1988-09-05', '310101198809051234', '上海市杨浦区新江湾城', '邢父', '13900139062', 2),
(4, 'EMP062', '石雨婷', '13900139063', '人事专员', '2022-09-01', 1, 1, 'shiyuting@company.com', 2, '1998-04-12', '310101199804121234', '上海市普陀区万里城', '石母', '13900139064', 1),
(4, 'EMP063', '范志杰', '13900139065', '人事专员', '2023-03-15', 1, 1, 'fanzhijie@company.com', 1, '1999-01-20', '310101199901201234', '上海市虹口区北外滩', '范父', '13900139066', 1),
(4, 'EMP064', '雷雨欣', '13900139067', '人事专员', '2023-07-01', 1, 1, 'leiyuxin@company.com', 2, '2000-08-15', '310101200008151234', '上海市静安区大宁路', '雷母', '13900139068', 1),

-- 财务部新增 (6人)
(5, 'EMP065', '钱志强', '13900139069', '会计', '2021-05-15', 1, 1, 'qianzhiqiang@company.com', 1, '1993-12-10', '310101199312101234', '上海市长宁区古北新区', '钱父', '13900139070', 2),
(5, 'EMP066', '孙雅文', '13900139071', '会计', '2022-03-01', 1, 1, 'sunyawen@company.com', 2, '1996-02-28', '310101199602281234', '上海市黄浦区老西门', '孙母', '13900139072', 2),
(5, 'EMP067', '李俊杰', '13900139073', '财务经理', '2019-07-15', 1, 3, 'lijunjie@company.com', 1, '1987-06-18', '310101198706181234', '上海市浦东新区金桥', '李父', '13900139074', 2),
(5, 'EMP068', '周诗雨', '13900139075', '会计', '2022-08-15', 1, 1, 'zhoushiyu@company.com', 2, '1997-11-05', '310101199711051234', '上海市徐汇区田林路', '周母', '13900139076', 1),
(5, 'EMP069', '吴志豪', '13900139077', '会计', '2023-01-20', 1, 1, 'wuzhihao@company.com', 1, '1998-05-22', '310101199805221234', '上海市闵行区梅陇镇', '吴父', '13900139078', 1),
(5, 'EMP070', '郑雨婷', '13900139079', '会计', '2023-06-01', 1, 1, 'zhengyuting@company.com', 2, '1999-09-30', '310101199909301234', '上海市嘉定区江桥镇', '郑母', '13900139080', 1),

-- 运营部新增 (10人)
(6, 'EMP071', '王志伟', '13900139081', '运营专员', '2021-10-15', 1, 1, 'wangzhiwei@company.com', 1, '1995-04-08', '310101199504081234', '上海市松江区佘山镇', '王父', '13900139082', 2),
(6, 'EMP072', '陈雨涵', '13900139083', '运营专员', '2022-02-01', 1, 1, 'chenyuhan@company.com', 2, '1997-06-15', '310101199706151234', '上海市宝山区罗店镇', '陈母', '13900139084', 1),
(6, 'EMP073', '张志明', '13900139085', '运营经理', '2020-06-01', 1, 3, 'zhangzhiming@company.com', 1, '1990-03-20', '310101199003201234', '上海市杨浦区五角场', '张父', '13900139086', 2),
(6, 'EMP074', '刘诗琪', '13900139087', '运营专员', '2022-07-15', 1, 1, 'liushiqi@company.com', 2, '1998-10-12', '310101199810121234', '上海市普陀区桃浦镇', '刘母', '13900139088', 1),
(6, 'EMP075', '赵志豪', '13900139089', '运营专员', '2022-11-20', 1, 1, 'zhaozhihao@company.com', 1, '1997-01-25', '310101199701251234', '上海市虹口区曲阳路', '赵父', '13900139090', 1),
(6, 'EMP076', '黄雨欣', '13900139091', '运营专员', '2023-03-01', 1, 1, 'huangyuxin@company.com', 2, '1999-07-18', '310101199907181234', '上海市静安区彭浦新村', '黄母', '13900139092', 1),
(6, 'EMP077', '孙俊杰', '13900139093', '运营专员', '2023-05-15', 1, 1, 'sunjunjie@company.com', 1, '1998-08-05', '310101199808051234', '上海市长宁区虹桥机场', '孙父', '13900139094', 1),
(6, 'EMP078', '周雨婷', '13900139095', '运营专员', '2023-08-01', 1, 1, 'zhouyuting@company.com', 2, '2000-02-10', '310101200002101234', '上海市黄浦区豫园', '周母', '13900139096', 1),
(6, 'EMP079', '吴志远', '13900139097', '运营专员', '2023-09-15', 1, 1, 'wuzhiyuan@company.com', 1, '1999-04-28', '310101199904281234', '上海市浦东新区川沙', '吴父', '13900139098', 1),
(6, 'EMP080', '郑欣怡', '13900139099', '运营专员', '2023-11-01', 2, 1, 'zhengxinyi@company.com', 2, '1998-12-15', '310101199812151234', '上海市徐汇区漕宝路', '郑母', '13900139100', 1)
ON DUPLICATE KEY UPDATE emp_name=VALUES(emp_name);

-- =====================================================
-- 为新员工生成部分考勤数据
-- =====================================================
INSERT INTO `sys_attendance` (`emp_id`, `attendance_date`, `check_in_time`, `check_out_time`, `work_hours`, `status`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP031'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:15:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP032'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:50:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP033'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:12:00', '18:30:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP034'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:58:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP035'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:45:00', '17:55:00', 8.5, 3),
((SELECT id FROM sys_emp WHERE emp_no='EMP043'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:52:00', '18:10:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP044'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:05:00', '18:00:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP051'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:48:00', '18:20:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP053'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP059'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:40:00', '18:05:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP065'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:55:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP067'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:08:00', '18:15:00', 8.5, 2),
((SELECT id FROM sys_emp WHERE emp_no='EMP071'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:50:00', '18:00:00', 9.0, 1),
((SELECT id FROM sys_emp WHERE emp_no='EMP073'), DATE_SUB(CURDATE(), INTERVAL 1 DAY), '08:58:00', '18:10:00', 9.0, 1)
ON DUPLICATE KEY UPDATE check_in_time=VALUES(check_in_time);

-- =====================================================
-- 为新员工生成部分请假数据
-- =====================================================
INSERT INTO `sys_leave` (`emp_id`, `leave_type`, `start_date`, `end_date`, `leave_days`, `reason`, `status`, `approver_id`, `approve_time`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP033'), 2, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 29 DAY), 2, '家中有事', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 31 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP045'), 3, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 14 DAY), 2, '身体不适', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 16 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP052'), 2, DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_SUB(CURDATE(), INTERVAL 8 DAY), 1, '参加面试', 0, NULL, NULL),
((SELECT id FROM sys_emp WHERE emp_no='EMP060'), 1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY), 4, '年假休息', 0, NULL, NULL),
((SELECT id FROM sys_emp WHERE emp_no='EMP072'), 2, DATE_ADD(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 15 DAY), 1, '处理个人事务', 0, NULL, NULL);

-- =====================================================
-- 为新员工生成部分加班数据
-- =====================================================
INSERT INTO `sys_overtime` (`emp_id`, `overtime_date`, `start_time`, `end_time`, `overtime_hours`, `overtime_type`, `reason`, `status`, `approver_id`, `approve_time`) VALUES
((SELECT id FROM sys_emp WHERE emp_no='EMP031'), DATE_SUB(CURDATE(), INTERVAL 10 DAY), '18:30:00', '21:00:00', 2.5, 1, '项目开发', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 10 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP037'), DATE_SUB(CURDATE(), INTERVAL 8 DAY), '18:30:00', '22:00:00', 3.5, 1, '紧急修复', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP001'), DATE_SUB(CURDATE(), INTERVAL 8 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP047'), DATE_SUB(CURDATE(), INTERVAL 5 DAY), '18:30:00', '20:30:00', 2, 1, '需求分析', 1, (SELECT id FROM sys_emp WHERE emp_no='EMP009'), DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
((SELECT id FROM sys_emp WHERE emp_no='EMP032'), DATE_ADD(CURDATE(), INTERVAL 3 DAY), '09:00:00', '18:00:00', 8, 2, '周末加班', 0, NULL, NULL);

-- =====================================================
-- 为新员工生成本月薪资数据
-- =====================================================
INSERT INTO `sys_salary` (`emp_id`, `salary_month`, `base_salary`, `position_salary`, `overtime_pay`, `bonus`, `subsidy`, `attendance_deduction`, `social_security`, `tax`, `total_salary`, `actual_salary`, `status`)
SELECT
    e.id,
    DATE_FORMAT(CURDATE(), '%Y-%m'),
    COALESCE(p.base_salary, 8000),
    CASE WHEN e.job_title LIKE '%总监%' THEN 5000 WHEN e.job_title LIKE '%经理%' THEN 3000 ELSE 1000 END,
    300,
    0,
    500,
    0,
    1500,
    500,
    0,
    0,
    0
FROM `sys_emp` e
LEFT JOIN `sys_position` p ON e.position_id = p.id
WHERE e.emp_no >= 'EMP031' AND e.emp_no <= 'EMP080'
ON DUPLICATE KEY UPDATE actual_salary=VALUES(actual_salary);

-- 更新薪资计算
UPDATE `sys_salary` SET
    total_salary = base_salary + position_salary + overtime_pay + bonus + subsidy,
    actual_salary = base_salary + position_salary + overtime_pay + bonus + subsidy - attendance_deduction - social_security - tax
WHERE salary_month = DATE_FORMAT(CURDATE(), '%Y-%m');

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '50条模拟数据导入完成！' AS message;