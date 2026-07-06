# Capability: infrastructure-conventions

## Purpose

定义基础设施层的统一约定：审计字段自动填充、错误码格式规范、DDD 架构合规规则，确保全平台的基础设施行为一致。

## Requirements

### Requirement: 审计字段自动填充

系统 SHALL 在执行数据库 insert 操作时自动填充 creator、gmtCreate、modifier、gmtModified、delFlag 字段，在执行 update 操作时自动填充 modifier、gmtModified 字段。

PO 实体中需使用 MyBatis-Plus 的 `@TableField(fill = FieldFill.INSERT)` 和 `@TableField(fill = FieldFill.INSERT_UPDATE)` 注解标记待填充字段。

系统 SHALL 在 `kbpd-common-database` 模块中提供 `MybatisMetaObjectHandler` 实现 `MetaObjectHandler` 接口，通过 `@Component` 注册为 Spring Bean。

#### Scenario: 新增记录时自动填充审计字段

- **WHEN** 通过 MyBatis-Plus 向任意表插入一条新记录，且 PO 实体的 creator、gmtCreate、modifier、gmtModified 字段标注了填充注解
- **THEN** 系统 SHALL 自动将 creator 和 modifier 设为当前认证用户的标识（通过 `SecurityUtils.getUserContext().getUserId()` 获取），gmtCreate 和 gmtModified 设为当前时间，delFlag 设为 "0"

#### Scenario: 更新记录时自动填充审计字段

- **WHEN** 通过 MyBatis-Plus 更新一条记录，且 PO 实体的 modifier、gmtModified 字段标注了填充注解
- **THEN** 系统 SHALL 自动将 modifier 设为当前认证用户的标识（通过 `SecurityUtils.getUserContext().getUserId()` 获取），gmtModified 设为当前时间

#### Scenario: 无认证上下文时的回退

- **WHEN** 系统在没有认证上下文的情况下（如定时任务、系统初始化）执行数据库写入
- **THEN** creator 和 modifier SHALL 设为固定值 "system"

### Requirement: 错误码格式为 8 位数字字符串

所有错误码 MUST 为 8 位数字字符串，格式为 `MM-SS-NNNN`：
- MM（2 位）：大模块编号，`00` 为通用，业务模块从 `10` 开始
- SS（2 位）：子模块编号，通用错误码中表示错误类别
- NNNN（4 位）：具体错误序号，从 `0001` 递增

#### Scenario: 通用错误码格式
- **WHEN** 系统抛出客户端请求方法不支持的异常
- **THEN** 错误码 MUST 为 `"00010001"`，格式符合 `00-01-NNNN`

#### Scenario: UPMS 业务错误码格式
- **WHEN** 系统抛出角色未找到的异常
- **THEN** 错误码 MUST 为 `"10010001"`，格式符合 `10-01-NNNN`

#### Scenario: 错误码无码值冲突
- **WHEN** 遍历 `CommonErrorCodeEnum` 和 `UpmsBizErrorCodeEnum` 的所有枚举值
- **THEN** 任意两个枚举值的 `errorCode` MUST 不相同

#### Scenario: 文档与代码格式一致
- **WHEN** `docs/04-reference/error-codes.md` 描述错误码格式
- **THEN** MUST 使用 8 位数字格式（如 `10010001`）
- **AND** `docs/06-modules/kbpd-upms/business-rules.md` 中的错误码表 MUST 使用相同的 8 位数字格式
- **AND** MUST NOT 使用简写格式（如 `A00101`）

### Requirement: 通用错误码定义

`CommonErrorCodeEnum` MUST 包含以下错误码分区：
- `00-00-xxxx`：系统级错误（系统未知错误等）
- `00-01-xxxx`：客户端请求错误（请求方法、请求体、参数等）
- `00-02-xxxx`：服务端错误（参数校验、数据库、RPC 等）
- `00-03-xxxx`：认证/授权错误（未认证、token 过期、权限不足等）

#### Scenario: 系统未知错误码
- **WHEN** 系统发生未预期的异常
- **THEN** `CommonErrorCodeEnum.SYSTEM_UNKNOWN_ERROR` 的码值 MUST 为 `"00000001"`

#### Scenario: 客户端请求错误码范围
- **WHEN** 客户端发送了不支持的 HTTP 方法
- **THEN** `CommonErrorCodeEnum.CLIENT_HTTP_METHOD_ERROR` 的码值 MUST 为 `"00010001"`

#### Scenario: 认证错误码定义
- **WHEN** 检查认证/授权错误码
- **THEN** `CommonErrorCodeEnum` MUST 包含 `AUTH_UNAUTHORIZED`（`"00030001"`）、`AUTH_TOKEN_EXPIRED`（`"00030002"`）、`AUTH_TOKEN_INVALID`（`"00030003"`）、`AUTH_FORBIDDEN`（`"00030004"`）、`AUTH_TENANT_INVALID`（`"00030005"`）

### Requirement: UPMS 业务错误码定义

`UpmsBizErrorCodeEnum` MUST 按子模块分区，每个子模块使用独立的 SS 编号：
- `10-01`：角色（Role）
- `10-02`：用户（User）
- `10-03`：菜单（Menu）
- `10-04`：租户（Tenant）
- `10-05`：分组（Group）
- `10-06`：OAuth 客户端（Client）
- `10-08`：国际化（I18n）
- `10-09`：应用（App）
- `10-10`：租户应用订阅（TenantApp）

#### Scenario: 角色子模块错误码
- **WHEN** 查询不存在的角色
- **THEN** `UpmsBizErrorCodeEnum.ROLE_NOT_FOUND` 的码值 MUST 为 `"10010001"`

#### Scenario: 菜单子模块错误码
- **WHEN** 菜单的上级菜单设置为自身
- **THEN** `UpmsBizErrorCodeEnum.MENU_PID_SELF_REFERENCE` 的码值 MUST 为 `"10030003"`

#### Scenario: 码值冲突已修复
- **WHEN** 检查 `ROLE_MENU_EMPTY` 和 `MENU_NOT_FOUND` 的码值
- **THEN** 两者 MUST 不同，`ROLE_MENU_EMPTY` 为 `"10010003"`，`MENU_NOT_FOUND` 为 `"10030001"`

#### Scenario: 应用子模块错误码
- **WHEN** 查询不存在的应用
- **THEN** `UpmsBizErrorCodeEnum.APP_NOT_FOUND` 的码值 MUST 为 `"10090001"`

#### Scenario: 租户应用订阅子模块错误码
- **WHEN** 租户重复订阅同一应用
- **THEN** `UpmsBizErrorCodeEnum.TENANT_APP_ALREADY_SUBSCRIBED` 的码值 MUST 为 `"10100001"`

### Requirement: 全局异常处理器统一返回 JsonResult

系统 MUST 提供 `@RestControllerAdvice` 全局异常处理器，确保所有 HTTP 异常响应符合 `JsonResult` 格式。

#### Scenario: 业务异常返回 JsonResult
- **WHEN** Controller 抛出 `BaseBizException`
- **THEN** 响应状态码 MUST 为 200，响应体 MUST 为 `{"success": false, "data": null, "errorCode": "<错误码>", "errorMessage": "<错误信息>"}`

#### Scenario: 参数校验异常返回 JsonResult
- **WHEN** 请求参数校验失败（`MethodArgumentNotValidException`）
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00010002"`，errorMessage 包含校验失败的字段信息

#### Scenario: 请求方法不支持返回 JsonResult
- **WHEN** 客户端使用了不支持的 HTTP 方法（`HttpRequestMethodNotSupportedException`）
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00010001"`

#### Scenario: 未捕获异常返回 JsonResult
- **WHEN** 系统抛出未预期的 `Exception`
- **THEN** 响应体 MUST 为 JsonResult 格式，errorCode 为 `"00000001"`，errorMessage 为 `"System unknown error"`

### Requirement: 已知 bug 修复

系统 MUST 修复已确认的基础设施和应用服务缺陷，保证异常处理、错误码使用和接口契约保持一致。

#### Scenario: AuthenticationFailureEventHandler 正确返回错误码
- **WHEN** OAuth2 认证失败
- **THEN** `AuthenticationFailureEventHandler` MUST 调用 `JsonResult.buildError(error.getErrorCode(), error.getDescription())` 而非 `JsonResult.buildError(error.getErrorCode())`

#### Scenario: SysI18nAppService 使用枚举错误码
- **WHEN** 创建重复的国际化编码
- **THEN** `SysI18nAppService` MUST 抛出 `UpmsBizException(UpmsBizErrorCodeEnum.I18N_CODE_DUPLICATE)` 而非使用硬编码字符串

### Requirement: Domain 层统一使用构造器注入
Domain 层所有类（DomainService 实现、Repository 实现等）MUST 使用构造器注入，MUST NOT 使用 @Resource 或 @Autowired 字段注入。Domain 层允许依赖轻量级 `spring-context`（仅注解），MUST NOT 依赖 `spring-boot-starter`、MyBatis、数据库驱动等重型框架。

#### Scenario: DomainService 使用构造器注入
- **WHEN** DomainService 实现类需要注入 Repository 依赖
- **THEN** 通过构造器参数注入，不使用 @Resource 或 @Autowired 字段注解

#### Scenario: Domain 模块 pom 依赖
- **WHEN** kbpd-upms-domain 模块声明 Spring 依赖
- **THEN** 依赖 spring-context 而非 spring-boot-starter

#### Scenario: CLAUDE.md 架构描述与 ddd-rules.md 一致
- **WHEN** CLAUDE.md 描述 domain 层约束
- **THEN** MUST 使用"domain 不依赖 Spring Boot / MyBatis 等重型框架，允许轻量 spring-context 注解"的措辞
- **AND** MUST NOT 使用"domain 不依赖任何外部框架"的绝对化表述

### Requirement: 未使用的 DomainService 实现不注入 Repository
未被 AppService 调用的 DomainService 实现 MUST NOT 注入 Repository，方法体 MUST 抛出 UnsupportedOperationException。

#### Scenario: 透传 DomainService 移除 Repository 注入
- **WHEN** 一个 DomainService 的所有方法均为纯 Repository 透传（零逻辑）
- **AND** AppService 未调用该 DomainService 的任何方法
- **THEN** 该 DomainService 实现类移除所有 Repository 注入
- **AND** 所有方法体改为 `throw new UnsupportedOperationException("暂未实现")`

#### Scenario: AppService 移除未使用的 DomainService 注入
- **WHEN** AppService 注入了 DomainService 但从未调用其任何方法
- **THEN** 该 DomainService 注入 MUST 被移除

### Requirement: DomainService 禁止跨聚合写
DomainService MUST NOT 直接写入其他聚合的 Repository。跨聚合写操作 MUST 由 AppService 在 @Transactional 内编排。

#### Scenario: 租户创建时初始化管理员角色
- **WHEN** 创建新租户
- **THEN** SysTenantService 只负责创建租户实体
- **AND** SysRoleService 提供 initTenantAdminRole 方法负责角色初始化
- **AND** SysTenantAppService.createTenant 在 @Transactional 内依次调用两者

### Requirement: Adapter 禁止引用 Domain 内部类型
Adapter 层 MUST NOT import domain.model.entity、domain.model.aggregate、domain.service、domain.repository 包中的任何类型。

#### Scenario: AdapterConverter 清理违规 import
- **WHEN** AdapterConverter 文件中存在对 domain.model.entity 的 import
- **THEN** 该 import MUST 被移除

#### Scenario: Adapter 允许引用 domain.model.valobj
- **WHEN** Adapter 需要构造 ID 值对象或 ListQuery
- **THEN** 可以 import domain.model.valobj 包中的类型
