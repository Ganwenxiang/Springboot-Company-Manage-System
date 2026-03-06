-- =====================================================
-- 员工管理系统数据库表结构
-- =====================================================

-- =====================================================
-- 一、权限管理相关表
-- =====================================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT COMMENT '关联员工ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    `password` VARCHAR(100) NOT NULL COMMENT '登录密码（加密）',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(200) COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0停用',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_emp_id` (`emp_id`),
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0停用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. 菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID：0表示顶级菜单',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_type` TINYINT NOT NULL COMMENT '类型：1目录 2菜单 3按钮',
    `path` VARCHAR(200) COMMENT '路由路径',
    `component` VARCHAR(200) COMMENT '组件路径',
    `icon` VARCHAR(100) COMMENT '菜单图标',
    `permission` VARCHAR(100) COMMENT '权限标识',
    `order_num` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0停用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 4. 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 5. 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- =====================================================
-- 二、基础数据表（扩展）
-- =====================================================

-- 6. 部门表（已有，确认结构）
CREATE TABLE IF NOT EXISTS `sys_dept` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID：0表示顶级部门',
    `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
    `order_num` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0停用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 7. 员工表（已有，扩展字段）
CREATE TABLE IF NOT EXISTS `sys_emp` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `dept_id` BIGINT COMMENT '部门ID',
    `emp_no` VARCHAR(20) NOT NULL COMMENT '工号',
    `emp_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `job_title` VARCHAR(50) COMMENT '职位',
    `entry_date` DATE COMMENT '入职日期',
    `emp_status` TINYINT DEFAULT 1 COMMENT '状态：1在职 2离职 3停职',
    `position_id` BIGINT COMMENT '职位ID',
    `email` VARCHAR(50) COMMENT '邮箱',
    `gender` TINYINT COMMENT '性别：1男 2女',
    `birth_date` DATE COMMENT '出生日期',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `address` VARCHAR(200) COMMENT '居住地址',
    `emergency_contact` VARCHAR(50) COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) COMMENT '紧急联系电话',
    `entry_status` TINYINT DEFAULT 1 COMMENT '入职状态：1试用 2正式',
    `leave_date` DATE COMMENT '离职日期',
    `leave_reason` VARCHAR(200) COMMENT '离职原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_emp_no` (`emp_no`),
    INDEX `idx_dept_id` (`dept_id`),
    INDEX `idx_emp_status` (`emp_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 扩展员工表字段（如果表已存在）
-- ALTER TABLE sys_emp ADD COLUMN emp_status TINYINT DEFAULT 1 COMMENT '状态：1在职 2离职 3停职';
-- ALTER TABLE sys_emp ADD COLUMN position_id BIGINT COMMENT '职位ID';
-- ALTER TABLE sys_emp ADD COLUMN email VARCHAR(50) COMMENT '邮箱';
-- ALTER TABLE sys_emp ADD COLUMN gender TINYINT COMMENT '性别：1男 2女';
-- ALTER TABLE sys_emp ADD COLUMN birth_date DATE COMMENT '出生日期';
-- ALTER TABLE sys_emp ADD COLUMN id_card VARCHAR(18) COMMENT '身份证号';
-- ALTER TABLE sys_emp ADD COLUMN address VARCHAR(200) COMMENT '居住地址';
-- ALTER TABLE sys_emp ADD COLUMN emergency_contact VARCHAR(50) COMMENT '紧急联系人';
-- ALTER TABLE sys_emp ADD COLUMN emergency_phone VARCHAR(20) COMMENT '紧急联系电话';
-- ALTER TABLE sys_emp ADD COLUMN entry_status TINYINT DEFAULT 1 COMMENT '入职状态：1试用 2正式';
-- ALTER TABLE sys_emp ADD COLUMN leave_date DATE COMMENT '离职日期';
-- ALTER TABLE sys_emp ADD COLUMN leave_reason VARCHAR(200) COMMENT '离职原因';

-- 8. 职位表
CREATE TABLE IF NOT EXISTS `sys_position` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `position_name` VARCHAR(50) NOT NULL COMMENT '职位名称',
    `position_level` INT DEFAULT 1 COMMENT '职位级别',
    `base_salary` DECIMAL(10,2) DEFAULT 0 COMMENT '基本工资',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1启用 0停用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_position_name` (`position_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- =====================================================
-- 三、业务功能表
-- =====================================================

-- 9. 考勤记录表
CREATE TABLE IF NOT EXISTS `sys_attendance` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `attendance_date` DATE NOT NULL COMMENT '考勤日期',
    `check_in_time` TIME COMMENT '签到时间',
    `check_out_time` TIME COMMENT '签退时间',
    `work_hours` DECIMAL(4,2) DEFAULT 0 COMMENT '工作时长(小时)',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 2迟到 3早退 4缺勤',
    `check_in_location` VARCHAR(100) COMMENT '签到地点',
    `check_out_location` VARCHAR(100) COMMENT '签退地点',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_emp_date` (`emp_id`, `attendance_date`),
    INDEX `idx_attendance_date` (`attendance_date`),
    INDEX `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- 10. 请假申请表
CREATE TABLE IF NOT EXISTS `sys_leave` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `leave_type` TINYINT NOT NULL COMMENT '类型：1年假 2事假 3病假 4调休',
    `start_date` DATETIME NOT NULL COMMENT '开始时间',
    `end_date` DATETIME NOT NULL COMMENT '结束时间',
    `leave_days` DECIMAL(4,1) NOT NULL COMMENT '请假天数',
    `reason` VARCHAR(500) COMMENT '请假原因',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0待审批 1已批准 2已拒绝 3已撤销',
    `approver_id` BIGINT COMMENT '审批人ID',
    `approve_time` DATETIME COMMENT '审批时间',
    `approve_remark` VARCHAR(200) COMMENT '审批备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_emp_id` (`emp_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';

-- 11. 加班记录表
CREATE TABLE IF NOT EXISTS `sys_overtime` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `overtime_date` DATE NOT NULL COMMENT '加班日期',
    `start_time` TIME NOT NULL COMMENT '开始时间',
    `end_time` TIME NOT NULL COMMENT '结束时间',
    `overtime_hours` DECIMAL(4,2) NOT NULL COMMENT '加班时长(小时)',
    `overtime_type` TINYINT DEFAULT 1 COMMENT '类型：1工作日 2周末 3节假日',
    `reason` VARCHAR(200) COMMENT '加班原因',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0待审批 1已批准 2已拒绝',
    `approver_id` BIGINT COMMENT '审批人ID',
    `approve_time` DATETIME COMMENT '审批时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_emp_id` (`emp_id`),
    INDEX `idx_overtime_date` (`overtime_date`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='加班记录表';

-- 12. 薪资记录表
CREATE TABLE IF NOT EXISTS `sys_salary` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `salary_month` VARCHAR(7) NOT NULL COMMENT '薪资月份 yyyy-MM',
    `base_salary` DECIMAL(10,2) NOT NULL COMMENT '基本工资',
    `position_salary` DECIMAL(10,2) DEFAULT 0 COMMENT '岗位工资',
    `overtime_pay` DECIMAL(10,2) DEFAULT 0 COMMENT '加班费',
    `bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '奖金',
    `subsidy` DECIMAL(10,2) DEFAULT 0 COMMENT '补贴',
    `attendance_deduction` DECIMAL(10,2) DEFAULT 0 COMMENT '考勤扣款',
    `social_security` DECIMAL(10,2) DEFAULT 0 COMMENT '社保',
    `tax` DECIMAL(10,2) DEFAULT 0 COMMENT '个税',
    `total_salary` DECIMAL(10,2) NOT NULL COMMENT '应发工资',
    `actual_salary` DECIMAL(10,2) NOT NULL COMMENT '实发工资',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0未发放 1已发放',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_emp_month` (`emp_id`, `salary_month`),
    INDEX `idx_salary_month` (`salary_month`),
    INDEX `idx_emp_id` (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资记录表';

-- 13. 员工状态变更记录表
CREATE TABLE IF NOT EXISTS `sys_emp_status_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `emp_id` BIGINT NOT NULL COMMENT '员工ID',
    `change_type` TINYINT NOT NULL COMMENT '类型：1入职 2调岗 3离职 4复职',
    `before_dept_id` BIGINT COMMENT '原部门ID',
    `after_dept_id` BIGINT COMMENT '新部门ID',
    `before_position` VARCHAR(50) COMMENT '原职位',
    `after_position` VARCHAR(50) COMMENT '新职位',
    `change_date` DATE NOT NULL COMMENT '变更日期',
    `reason` VARCHAR(200) COMMENT '变更原因',
    `operator_id` BIGINT COMMENT '操作人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_emp_id` (`emp_id`),
    INDEX `idx_change_date` (`change_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工状态变更记录表';

-- =====================================================
-- 四、初始数据
-- =====================================================

-- 插入初始角色数据
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`) VALUES
('超级管理员', 'admin', '拥有所有权限'),
('部门主管', 'manager', '管理部门内员工和审批'),
('普通员工', 'employee', '普通员工权限')
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name);

-- 插入初始管理员用户（密码: admin123，已加密）
-- 注意：这是使用 PasswordEncoder 工具加密后的密码
INSERT INTO `sys_user` (`emp_id`, `username`, `password`, `nickname`, `status`) VALUES
(NULL, 'admin', 'V1hQYVczcXRUbGZIY1RwaFFXSXhVUT09', '系统管理员', 1)
ON DUPLICATE KEY UPDATE password=VALUES(password);

-- 为管理员分配角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `sys_user` u, `sys_role` r
WHERE u.username = 'admin' AND r.role_code = 'admin'
ON DUPLICATE KEY UPDATE user_id=VALUES(user_id);

-- 插入初始菜单数据
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `permission`, `order_num`) VALUES
(0, '系统管理', 1, '/system', NULL, 'setting', NULL, 100),
(0, '员工管理', 1, '/employee', NULL, 'user', NULL, 1),
(0, '部门管理', 1, '/dept', NULL, 'apartment', NULL, 2),
(0, '考勤管理', 1, '/attendance', NULL, 'clock', NULL, 3),
(0, '请假管理', 1, '/leave', NULL, 'document', NULL, 4),
(0, '加班管理', 1, '/overtime', NULL, 'time', NULL, 5),
(0, '薪资管理', 1, '/salary', NULL, 'money', NULL, 6),
(0, '统计报表', 1, '/statistics', NULL, 'chart', NULL, 7)
ON DUPLICATE KEY UPDATE menu_name=VALUES(menu_name);

-- 插入系统管理子菜单
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `permission`, `order_num`)
SELECT
    (SELECT id FROM (SELECT id FROM sys_menu WHERE path='/system') AS tmp),
    '用户管理',
    2,
    '/system/users',
    'system/UserList',
    NULL,
    'system:user',
    1
FROM DUAL
WHERE EXISTS (SELECT 1 FROM sys_menu WHERE path='/system');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `permission`, `order_num`)
SELECT
    (SELECT id FROM (SELECT id FROM sys_menu WHERE path='/system') AS tmp),
    '角色管理',
    2,
    '/system/roles',
    'system/RoleList',
    NULL,
    'system:role',
    2
FROM DUAL
WHERE EXISTS (SELECT 1 FROM sys_menu WHERE path='/system');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `path`, `component`, `icon`, `permission`, `order_num`)
SELECT
    (SELECT id FROM (SELECT id FROM sys_menu WHERE path='/system') AS tmp),
    '菜单管理',
    2,
    '/system/menus',
    'system/MenuList',
    NULL,
    'system:menu',
    3
FROM DUAL
WHERE EXISTS (SELECT 1 FROM sys_menu WHERE path='/system');

-- 为管理员角色分配所有菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT r.id, m.id
FROM (SELECT id FROM sys_role WHERE role_code = 'admin') AS r
CROSS JOIN (SELECT id FROM sys_menu) AS m;

-- =====================================================
-- 五、创建索引优化查询
-- =====================================================

-- 为常用查询创建复合索引
CREATE INDEX idx_emp_dept_status ON sys_emp(dept_id, emp_status);
CREATE INDEX idx_attendance_emp_date ON sys_attendance(emp_id, attendance_date);
CREATE INDEX idx_leave_emp_status ON sys_leave(emp_id, status);
CREATE INDEX idx_overtime_emp_status ON sys_overtime(emp_id, status);
CREATE INDEX idx_salary_emp_month ON sys_salary(emp_id, salary_month);
