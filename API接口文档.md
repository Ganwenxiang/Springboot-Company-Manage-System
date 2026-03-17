# 员工管理系统 API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **认证方式**: Bearer Token (JWT)
- **请求格式**: JSON
- **响应格式**: JSON

## 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200表示成功，其他表示失败 |
| message | String | 提示信息 |
| data | Object | 返回数据 |

---

## 1. 认证授权模块 (`/api/auth`)

### 1.1 用户登录
- **接口**: `POST /api/auth/login`
- **描述**: 用户登录认证，获取Token
- **是否需要认证**: 否

**请求参数**:
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1
  }
}
```

##知识点
### 1、使用spl id 标签可以定义需要查全整个数据表的字段，mybaties的XML通过refid进行调用，避免重复书写数据表字段
### 2、ThreafLocal的使用

---

### 1.2 用户登出
- **接口**: `POST /api/auth/logout`
- **描述**: 用户退出登录，清除Token
- **是否需要认证**: 是

**响应示例**:
```json
{
  "code": 200,
  "message": "登出成功"
}
```

---

### 1.3 获取当前用户信息
- **接口**: `GET /api/auth/info`
- **描述**: 获取当前登录用户的详细信息、角色、权限和菜单
- **是否需要认证**: 是

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "username": "admin",
    "nickname": "管理员",
    "roles": ["admin"],
    "permissions": ["*:*:*"],
    "menuTree": [],
    "isAdmin": true
  }
}
```

---

### 1.4 修改密码
- **接口**: `PUT /api/auth/password`
- **描述**: 修改当前用户密码
- **是否需要认证**: 是

**请求参数**:
```json
{
  "oldPassword": "123456",
  "newPassword": "newpass123",
  "confirmPassword": "newpass123"
}
```

---

### 1.5 刷新Token
- **接口**: `POST /api/auth/refresh`
- **描述**: 刷新过期的Token
- **是否需要认证**: 是

**请求头**:
```
Authorization: Bearer <old_token>
```

---

## 2. 员工管理模块 (`/api/employees`)

### 2.1 分页查询员工列表
- **接口**: `GET /api/employees`
- **描述**: 分页查询员工列表，支持条件筛选
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| empName | String | 否 | 员工姓名（模糊查询） |
| deptId | Long | 否 | 部门ID |

---

### 2.2 高级搜索员工
- **接口**: `GET /api/employees/search`
- **描述**: 高级搜索员工，支持多条件联合查询
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页条数 |
| empName | String | 否 | 员工姓名 |
| jobTitle | String | 否 | 职位名称 |
| entryDateStart | Date | 否 | 入职日期开始 |
| entryDateEnd | Date | 否 | 入职日期结束 |

---

### 2.3 获取员工详情
- **接口**: `GET /api/employees/{id}`
- **描述**: 根据ID获取员工详细信息
- **是否需要认证**: 是

**路径参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 员工ID |

---

### 2.4 新增员工
- **接口**: `POST /api/employees`
- **描述**: 新增员工信息
- **是否需要认证**: 是

**请求参数**:
```json
{
  "empNo": "EMP001",
  "empName": "张三",
  "deptId": 1,
  "positionId": 1,
  "phone": "13800138000",
  "email": "zhangsan@company.com",
  "jobTitle": "软件工程师",
  "entryDate": "2024-01-01",
  "gender": 1,
  "birthDate": "1990-01-01"
}
```

---

### 2.5 修改员工
- **接口**: `PUT /api/employees`
- **描述**: 修改员工信息
- **是否需要认证**: 是

---

### 2.6 删除员工
- **接口**: `DELETE /api/employees/{id}`
- **描述**: 根据ID删除员工
- **是否需要认证**: 是

---

## 3. 部门管理模块 (`/api/depts`)

### 3.1 获取部门树
- **接口**: `GET /api/depts`
- **描述**: 获取部门树形结构（带Redis缓存）
- **是否需要认证**: 是

**响应示例**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "deptName": "技术部",
      "parentId": 0,
      "children": [
        {
          "id": 2,
          "deptName": "前端组",
          "parentId": 1
        }
      ]
    }
  ]
}
```

---

### 3.2 新增部门
- **接口**: `POST /api/depts`
- **描述**: 新增部门
- **是否需要认证**: 是

**请求参数**:
```json
{
  "deptName": "技术部",
  "parentId": 0,
  "orderNum": 1,
  "leader": "张三",
  "phone": "13800138000"
}
```

---

### 3.3 更新部门
- **接口**: `PUT /api/depts`
- **描述**: 更新部门信息
- **是否需要认证**: 是

---

### 3.4 删除部门
- **接口**: `DELETE /api/depts/{id}`
- **描述**: 删除部门（存在子部门时不允许删除）
- **是否需要认证**: 是

---

## 4. 考勤管理模块 (`/api/attendance`)

### 4.1 员工签到
- **接口**: `POST /api/attendance/check-in`
- **描述**: 员工上班签到
- **是否需要认证**: 是

**请求参数**:
```json
{
  "location": "北京市朝阳区望京SOHO"
}
```

---

### 4.2 员工签退
- **接口**: `POST /api/attendance/check-out`
- **描述**: 员工下班签退
- **是否需要认证**: 是

**请求参数**:
```json
{
  "location": "北京市朝阳区望京SOHO"
}
```

---

### 4.3 获取今日考勤
- **接口**: `GET /api/attendance/my-today`
- **描述**: 获取当前用户今日考勤记录
- **是否需要认证**: 是

---

### 4.4 查询月度考勤
- **接口**: `GET /api/attendance/monthly`
- **描述**: 查询当前用户指定月份的考勤记录
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| year | Integer | 否 | 年份，默认当前年 |
| month | Integer | 否 | 月份，默认当前月 |

---

### 4.5 管理端查询考勤记录
- **接口**: `GET /api/attendance`
- **描述**: 管理员分页查询考勤记录
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| pageNum | Integer | 页码 |
| pageSize | Integer | 每页条数 |
| empId | Long | 员工ID |
| empName | String | 员工姓名 |
| attendanceDate | Date | 考勤日期 |
| startDate | Date | 开始日期 |
| endDate | Date | 结束日期 |
| status | Integer | 状态 |

---

### 4.6 修正考勤记录
- **接口**: `PUT /api/attendance/{id}`
- **描述**: 管理员修正考勤记录
- **是否需要认证**: 是

---

### 4.7 月度考勤汇总
- **接口**: `GET /api/attendance/monthly-summary`
- **描述**: 获取月度考勤汇总统计
- **是否需要认证**: 是

---

## 5. 请假管理模块 (`/api/leave`)

### 5.1 查询我的请假申请
- **接口**: `GET /api/leave/my-requests`
- **描述**: 获取当前用户的请假申请列表
- **是否需要认证**: 是

---

### 5.2 提交请假申请
- **接口**: `POST /api/leave`
- **描述**: 提交新的请假申请
- **是否需要认证**: 是

**请求参数**:
```json
{
  "leaveType": 1,
  "startDate": "2024-01-15",
  "endDate": "2024-01-16",
  "leaveDays": 2,
  "leaveReason": "家中有事"
}
```

| leaveType | 说明 |
|-----------|------|
| 1 | 事假 |
| 2 | 病假 |
| 3 | 年假 |
| 4 | 婚假 |
| 5 | 产假 |

---

### 5.3 撤销请假申请
- **接口**: `PUT /api/leave/{id}/cancel`
- **描述**: 撤销待审批的请假申请
- **是否需要认证**: 是

---

### 5.4 查询待审批列表
- **接口**: `GET /api/leave/pending`
- **描述**: 获取待审批的请假申请列表
- **是否需要认证**: 是

---

### 5.5 审批通过
- **接口**: `PUT /api/leave/{id}/approve`
- **描述**: 审批通过请假申请
- **是否需要认证**: 是

**请求参数**:
```json
{
  "remark": "同意"
}
```

---

### 5.6 审批拒绝
- **接口**: `PUT /api/leave/{id}/reject`
- **描述**: 审批拒绝请假申请
- **是否需要认证**: 是

**请求参数**:
```json
{
  "remark": "拒绝原因"
}
```

---

### 5.7 管理端查询请假记录
- **接口**: `GET /api/leave`
- **描述**: 管理员分页查询请假记录
- **是否需要认证**: 是

---

### 5.8 删除请假申请
- **接口**: `DELETE /api/leave/{id}`
- **描述**: 删除请假申请记录
- **是否需要认证**: 是

---

## 6. 加班管理模块 (`/api/overtime`)

### 6.1 查询我的加班记录
- **接口**: `GET /api/overtime/my-records`
- **描述**: 获取当前用户的加班记录
- **是否需要认证**: 是

---

### 6.2 提交加班申请
- **接口**: `POST /api/overtime`
- **描述**: 提交加班申请
- **是否需要认证**: 是

**请求参数**:
```json
{
  "overtimeType": 1,
  "overtimeDate": "2024-01-15",
  "startTime": "09:00",
  "endTime": "18:00",
  "overtimeHours": 8,
  "overtimeReason": "项目紧急"
}
```

---

### 6.3 查询待审批列表
- **接口**: `GET /api/overtime/pending`
- **描述**: 获取待审批的加班申请列表
- **是否需要认证**: 是

---

### 6.4 审批通过
- **接口**: `PUT /api/overtime/{id}/approve`
- **描述**: 审批通过加班申请
- **是否需要认证**: 是

---

### 6.5 审批拒绝
- **接口**: `PUT /api/overtime/{id}/reject`
- **描述**: 审批拒绝加班申请
- **是否需要认证**: 是

---

### 6.6 管理端查询加班记录
- **接口**: `GET /api/overtime`
- **描述**: 管理员分页查询加班记录
- **是否需要认证**: 是

---

### 6.7 删除加班记录
- **接口**: `DELETE /api/overtime/{id}`
- **描述**: 删除加班记录
- **是否需要认证**: 是

---

## 7. 薪资管理模块 (`/api/salary`)

### 7.1 查询我的薪资记录
- **接口**: `GET /api/salary/my-salaries`
- **描述**: 获取当前用户的薪资记录
- **是否需要认证**: 是

---

### 7.2 生成月度薪资
- **接口**: `POST /api/salary/calculate`
- **描述**: 生成指定月份的薪资数据
- **是否需要认证**: 是

**请求参数**:
```json
{
  "salaryMonth": "2024-01"
}
```

---

### 7.3 管理端查询薪资记录
- **接口**: `GET /api/salary`
- **描述**: 管理员分页查询薪资记录
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| pageNum | Integer | 页码 |
| pageSize | Integer | 每页条数 |
| empId | Long | 员工ID |
| empName | String | 员工姓名 |
| salaryMonth | String | 薪资月份(yyyy-MM) |
| status | Integer | 状态 |

---

### 7.4 薪资详情
- **接口**: `GET /api/salary/{id}`
- **描述**: 获取薪资详情
- **是否需要认证**: 是

---

### 7.5 调整薪资
- **接口**: `PUT /api/salary/{id}`
- **描述**: 调整员工薪资
- **是否需要认证**: 是

---

### 7.6 标记已发放
- **接口**: `PUT /api/salary/{id}/pay`
- **描述**: 标记薪资已发放
- **是否需要认证**: 是

---

### 7.7 删除薪资记录
- **接口**: `DELETE /api/salary/{id}`
- **描述**: 删除薪资记录
- **是否需要认证**: 是

---

### 7.8 薪资汇总统计
- **接口**: `GET /api/salary/summary`
- **描述**: 获取指定月份的薪资汇总统计
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| salaryMonth | String | 是 | 薪资月份(yyyy-MM) |

---

## 8. 职位管理模块 (`/api/positions`)

### 8.1 分页查询职位列表
- **接口**: `GET /api/positions`
- **描述**: 分页查询职位列表
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| pageNum | Integer | 页码 |
| pageSize | Integer | 每页条数 |
| positionName | String | 职位名称 |
| status | Integer | 状态 |

---

### 8.2 查询所有职位
- **接口**: `GET /api/positions/all`
- **描述**: 获取所有职位（不分页，用于下拉选择）
- **是否需要认证**: 是

---

### 8.3 获取职位详情
- **接口**: `GET /api/positions/{id}`
- **描述**: 根据ID获取职位详情
- **是否需要认证**: 是

---

### 8.4 新增职位
- **接口**: `POST /api/positions`
- **描述**: 新增职位
- **是否需要认证**: 是

**请求参数**:
```json
{
  "positionName": "高级工程师",
  "positionCode": "SENIOR_ENGINEER",
  "description": "高级软件开发工程师",
  "status": 1
}
```

---

### 8.5 更新职位
- **接口**: `PUT /api/positions`
- **描述**: 更新职位信息
- **是否需要认证**: 是

---

### 8.6 删除职位
- **接口**: `DELETE /api/positions/{id}`
- **描述**: 删除单个职位
- **是否需要认证**: 是

---

### 8.7 批量删除职位
- **接口**: `DELETE /api/positions`
- **描述**: 批量删除职位
- **是否需要认证**: 是

**请求参数**:
```json
[1, 2, 3]
```

---

## 9. 系统管理模块 (`/api/system`)

### 9.1 用户管理

#### 9.1.1 分页查询用户列表
- **接口**: `GET /api/system/users`
- **描述**: 分页查询系统用户列表
- **是否需要认证**: 是

---

#### 9.1.2 获取用户详情
- **接口**: `GET /api/system/users/{id}`
- **描述**: 根据ID获取用户详情
- **是否需要认证**: 是

---

#### 9.1.3 新增用户
- **接口**: `POST /api/system/users`
- **描述**: 新增系统用户
- **是否需要认证**: 是

**请求参数**:
```json
{
  "username": "zhangsan",
  "password": "123456",
  "nickname": "张三",
  "email": "zhangsan@company.com",
  "phone": "13800138000",
  "status": 1,
  "roleIds": [2, 3]
}
```

---

#### 9.1.4 更新用户
- **接口**: `PUT /api/system/users`
- **描述**: 更新用户信息
- **是否需要认证**: 是

---

#### 9.1.5 删除用户
- **接口**: `DELETE /api/system/users/{id}`
- **描述**: 删除用户
- **是否需要认证**: 是

---

#### 9.1.6 重置密码
- **接口**: `PUT /api/system/users/{id}/reset-pwd`
- **描述**: 重置用户密码为默认密码(123456)
- **是否需要认证**: 是

---

### 9.2 角色管理

#### 9.2.1 查询角色列表
- **接口**: `GET /api/system/roles`
- **描述**: 分页查询角色列表
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| pageNum | Integer | 页码 |
| pageSize | Integer | 每页条数 |
| roleName | String | 角色名称 |
| status | Integer | 状态 |

---

#### 9.2.2 获取角色详情
- **接口**: `GET /api/system/roles/{id}`
- **描述**: 根据ID获取角色详情
- **是否需要认证**: 是

---

#### 9.2.3 新增角色
- **接口**: `POST /api/system/roles`
- **描述**: 新增角色
- **是否需要认证**: 是

**请求参数**:
```json
{
  "roleName": "部门主管",
  "roleCode": "manager",
  "description": "管理部门内员工",
  "status": 1,
  "menuIds": [1, 2, 3]
}
```

---

#### 9.2.4 更新角色
- **接口**: `PUT /api/system/roles`
- **描述**: 更新角色信息
- **是否需要认证**: 是

---

#### 9.2.5 删除角色
- **接口**: `DELETE /api/system/roles/{id}`
- **描述**: 删除角色（管理员角色不可删除）
- **是否需要认证**: 是

---

#### 9.2.6 查询角色菜单权限
- **接口**: `GET /api/system/role/{roleId}/menus`
- **描述**: 获取角色拥有的菜单ID列表
- **是否需要认证**: 是

---

#### 9.2.7 分配角色菜单权限
- **接口**: `PUT /api/system/role/{roleId}/menus`
- **描述**: 为角色分配菜单权限
- **是否需要认证**: 是

**请求参数**:
```json
[1, 2, 3, 4, 5]
```

---

### 9.3 菜单管理

#### 9.3.1 查询当前用户菜单树
- **接口**: `GET /api/system/menus`
- **描述**: 获取当前登录用户可访问的菜单树
- **是否需要认证**: 是

---

#### 9.3.2 查询所有菜单
- **接口**: `GET /api/system/menus/all`
- **描述**: 获取所有菜单（管理端使用）
- **是否需要认证**: 是

---

#### 9.3.3 获取菜单详情
- **接口**: `GET /api/system/menus/{id}`
- **描述**: 根据ID获取菜单详情
- **是否需要认证**: 是

---

#### 9.3.4 新增菜单
- **接口**: `POST /api/system/menus`
- **描述**: 新增菜单
- **是否需要认证**: 是

**请求参数**:
```json
{
  "menuName": "员工管理",
  "menuType": 1,
  "parentId": 0,
  "path": "/employee",
  "component": "Employee/index",
  "icon": "user",
  "permission": "employee:view",
  "orderNum": 1,
  "status": 1
}
```

| menuType | 说明 |
|----------|------|
| 1 | 目录 |
| 2 | 菜单 |
| 3 | 按钮 |

---

#### 9.3.5 更新菜单
- **接口**: `PUT /api/system/menus`
- **描述**: 更新菜单信息
- **是否需要认证**: 是

---

#### 9.3.6 删除菜单
- **接口**: `DELETE /api/system/menus/{id}`
- **描述**: 删除菜单
- **是否需要认证**: 是

---

## 10. 统计分析模块 (`/api/statistics`)

### 10.1 获取首页概览数据
- **接口**: `GET /api/statistics/overview`
- **描述**: 获取首页统计概览数据
- **是否需要认证**: 是

**响应示例**:
```json
{
  "code": 200,
  "data": {
    "totalEmployees": 100,
    "todayAttendance": 95,
    "pendingLeaves": 5,
    "pendingOvertimes": 3,
    "monthlySalaryTotal": 500000
  }
}
```

---

### 10.2 获取员工分布统计
- **接口**: `GET /api/statistics/emp-distribution`
- **描述**: 获取员工按部门分布统计
- **是否需要认证**: 是

---

### 10.3 获取考勤汇总统计
- **接口**: `GET /api/statistics/attendance-summary`
- **描述**: 获取指定日期范围的考勤汇总
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startDate | Date | 是 | 开始日期(yyyy-MM-dd) |
| endDate | Date | 是 | 结束日期(yyyy-MM-dd) |

---

### 10.4 获取部门考勤对比
- **接口**: `GET /api/statistics/dept-attendance`
- **描述**: 获取各部门考勤对比数据
- **是否需要认证**: 是

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| month | String | 是 | 月份(yyyy-MM) |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未登录或Token过期 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 注意事项

1. 所有需要认证的接口，请求头必须携带Token：
   ```
   Authorization: Bearer <token>
   ```

2. 分页接口默认参数：
   - `pageNum`: 默认为1
   - `pageSize`: 默认为10

3. 日期格式：
   - 日期：`yyyy-MM-dd`
   - 月份：`yyyy-MM`
   - 时间：`HH:mm`

4. 部门管理接口使用Redis缓存，数据变更时会自动清除缓存

5. 角色/菜单的删除操作有保护机制，核心数据不允许删除
