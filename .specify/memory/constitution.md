<!--
同步影响报告
版本变更：1.0.0 → 1.1.0（分层策略细化）
修改原则列表：
  - 重构：领域优先原则 → 分级分层原则
  - 新增：模块复杂度分类标准
  - 保留：端口与适配器原则
  - 保留：关键路径测试原则
  - 保留：依赖内向原则
添加章节：
  - 模块分类示例（基于 UPMS 模块）
移除章节：无
模板更新状态：
  - plan-template.md：待验证（新增分层决策节）
  - spec-template.md：待验证（新增复杂度评估节）
  - tasks-template.md：待验证（新增分层策略节）
-->

# Kava Business Platform DDD (KBPD) 宪章

## 核心原则

### 一、分级分层（基于业务复杂度）

**反对形式主义**：不为简单 CRUD 强制创建空壳领域层，避免无意义的代码转发。

模块按业务复杂度采用不同的分层策略：

#### 简单 CRUD 模块

**特征**：无业务规则，纯数据增删改查，无跨实体关联逻辑。

**分层路径**：`adapter → application → repository → infrastructure`

**约束**：
- 不创建空的 `ISysXxxService` 接口和实现
- Application 直接调用 Repository
- 仅保留必要的格式校验（如字段长度）在 Application 层

**适用模块**：日志、行政区划、文件管理、国际化、公共参数、路由配置等

#### 有约束 CRUD 模块

**特征**：有单一实体内的校验规则（唯一性、父子关系、状态约束）。

**分层路径**：`adapter → application → domain(service) → repository → infrastructure`

**约束**：
- Domain Service 实现校验规则（如父部门存在性、名称唯一性）
- 不创建聚合根（单个实体即可表达完整约束）
- Application 协调 Domain Service 和 Repository

**适用模块**：部门（父级校验）、菜单（路由规则）、文件分类（层级约束）等

#### 复杂业务模块

**特征**：跨实体业务规则、聚合行为、状态流转、领域事件。

**分层路径**：`adapter → application → domain(aggregate/service/event) → repository → infrastructure`

**约束**：
- 使用聚合根封装业务行为（如 `SysUserAggregate.assignRole()`）
- 跨聚合通信使用领域事件
- Domain 层不得依赖 Spring 或外部框架
- 聚合根内部保证一致性边界

**适用模块**：用户（角色分配、状态管理）、角色（权限绑定）、租户（隔离规则）、订单（状态流转）等

#### 模块分类决策表

| 判断条件 | 分层策略 |
|----------|----------|
| 仅 CRUD，无规则 | 简单 CRUD → 删除 domain/service |
| 有唯一性/父子校验 | 有约束 CRUD → Domain Service |
| 跨实体操作/状态流转 | 复杂业务 → 聚合根 + 领域事件 |

**治理规则**：
- 每个 PR 需在 `plan.md` 中声明模块复杂度分类
- 新模块开发前需评估复杂度，选择合适分层
- 已存在的空壳领域服务需逐步清理或补充业务逻辑

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

根据复杂度分类选择结构：

**简单 CRUD 模块**：
```text
kbpd-{业务名}/
├── api/           # 端口定义（DTO、接口）
├── application/   # 应用服务（直接调用 Repository）
├── domain/        # 仅保留 types（值对象、查询对象），无 service
├── infrastructure/# 基础设施（持久化实现）
├── adapter/       # 适配器（Controller、RPC 实现）
└── types/         # 通用类型（枚举、常量）
```

**有约束 CRUD 模块**：
```text
kbpd-{业务名}/
├── api/           # 端口定义（DTO、接口）
├── application/   # 应用服务（协调 Domain Service）
├── domain/
│   ├── model/     # 实体、值对象
│   ├── service/   # 领域服务（校验规则）
│   └── repository/# 仓储接口
├── infrastructure/# 基础设施（持久化实现）
├── adapter/       # 适配器（Controller、RPC 实现）
└── types/         # 通用类型（枚举、常量）
```

**复杂业务模块**：
```text
kbpd-{业务名}/
├── api/           # 端口定义（DTO、接口）
├── application/   # 应用服务（编排聚合、发布事件）
├── domain/
│   ├── model/
│   │   ├── aggregate/  # 聚合根（封装业务行为）
│   │   ├── entity/     # 实体
│   │   └── valobj/     # 值对象
│   ├── service/   # 领域服务（跨聚合逻辑）
│   ├── event/     # 领域事件
│   └── repository/# 仓储接口（CQRS 可分离读写）
├── infrastructure/# 基础设施（持久化、事件发布实现）
├── adapter/       # 适配器（Controller、RPC 实现）
└── types/         # 通用类型（枚举、常量）
```

### 公共模块职责

- `kbpd-common-bom`：统一依赖版本管理
- `kbpd-common-core`：核心基础类、错误码、通用模型、标记接口（Entity/Identifier/ValueObject）
- `kbpd-common-database`：数据库配置和工具、PO 基类（TenantDeletablePO）
- `kbpd-common-security`：安全配置和工具
- `kbpd-common-cache`：缓存配置和工具
- `kbpd-common-web`：Web 相关配置、统一响应封装

### UPMS 模块分类示例

| 模块 | 分类 | 分层路径 | 说明 |
|------|------|----------|------|
| SysLog | 简单 CRUD | Application → Repository | 系统日志，无业务规则 |
| SysArea | 简单 CRUD | Application → Repository | 行政区划，纯数据查询 |
| SysFileGroup | 简单 CRUD | Application → Repository | 文件分类，无约束 |
| SysFile | 箇单 CRUD | Application → Repository | 文件管理，无约束 |
| SysI18n | 简单 CRUD | Application → Repository | 国际化，纯字典 |
| SysPublicParam | 简单 CRUD | Application → Repository | 公共参数，无约束 |
| SysRouteConf | 箇单 CRUD | Application → Repository | 路由配置，无约束 |
| SysAuditLog | 简单 CRUD | Application → Repository | 审计日志，无约束 |
| SysDept | 有约束 CRUD | Application → Domain Service | 部门层级校验、父部门存在性 |
| SysMenu | 有约束 CRUD | Application → Domain Service | 菜单路由规则、权限标识唯一性 |
| SysOauthClient | 有约束 CRUD | Application → Domain Service | 客户端配置校验 |
| SysTenant | 复杂业务 | 聚合根 + 领域事件 | 多租户隔离规则、菜单绑定 |
| SysRole | 复杂业务 | 聚合根 + 领域事件 | 权限绑定、用户关联、数据范围 |
| SysUser | 复杂业务 | 聚合根 + 领域事件 | 角色/部门分配、状态管理、密码策略 |

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

**版本**：1.1.0 | **批准日期**：2026-03-31 | **最后修订**：2026-03-31