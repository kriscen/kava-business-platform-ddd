## Context

UPMS 用户创建/更新流程中，密码以明文从 Controller → AppService → DomainService → Repository → 数据库，未做任何加密处理。auth 模块登录时使用 `PasswordEncoder.matches()` 校验 BCrypt hash，导致明文密码永远无法通过校验。

当前 SysLogPO 表名问题（`business-rules.md` 记录为 `@TableName("sys_file_group")`）实际代码已修复为 `"sys_log"`，仅文档未同步。

## Goals / Non-Goals

**Goals:**
- 用户创建时密码 BCrypt 加密后入库
- 用户更新时非空密码加密，空密码保留原值
- 更新 `business-rules.md` 中两条待实现条目的状态

**Non-Goals:**
- 不修改 domain 层代码
- 不引入密码强度校验（后续 change）
- 不实现密码过期检测（P1，后续 change）
- 不实现登录失败锁定策略（P1，后续 change）

## Decisions

### 1. 加密位置放在 SysUserAppService（application 层）

**Because**: domain 层不引入 `spring-security-crypto` 依赖，保持 domain 纯净。密码加密是"明文不得持久化"的基础设施约束，非核心领域规则，放在 application 层合理。

**替代方案**: 在 domain 层 `SysUserService` 注入 PasswordEncoder — 违反 domain 不依赖外部框架原则；在 `SysUserEntity` 上加 `encodePassword()` 方法 — entity 需要依赖 PasswordEncoder 接口，同样污染 domain。

### 2. 使用 `spring-security-crypto` 而非 `spring-boot-starter-security`

**Because**: `spring-security-crypto` 是轻量级模块，仅包含 `PasswordEncoder` 接口和 `BCryptPasswordEncoder` 实现，不引入 Spring Security 全家桶。application 层只需要编码能力，不需要完整的安全框架。

### 3. update 时密码空值跳过

**Because**: 前端更新用户信息时不一定传密码字段（如只改头像）。空密码表示"不修改密码"，应保留数据库中原有的加密密码。非空密码才执行加密覆盖。

### 4. 使用 `DelegatingPasswordEncoder` 而非直接 `BCryptPasswordEncoder`

**Because**: auth 模块已使用 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`，UPMS 加密方式必须与 auth 解密方式一致。`DelegatingPasswordEncoder` 默认使用 BCrypt 但支持算法升级，保持前后兼容。

## Risks / Trade-offs

- **[风险] 已有明文密码无法登录** → 需要数据迁移：对 `sys_user` 表中已有的明文密码执行一次性 BCrypt 加密。本次 change 不包含迁移脚本，后续单独处理。
- **[权衡] 加密逻辑不在 domain 层** → "密码不得明文持久化"是业务规则，严格来说应在 domain 层。但为保持 domain 纯净，接受 application 层实现。未来如需更复杂的密码策略（强度校验、历史密码比对），再考虑在 domain 层定义 `PasswordStrategy` 接口。
