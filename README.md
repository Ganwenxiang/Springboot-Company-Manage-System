# 员工管理系统 (Employee Management System)

一个基于 Spring Boot + Vue 3 的企业级员工管理系统，包含员工管理、部门管理、考勤管理、请假管理、加班管理、薪资管理等核心功能。

## 技术栈

### 后端
- **Spring Boot 2.7** - 基础框架
- **MyBatis** - ORM框架
- **MySQL** - 数据库
- **Redis** - 缓存
- **JWT** - 身份认证
- **PageHelper** - 分页插件

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Element Plus** - UI组件库
- **Vue Router** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP请求

## 功能模块

| 模块 | 功能 |
|------|------|
| 员工管理 | 员工增删改查、高级搜索、详情查看 |
| 部门管理 | 部门树形结构、增删改 |
| 职位管理 | 职位管理、下拉选择 |
| 考勤管理 | 签到签退、考勤记录、月度汇总 |
| 请假管理 | 请假申请、审批流程 |
| 加班管理 | 加班申请、审批流程 |
| 薪资管理 | 薪资计算、发放管理 |
| 系统管理 | 用户管理、角色管理、菜单管理、权限控制 |
| 统计报表 | 数据概览、员工分布、考勤统计 |

## 项目结构

```
emps_Project/
├── src/main/java/          # 后端源码
│   └── com/example/emps_project/
│       ├── controller/     # 控制器层
│       ├── service/        # 业务层
│       ├── mapper/         # 数据层
│       ├── entity/         # 实体类
│       ├── dto/            # 数据传输对象
│       ├── common/         # 公共类
│       ├── security/       # 安全认证
│       └── annotation/     # 自定义注解
├── src/main/resources/
│   ├── mapper/             # MyBatis XML
│   └── application.yml     # 配置文件
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── views/          # 页面组件
│   │   ├── components/     # 公共组件
│   │   ├── api/            # 接口封装
│   │   ├── stores/         # 状态管理
│   │   ├── router/         # 路由配置
│   │   └── utils/          # 工具函数
│   └── package.json
├── start.bat               # 一键启动脚本
└── API接口文档.md          # 接口文档
```

## 环境要求

- **JDK 17+**
- **Node.js 20+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Maven 3.6+**

## 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd emps_Project
```

### 2. 配置数据库

创建数据库并导入SQL：
```sql
CREATE DATABASE emps_db DEFAULT CHARACTER SET utf8mb4;
```

修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emps_db
    username: root
    password: your_password
```

### 3. 启动依赖服务

确保 **MySQL** 和 **Redis** 已启动。

### 4. 启动项目

**方式一：一键启动（Windows）**
```bash
双击 start.bat
```

**方式二：手动启动**

启动后端：
```bash
mvn spring-boot:run
```

启动前端（新终端）：
```bash
cd frontend
npm install   # 首次运行需要安装依赖
npm run dev
```

### 5. 访问系统

- 前端地址：http://localhost:5173
- 后端地址：http://localhost:8080

### 6. 默认账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 管理员 |

## 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emps_db
    username: root
    password: 123456
  redis:
    host: localhost
    port: 6379

jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

### 前端配置

API 代理配置在 `frontend/vite.config.js`：
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## 项目截图

> 登录页面、首页概览、员工管理等页面截图

## 部署说明

### 打包部署

**后端打包：**
```bash
mvn clean package -DskipTests
java -jar target/emps_Project-0.0.1-SNAPSHOT.jar
```

**前端打包：**
```bash
cd frontend
npm run build
```

### Docker 部署（可选）

```bash
# 构建镜像
docker build -t emps-system .

# 运行容器
docker run -d -p 8080:8080 emps-system
```

## 常见问题

**Q: 启动报错 "Port 8080 is already in use"**
```bash
# Windows 查找并结束占用端口的进程
netstat -ano | findstr :8080
taskkill //F //PID <PID>
```

**Q: 前端启动报错 "Node version not supported"**
```bash
# 升级 Node.js 到 20.19+ 或 22.12+
```

**Q: Redis 连接失败**
```bash
# 确保 Redis 服务已启动
redis-cli ping
```

## 许可证

[MIT License](LICENSE)

## 联系方式

如有问题，请提交 Issue 或联系作者。
