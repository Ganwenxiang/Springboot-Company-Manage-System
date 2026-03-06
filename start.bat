@echo off
chcp 65001 >nul
echo ========================================
echo     员工管理系统 - 一键启动脚本
echo ========================================
echo.

:: 检查是否在项目根目录
if not exist "pom.xml" (
    echo [错误] 请在项目根目录运行此脚本！
    pause
    exit /b 1
)

echo [1/2] 启动后端服务...
start "后端服务 - Spring Boot" cmd /k "mvn spring-boot:run"

echo [2/2] 启动前端服务...
cd frontend
start "前端服务 - Vue Dev Server" cmd /k "npm run dev"
cd ..

echo.
echo ========================================
echo 启动完成！
echo.
echo 后端地址: http://localhost:8080
echo 前端地址: http://localhost:5173
echo.
echo 默认账号: admin / 123456
echo ========================================
echo.
echo 按任意键退出此窗口（服务会继续运行）
pause >nul
