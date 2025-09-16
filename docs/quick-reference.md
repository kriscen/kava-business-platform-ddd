# Kava DDD 模块生成快速参考

## 🚀 一分钟生成新模块

### 第一步：复制提示词模板
```
我需要基于现有的 kbpd-demo 模板创建一个新的业务模块。

### 项目背景
- 项目采用DDD分层架构
- 使用Spring Boot 3.x + MyBatis Plus + Nacos
- 参考模块：kbpd-demo（模板）、kbpd-upms（实现示例）

### 需求信息
- **模块名称**: [填写模块名，如order/goods/customer]
- **主实体名**: [填写实体名，如Order/Product/Customer]
- **业务场景**: [简要描述业务功能]
- **核心属性**: [列出主要业务字段]

### 生成要求
1. **严格遵循DDD分层架构**：api、adapter、application、bootstrap、domain、infrastructure、types
2. **参考kbpd-upms的实现模式**：特别是SysUser相关的所有类
3. **包含完整CRUD操作**：增删改查、分页查询
4. **确保命名规范一致**：包名、类名、字段名遵循项目约定
5. **生成所有必要文件**：pom.xml、配置文件、Java类文件
6. **保持依赖关系正确**：模块间依赖、公共模块集成

请严格按照这个模式生成完整的模块代码。
```

### 第二步：填写具体信息
将模板中的方括号内容替换为实际信息

### 第三步：提交给AI助手
粘贴完整的提示词给AI助手

### 第四步：质量检查
使用检查清单验证生成结果

## 📋 DDD分层架构速览

```
kbpd-{module}/
├── kbpd-{module}-api/           # 对外接口层
├── kbpd-{module}-adapter/       # 适配器层（Controller）
├── kbpd-{module}-application/   # 应用层（编排）
├── kbpd-{module}-bootstrap/     # 启动层
├── kbpd-{module}-domain/        # 领域层（核心业务）
├── kbpd-{module}-infrastructure/# 基础设施层（数据库）
└── kbpd-{module}-types/         # 类型层（常量/异常）
```

## 🎯 核心文件清单

### 必须生成的关键文件
- [ ] `{Entity}Entity.java` - 领域实体
- [ ] `{Entity}Controller.java` - REST控制器
- [ ] `{Entity}Repository.java` - 仓储实现
- [ ] `{Entity}Service.java` - 域服务
- [ ] `{Entity}Mapper.java` - 数据映射
- [ ] `{Entity}PO.java` - 持久化对象
- [ ] `{Module}Application.java` - 启动类

## 🔧 常用配置模板

### application.yml
```yaml
server:
  port: 8080  # 修改端口
  servlet:
    context-path: /{module}  # 修改上下文路径

spring:
  application:
    name: kbpd-{module}-app  # 修改应用名
```

### application-dev.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/kava-{module}  # 修改数据库名
```

## 📦 依赖关系图

```
bootstrap
    ├── adapter
    │   ├── domain
    │   ├── api
    │   └── types
    └── infrastructure
        └── domain
            └── types
```

## ⚡ 快速检查命令

### 编译检查
```bash
mvn clean compile -pl kbpd-{module}
```

### 启动测试
```bash
cd kbpd-{module}/kbpd-{module}-bootstrap
mvn spring-boot:run
```

### API测试
```bash
# 分页查询
curl "http://localhost:8080/{module}/api/v1/{entity}/page"

# 详情查询
curl "http://localhost:8080/{module}/api/v1/{entity}/details?id=1"
```

## 🛠️ 常见问题快速修复

### 编译失败
1. 检查包名是否正确
2. 检查import语句
3. 检查Maven依赖

### 启动失败
1. 检查端口是否冲突
2. 检查数据库连接
3. 检查Nacos配置

### 404错误
1. 检查Controller路径
2. 检查context-path
3. 检查组件扫描

## 📞 支持文档

- 详细指南：`docs/module-generation-guide.md`
- 完整提示词：`docs/ai-prompts-for-module-generation.md`
- 检查清单：`docs/module-checklist.md`

---
**提示**：使用此参考卡片可以在几分钟内生成一个完整的DDD业务模块！