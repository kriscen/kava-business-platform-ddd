<!--
同步影响报告
版本变更：0.0.0 → 1.0.0（首次完整章程）
修改原则列表：
  - 新增：领域优先原则
  - 新增：端口与适配器原则
  - 新增：关键路径测试原则
  - 新增：依赖内向原则
添加章节：
  - 技术约束（基于项目技术栈）
  - 模块结构（基于现有 Maven 多模块）
移除章节：无（首次创建）
模板更新状态：
  - plan-template.md：已验证兼容（宪章检查节）
  - spec-template.md：已验证兼容（用户场景节）
  - tasks-template.md：已验证兼容（阶段结构）
-->

# Kava Business Platform DDD (KBPD) 宪章

## 核心原则

### 一、领域优先

每个业务模块的核心逻辑必须集中在领域层（domain 模块）。领域层包含：
- 实体（Entity）：具有唯一标识的业务对象
- 聚合（Aggregate）：一组相关实体的边界
- 值对象（Value Object）：无标识的不可变对象
- 领域服务（Domain Service）：跨实体的业务逻辑
- 仓储接口（Repository Interface）：持久化抽象

**约束**：
- 领域层不得依赖任何外部技术框架（Spring、数据库驱动等）
- 领域对象必须可独立测试
- 业务规则必须在领域层实现，不得泄露到应用层或适配器层

### 二、端口与适配器

采用六边形架构，通过明确的端口（api）和适配器（adapter/infrastructure）实现核心与外部的解耦。

**端口定义**：
- api 模块定义 DTO、查询对象、请求/响应对象及服务接口
- 端口是领域核心对外交互的契约

**适配器实现**：
- adapter 层：将外部请求（HTTP、RPC）转换为领域层可处理的格式
- infrastructure 层：实现持久化、外部服务访问等技术细节

**约束**：
- 适配器只能依赖端口接口，不得直接访问领域实现
- 技术变更（如切换数据库）只需替换适配器，不影响领域核心

### 三、关键路径测试（非协商）

关键业务路径必须有测试覆盖。关键路径定义：
- 涉及资金、权限、数据完整性的操作
- 核心业务流程的主要入口
- 跨服务边界的集成点

**约束**：
- 关键路径的测试必须在实现前编写
- 测试必须覆盖成功场景和主要失败场景
- 集成测试必须覆盖服务间契约（Dubbo 接口）

### 四、依赖内向

依赖方向必须从外向内指向核心，即：
- adapter/infrastructure → application → domain
- api 模块作为端口定义，可被外部依赖

**约束**：
- domain 模块不得依赖 application、adapter、infrastructure
- application 不得依赖 adapter、infrastructure
- 违反依赖方向的代码必须在架构评审中说明理由

## 技术约束

### 语言与框架

- **语言版本**：Java 21（LTS）
- **核心框架**：Spring Boot 3.3.x、Spring Cloud 2023.x
- **RPC 框架**：Apache Dubbo（服务间通信）
- **安全框架**：Spring Security + OAuth2
- **数据访问**：MyBatis-Plus
- **构建工具**：Maven

### 模块命名

- 业务模块：`kbpd-{业务名}`（如 kbpd-upms、kbpd-member）
- 公共模块：`kbpd-common-{功能名}`（如 kbpd-common-core）
- 模块内部分层：api、application、domain、infrastructure、adapter、types

## 模块结构

### 业务模块内部结构

```text
kbpd-{业务名}/
├── api/           # 端口定义（DTO、接口）
├── application/   # 应用服务（协调领域对象）
├── domain/        # 领域核心（实体、聚合、领域服务）
├── infrastructure/# 基础设施（持久化实现）
├── adapter/       # 适配器（Controller、RPC 实现）
└── types/         # 通用类型（枚举、常量）
```

### 公共模块职责

- `kbpd-common-bom`：统一依赖版本管理
- `kbpd-common-core`：核心基础类、错误码、通用模型
- `kbpd-common-database`：数据库配置和工具
- `kbpd-common-security`：安全配置和工具
- `kbpd-common-cache`：缓存配置和工具

## 治理

### 宪章优先级

本宪章优先于所有其他开发实践。所有 PR 和代码评审必须验证宪章合规性。

### 修订流程

修订本宪章需要：
1. 提出修订理由和影响范围
2. 团队评审和批准
3. 更新相关模板（plan-template.md、spec-template.md、tasks-template.md）
4. 迁移现有代码以符合新原则（如需要）

### 复杂性说明

任何违反宪章原则的复杂性必须在 plan.md 的「复杂度追踪」节中说明：
- 违反的具体原则
- 当前需求的必要性
- 拒绝更简单方案的理由

### 版本策略

- **开发阶段**：使用 `-SNAPSHOT` 后缀（如 1.0.0-SNAPSHOT）
- **发布版本**：MAJOR.MINOR.PATCH 语义版本
  - MAJOR：破坏性变更（API 不兼容）
  - MINOR：新增功能，向后兼容
  - PATCH：Bug 修复，向后兼容

**版本**：1.0.0 | **批准日期**：2026-03-31 | **最后修订**：2026-03-31