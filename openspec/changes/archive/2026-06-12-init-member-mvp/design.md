## Context

当前 Member 模块仅有 `MemberInfoDTO` 用于 RPC 数据传输，缺少完整的 DDD 领域模型。UPMS 模块已建立成熟的 DDD 实现模式（聚合根、值对象 ID、读写分离 Repository），可作为参考。

本次初始化 MVP 版本的 Member 领域模型，为后续 C 端用户功能奠定基础。

## Goals / Non-Goals

**Goals:**
- 创建 Member 领域模型（聚合根、值对象）
- 建立读写分离的 Repository 接口
- 实现基础持久化层（PO、Mapper、Repository 实现）
- 提供建表脚本

**Non-Goals:**
- 不实现业务行为（注册、登录等）——MVP 阶段先纯数据模型
- 不实现 Application 层服务
- 不实现 Adapter 层（Controller/RPC）
- 不处理第三方登录（微信、QQ 等）

## Decisions

### 1. MemberId 放置位置

**决策**: 将 `MemberId` 放在 `kbpd-common-core`

**Because**: Member 会被多个模块引用（auth 认证、member 业务等），属于跨模块共享的值对象。按照项目约定"跨模块共享时提升到 common-core"。

**替代方案**: 放在 `kbpd-member-domain` —— 会导致其他模块依赖 member-domain，破坏依赖方向。

### 2. SysAppId 提升

**决策**: 将 `SysAppId` 从 `kbpd-upms-domain` 提升到 `kbpd-common-core`

**Because**: Member 模型需要关联 App，`SysAppId` 成为跨模块共享的值对象。同时保持 ID 命名一致性（`Sys*Id` 前缀）。

**影响**: 需要修改 UPMS 模块中约 8 个文件的 import 路径。

### 3. 表名前缀

**决策**: Member 模块表名使用 `mbr_` 前缀

**Because**:
- 避免 `member_member` 的冗余
- 与 `sys_` 风格统一（简短前缀）
- 三字母缩写足够清晰

**命名规则**: `{模块缩写}_{概念名}`，如 `mbr_member`、`pay_transaction`

### 4. MVP 字段范围

**决策**: MVP 只包含核心认证和归属字段

**Because**: 快速验证领域模型骨架，行为和扩展字段后续迭代。

**MVP 字段**:
- `id`: MemberId — 主键
- `mobile`: String — 登录标识
- `password`: String — 认证凭证
- `tenantId`: SysTenantId — 租户归属
- `appId`: SysAppId — App 归属
- `enabled`: Boolean — 启用状态

**后续迭代字段**: nickname, avatar, wxOpenid, lockFlag 等

### 5. Repository 读写分离

**决策**: 遵循 UPMS 模式，拆分 `IMemberReadRepository` 和 `IMemberWriteRepository`

**Because**: 读写分离是项目既定的 CQRS 模式，读操作可以有更灵活的查询方式，写操作专注于聚合持久化。

## Risks / Trade-offs

**[风险] SysAppId 提升影响 UPMS**
→ 影响范围可控（约 8 个文件），可通过 IDE 批量重构

**[风险] MVP 没有业务行为，聚合根贫血**
→ 可接受。先建立模型骨架，行为在后续 change 中逐步补充

**[权衡] 不实现 Application/Adapter 层**
→ MVP 聚焦领域模型，但意味着无法直接运行验证。后续 change 补充完整链路。
