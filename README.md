# 基金管理系统 (Fund Admin)

前后端分离的基金管理系统基础骨架，开箱即可运行。

## 技术栈

| 层 | 技术 |
|---|------|
| 前端 | Vue 3 + Vite + TypeScript + Element Plus + Pinia + Vue Router + Axios |
| 后端 | Spring Boot 3.2 + MyBatis Plus + Spring Security + JWT + Knife4j |
| 数据库 | MySQL 8.x |
| 构建 | Maven (后端) / Vite (前端) |

## 默认账号

- **用户名**: `admin`
- **密码**: `admin123`
- 验证码：登录页面自动获取，点击图片刷新

## 快速启动

### 1. 创建数据库

```sql
-- 执行建表脚本
source backend/src/main/resources/db/schema.sql;
-- 执行初始化数据
source backend/src/main/resources/db/data.sql;
```

或手动在 MySQL 中依次执行 `schema.sql` 和 `data.sql`。

### 2. 配置数据库连接

编辑 `backend/src/main/resources/application-dev.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fund_admin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: 你的数据库用户名
    password: 你的数据库密码
```

> 注意：生产环境密码必须加密存储，开发环境可直接使用明文方便调试。

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动后访问：
- API 接口：http://localhost:8080/api/health
- 接口文档：http://localhost:8080/doc.html

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端启动后访问：http://localhost:5173

### 5. 登录系统

1. 浏览器访问 http://localhost:5173
2. 输入用户名 `admin`，密码 `admin123`
3. 输入验证码（点击验证码图片可刷新）
4. 点击登录，成功后跳转仪表盘首页

## 访问地址汇总

| 项目 | 地址 |
|------|------|
| 前端页面 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |
| 接口文档 | http://localhost:8080/doc.html |
| 健康检查 | http://localhost:8080/api/health |

## 基础接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/auth/captcha | 获取验证码图片和标识 |
| POST | /api/auth/login | 用户登录 |
| POST | /api/auth/logout | 退出登录 |
| GET | /api/auth/me | 获取当前登录用户信息 |
| GET | /api/health | 健康检查 |

## 项目目录结构

### 前端

```
frontend/
  src/
    api/          # API 请求模块
    assets/       # 静态资源
    components/   # 公共组件
    config/       # 前端配置
    composables/  # 组合式函数
    layouts/      # 页面布局
    router/       # 路由配置
    stores/       # Pinia 状态管理
    styles/       # 全局样式
    types/        # TypeScript 类型
    utils/        # 工具函数
    views/        # 页面视图
      login/      # 登录页
      dashboard/  # 仪表盘
```

### 后端

```
backend/src/main/java/com/miujoke/fundadmin/
  common/        # 通用类(Result, PageResult, ResultCode)
  config/        # 配置类(Security, MybatisPlus, CORS, Knife4j)
  controller/    # 控制器
  dto/           # 数据传输对象(请求)
  entity/        # 实体类
  enums/         # 枚举
  exception/     # 异常处理
  mapper/        # MyBatis Mapper
  query/         # 查询条件对象
  security/      # 安全模块(JWT)
  service/       # 业务逻辑
    impl/        # 业务实现
  vo/            # 视图对象(响应)
  util/          # 工具类
```

## 认证鉴权

- **方案**: JWT + Spring Security
- **Token 生成**: 登录成功后后端生成 JWT Token
- **Token 存储**: 前端 localStorage 存储，请求头携带 `Authorization: Bearer <token>`
- **Token 过期**: 默认 24 小时(86400000ms)
- **密码加密**: BCrypt (强度10)

## 验证码

- **生成**: 后端 `CaptchaUtils` 生成图片验证码
- **存储**: 内存 ConcurrentHashMap (生产环境应替换为 Redis)
- **过期**: 300秒(5分钟)
- **刷新**: 前端点击验证码图片重新请求 `/api/auth/captcha`

## 部署

### Docker Compose

```bash
docker-compose up -d
```

包含 MySQL + 后端 + 前端(Nginx) 三个服务。

### 手动部署

1. 后端：`mvn clean package` → 部署 jar 包
2. 前端：`npm run build` → 部署 dist 到 Nginx
3. Nginx 反向代理 `/api/` 到后端

## 后续扩展

以下功能标注为后续扩展，不影响当前基础启动运行：

- Redis 缓存（验证码存储、Token 黑名单）
- 完整 RBAC 权限体系（角色管理、菜单权限）
- 审计日志
- 文件管理
- 数据字典
- 部门管理
- 接口限流、防重复提交
- 微服务拆分（当前为单体分层架构）